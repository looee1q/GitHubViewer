package com.example.githubviewer.repository.auth

import android.app.AlertDialog
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
            renderScreenState(it)
            if (it is AuthScreenState.AuthSuccess) {
                navigateFromAuthFragment()
            }
            if (it is AuthScreenState.InvalidInput) {
                createErrorDialog(it.reason).show()
            }
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

    private fun renderScreenState(state: AuthScreenState) {
        binding.progressBarOnSignInButton.isVisible = state is AuthScreenState.Loading
        binding.textInputLayout.isVisible = state !is AuthScreenState.NoConnection

        binding.logoIcon.isVisible = !(state is AuthScreenState.NoConnection
                && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)

        binding.textInputLayout.error = if (state is AuthScreenState.InvalidInput) {
            getString(R.string.invalid_token)
        } else null

        binding.signInButton.text = when (state) {
            is AuthScreenState.Loading -> getString(R.string.empty_string)
            is AuthScreenState.NoConnection -> getText(R.string.retry)
            else -> getText(R.string.sign_in)
        }

        with(binding.errorNotificationContainer) {
            root.isVisible = state is AuthScreenState.NoConnection
            errorImage.setImageResource(R.drawable.ic_no_connection)
            errorMainDescription.text = getString(R.string.connection_error)
            errorMainDescription.setTextColor(getColorFromFragment(R.color.red))
            errorAuxiliaryDescription.text = getString(R.string.check_your_internet_connection)
        }
    }

    private fun navigateFromAuthFragment() {
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

    private fun createErrorDialog(errorMessage: String): AlertDialog {
        return AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogTheme)
            .setTitle(getString(R.string.error))
            .setMessage(errorMessage)
            .setPositiveButton(getString(R.string.ok), null)
            .create()
    }
}
