package com.example.githubviewer.repository.repositorydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.githubviewer.R
import com.example.githubviewer.databinding.DetailInfoFragmentBinding
import com.example.githubviewer.repository.util.getColorFromFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailInfoFragment : Fragment() {

    private var _binding: DetailInfoFragmentBinding? = null
    private val binding: DetailInfoFragmentBinding get() = _binding!!

    private val viewModel by viewModels<RepositoryInfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailInfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.screenHeaderTitle.text = requireArguments().getString(
            RepositoryInfoViewModel.REPOSITORY_NAME
        )

        binding.navigationBackButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.leaveProfileButton.setOnClickListener {
            //
        }

        binding.linkText.setOnClickListener {
            //
        }

        binding.retryButton.setOnClickListener {
            //
        }

        viewModel.screenState.onEach {
            render(it)
        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(detailInfoScreenState: DetailInfoScreenState) {
        when (detailInfoScreenState) {
            DetailInfoScreenState.Initial -> {}
            DetailInfoScreenState.Loading -> showLoading()
            is DetailInfoScreenState.Loaded -> showContent(detailInfoScreenState)
            DetailInfoScreenState.ErrorNoConnection -> showNotionAboutConnectionError()
            is DetailInfoScreenState.ErrorOther -> showNotionAboutError()
        }
    }

    private fun showContent(detailInfoScreenState: DetailInfoScreenState.Loaded) {
        val repoDetails = detailInfoScreenState.repoDetails
        binding.contentScrollView.isVisible = true
        binding.linkText.text = repoDetails.url
        if (repoDetails.licenseName.isNotEmpty()) {
            binding.licenseInfoContainer.isVisible = true
            binding.licenseNameText.text = repoDetails.licenseName
        } else {
            binding.licenseInfoContainer.isVisible = false
        }
        binding.starsCountNumber.text = repoDetails.stargazersCount.toString()
        binding.starsTitle.text = resources.getQuantityString(
            R.plurals.star_plurals, repoDetails.stargazersCount
        )
        binding.forksCountNumber.text = repoDetails.forksCount.toString()
        binding.forksTitle.text = resources.getQuantityString(
            R.plurals.fork_plurals, repoDetails.forksCount
        )
        binding.watchersCountNumber.text = repoDetails.watchersCount.toString()
        binding.watchersTitle.text = resources.getQuantityString(
            R.plurals.watcher_plurals, repoDetails.watchersCount
        )
        binding.repositoryProgressBar.isVisible = false
        binding.repositoryErrorNotificationContainer.root.isVisible = false
        binding.retryButton.isVisible = false
    }

    private fun showLoading() {
        binding.contentScrollView.isVisible = false
        binding.repositoryProgressBar.isVisible = true
        binding.repositoryErrorNotificationContainer.root.isVisible = false
        binding.retryButton.isVisible = false
    }

    private fun showNotionAboutConnectionError() {
        binding.contentScrollView.isVisible = false
        binding.repositoryProgressBar.isVisible = false
        binding.repositoryErrorNotificationContainer.root.isVisible = true
        binding.retryButton.isVisible = true
        with(binding.repositoryErrorNotificationContainer) {
            errorImage.setImageResource(R.drawable.ic_no_connection)
            errorMainDescription.text = getString(R.string.connection_error)
            errorMainDescription.setTextColor(getColorFromFragment(R.color.red))
            errorAuxiliaryDescription.text = getString(R.string.check_your_internet_connection)
        }
    }

    private fun showNotionAboutError() {
        binding.contentScrollView.isVisible = false
        binding.repositoryProgressBar.isVisible = false
        binding.repositoryErrorNotificationContainer.root.isVisible = true
        binding.retryButton.isVisible = true
        with(binding.repositoryErrorNotificationContainer) {
            errorImage.setImageResource(R.drawable.ic_something_error)
            errorMainDescription.text = "Something error"
            errorMainDescription.setTextColor(getColorFromFragment(R.color.red))
            errorAuxiliaryDescription.text = "Check your something"
        }
    }

    companion object {
        fun createArgs(repositoryName: String) = bundleOf(
            RepositoryInfoViewModel.REPOSITORY_NAME to repositoryName
        )
    }
}