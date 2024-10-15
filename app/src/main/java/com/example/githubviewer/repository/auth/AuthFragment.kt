package com.example.githubviewer.repository.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.githubviewer.R
import com.example.githubviewer.databinding.AuthFragmentBinding
import com.example.githubviewer.repository.util.hideKeyboard
import com.example.githubviewer.repository.util.setOnIMEActionDoneListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
            Log.d("AuthFragment", "Нажал на кнопку sign in")
            viewModel.onSignButtonPressed(binding.inputEditText.text.toString())
            view.hideKeyboard()
            binding.inputEditText.clearFocus()
        }

        binding.inputEditText.setOnIMEActionDoneListener {
            Log.d("AuthFragment", "Нажал на кнопку enter на клавиатуре")
            viewModel.onSignButtonPressed(binding.inputEditText.text.toString())
            view.hideKeyboard()
            binding.inputEditText.clearFocus()
        }

        viewModel.screenState.onEach { authScreenState ->
            when (authScreenState) {
                is AuthScreenState.InvalidInput -> invalidInputScreenState()
                AuthScreenState.Loading -> loadingScreenState()
                AuthScreenState.Initial -> {}
                AuthScreenState.Idle -> {}
            }
        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadingScreenState() {
        binding.progressBarOnSignInButton.isVisible = true
        binding.textInputLayout.error = null
        binding.signInButton.text = getString(R.string.empty_string)
    }

    private fun invalidInputScreenState() {
        binding.progressBarOnSignInButton.isVisible = false
        binding.textInputLayout.error = getString(R.string.invalid_token)
        binding.signInButton.text = resources.getText(R.string.sign_in)
    }
}