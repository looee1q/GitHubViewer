package com.example.githubviewer.repository.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.githubviewer.R
import com.example.githubviewer.databinding.AuthFragmentBinding
import com.example.githubviewer.repository.util.getColorFromFragment
import com.example.githubviewer.repository.util.hideKeyboard
import com.example.githubviewer.repository.util.setOnIMEActionDoneListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var _binding: AuthFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener {
            viewModel.onSignButtonPressed(binding.inputEditText.text.toString())
            view.hideKeyboard()
            binding.inputEditText.clearFocus()
        }

        binding.inputEditText.setOnIMEActionDoneListener {
            viewModel.onSignButtonPressed(binding.inputEditText.text.toString())
            view.hideKeyboard()
            binding.inputEditText.clearFocus()
        }

        viewModel.screenState.onEach { authScreenState ->
            when (authScreenState) {
                AuthScreenState.Initial -> initialScreenState()
                AuthScreenState.Loading -> loadingScreenState()
                is AuthScreenState.InvalidInput -> invalidInputScreenState()
                AuthScreenState.NoConnection -> noConnectionState()
                AuthScreenState.Idle -> {
                    val navOptions = navOptions {
                        popUpTo(
                            id = R.id.authFragment,
                            popUpToBuilder = { inclusive = true }
                        )
                    }
                    findNavController().navigate(
                        resId = R.id.action_authFragment_to_repositoriesListFragment,
                        args = null,
                        navOptions = navOptions
                    )
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadingScreenState() {
        binding.errorNotificationContainer.root.isVisible = false
        binding.progressBarOnSignInButton.isVisible = true
        binding.textInputLayout.isVisible = true
        binding.textInputLayout.error = null
        binding.signInButton.text = getString(R.string.empty_string)
    }

    private fun initialScreenState() {
        binding.errorNotificationContainer.root.isVisible = false
        binding.progressBarOnSignInButton.isVisible = false
        binding.textInputLayout.isVisible = true
        binding.textInputLayout.error = null
        binding.signInButton.text = getText(R.string.sign_in)
    }

    private fun invalidInputScreenState() {
        binding.errorNotificationContainer.root.isVisible = false
        binding.progressBarOnSignInButton.isVisible = false
        binding.textInputLayout.isVisible = true
        binding.textInputLayout.error = getString(R.string.invalid_token)
        binding.signInButton.text = getText(R.string.sign_in)
    }

    private fun noConnectionState() {
        binding.errorNotificationContainer.root.isVisible = true
        binding.progressBarOnSignInButton.isVisible = false
        binding.textInputLayout.isVisible = false
        binding.signInButton.text = getText(R.string.retry)
        with(binding.errorNotificationContainer) {
            errorImage.setImageResource(R.drawable.ic_no_connection)
            errorMainDescription.text = getString(R.string.connection_error)
            errorMainDescription.setTextColor(getColorFromFragment(R.color.red))
            errorAuxiliaryDescription.text = getString(R.string.check_your_internet_connection)
        }
    }
}