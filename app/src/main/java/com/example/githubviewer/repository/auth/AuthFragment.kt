package com.example.githubviewer.repository.auth

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.githubviewer.R
import com.example.githubviewer.databinding.AuthFragmentBinding
import com.example.githubviewer.repository.bindingfragment.BindingFragment
import com.example.githubviewer.repository.util.getColorFromFragment
import com.example.githubviewer.repository.util.hideKeyboard
import com.example.githubviewer.repository.util.setKeyboardVisibilityListener
import com.example.githubviewer.repository.util.setOnIMEActionDoneListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AuthFragment : BindingFragment<AuthFragmentBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): AuthFragmentBinding {
        return AuthFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener {
            signInActions()
        }

        binding.inputEditText.setOnIMEActionDoneListener {
            signInActions()
        }

        viewModel.screenState.onEach {
            render(it)
        }.launchIn(lifecycleScope)

        setKeyboardVisibilityListener(
            parentView = binding.root,
            onKeyboardHidden = {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    binding.logoIcon.isVisible = true
                }
            },
            onKeyboardShown = {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    binding.logoIcon.isVisible = false
                }
            }
        )
    }

    private fun signInActions() {
        viewModel.onSignButtonPressed(binding.inputEditText.text.toString())
        requireView().hideKeyboard()
        binding.inputEditText.clearFocus()
    }

    private fun render(authScreenState: AuthScreenState) {
        when (authScreenState) {
            AuthScreenState.Initial -> {}
            AuthScreenState.Loading -> showLoadingState()
            is AuthScreenState.InvalidInput -> showInvalidInputState()
            AuthScreenState.NoConnection -> showNoConnectionState()
            AuthScreenState.AuthSuccess -> {
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
    }

    private fun showLoadingState() {
        binding.errorNotificationContainer.root.isVisible = false
        binding.progressBarOnSignInButton.isVisible = true
        binding.textInputLayout.isVisible = true
        binding.textInputLayout.error = null
        binding.signInButton.text = getString(R.string.empty_string)
        binding.logoIcon.isVisible = true
    }

    private fun showInvalidInputState() {
        binding.errorNotificationContainer.root.isVisible = false
        binding.progressBarOnSignInButton.isVisible = false
        binding.textInputLayout.isVisible = true
        binding.textInputLayout.error = getString(R.string.invalid_token)
        binding.signInButton.text = getText(R.string.sign_in)
        binding.logoIcon.isVisible = true
    }

    private fun showNoConnectionState() {
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
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> binding.logoIcon.isVisible = true
            Configuration.ORIENTATION_LANDSCAPE -> binding.logoIcon.isVisible = false
        }
    }
}
