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
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.githubviewer.R
import com.example.githubviewer.databinding.DetailInfoFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailInfoFragment : Fragment() {

    private var _binding: DetailInfoFragmentBinding? = null
    private val binding: DetailInfoFragmentBinding get() = _binding!!

    private val viewModel by viewModels<RepositoryInfoViewModel>()

    private val markwon: Markwon by lazy {
        Markwon.create(requireContext())
    }

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
            findNavController().navigateUp()
        }

        binding.leaveProfileButton.setOnClickListener {
            val navOptions = navOptions {
                popUpTo(
                    id = R.id.detailInfoFragment,
                    popUpToBuilder = { inclusive = true }
                )
            }
            findNavController().navigate(
                resId = R.id.action_detailInfoFragment_to_authFragment,
                args = null,
                navOptions = navOptions
            )
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
            DetailInfoScreenState.Loading -> showLoadingState()
            is DetailInfoScreenState.Loaded -> showLoadedState(detailInfoScreenState)
            DetailInfoScreenState.ErrorNoConnection -> showNoConnectionState()
            is DetailInfoScreenState.ErrorOther -> showOtherErrorState(detailInfoScreenState)
        }
    }

    private fun showLoadingState() {
        binding.contentScrollView.isVisible = false
        binding.repositoryProgressBar.isVisible = true
        binding.repositoryErrorNotificationContainer.root.isVisible = false
        binding.retryButton.isVisible = false
    }

    private fun showLoadedState(detailInfoScreenState: DetailInfoScreenState.Loaded) {
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

        val readmeState = detailInfoScreenState.readmeState
        renderReadmeState(readmeState)
    }

    private fun showNoConnectionState() {
        binding.contentScrollView.isVisible = false
        binding.repositoryProgressBar.isVisible = false
        binding.repositoryErrorNotificationContainer.root.isVisible = true
        binding.retryButton.isVisible = true
        with(binding.repositoryErrorNotificationContainer) {
            errorImage.setImageResource(R.drawable.ic_no_connection)
            errorMainDescription.text = getString(R.string.connection_error)
            errorAuxiliaryDescription.text = getString(R.string.check_your_internet_connection)
        }
    }

    private fun showOtherErrorState(detailInfoScreenState: DetailInfoScreenState.ErrorOther) {
        binding.contentScrollView.isVisible = false
        binding.repositoryProgressBar.isVisible = false
        binding.repositoryErrorNotificationContainer.root.isVisible = true
        binding.retryButton.isVisible = true
        with(binding.repositoryErrorNotificationContainer) {
            errorImage.setImageResource(R.drawable.ic_something_error)
            errorMainDescription.text = detailInfoScreenState.error
            errorAuxiliaryDescription.text = getString(R.string.try_again_later)
        }
    }

    private fun renderReadmeState(readmeState: ReadmeState) {
        when (readmeState) {
            ReadmeState.Initial -> {}
            ReadmeState.Loading -> showReadmeLoadingState()
            is ReadmeState.Loaded -> showReadmeLoadedState(readmeState)
            ReadmeState.Empty -> showReadmeEmptyState()
            ReadmeState.NotExists -> showReadmeNotExistsState()
            ReadmeState.ErrorNoConnection -> showReadmeNoConnectionState()
            is ReadmeState.ErrorOther -> showReadmeOtherErrorState(readmeState)
        }
    }

    private fun showReadmeLoadingState() {
        binding.readmeContent.isVisible = false
        binding.readmeProgressBar.isVisible = true
        binding.readmeErrorNotificationContainer.root.isVisible = false
    }

    private fun showReadmeLoadedState(readmeState: ReadmeState.Loaded) {
        binding.readmeContent.isVisible = true
        binding.readmeProgressBar.isVisible = false
        binding.readmeErrorNotificationContainer.root.isVisible = false
        markwon.setMarkdown(binding.readmeContent, readmeState.markdown)
    }

    private fun showReadmeEmptyState() {
        binding.readmeContent.isVisible = true
        binding.readmeProgressBar.isVisible = false
        binding.readmeErrorNotificationContainer.root.isVisible = false
        markwon.setMarkdown(binding.readmeContent, getString(R.string.readme_is_empty))
    }

    private fun showReadmeNotExistsState() {
        binding.readmeContent.isVisible = true
        binding.readmeProgressBar.isVisible = false
        binding.readmeErrorNotificationContainer.root.isVisible = false
        markwon.setMarkdown(binding.readmeContent, getString(R.string.no_readme))
    }

    private fun showReadmeNoConnectionState() {
        binding.readmeContent.isVisible = false
        binding.readmeProgressBar.isVisible = false
        binding.readmeErrorNotificationContainer.root.isVisible = true
        binding.retryButton.isVisible = true
        with(binding.readmeErrorNotificationContainer) {
            errorImage.setImageResource(R.drawable.ic_no_connection)
            errorMainDescription.text = getString(R.string.connection_error)
            errorAuxiliaryDescription.text =
                getString(R.string.check_your_internet_connection)
        }
    }

    private fun showReadmeOtherErrorState(readmeState: ReadmeState.ErrorOther) {
        binding.readmeContent.isVisible = false
        binding.readmeProgressBar.isVisible = false
        binding.readmeErrorNotificationContainer.root.isVisible = true
        binding.retryButton.isVisible = true
        with(binding.readmeErrorNotificationContainer) {
            errorImage.setImageResource(R.drawable.ic_something_error)
            errorMainDescription.text = readmeState.error
            errorAuxiliaryDescription.text = getString(R.string.try_again_later)
        }
    }

    companion object {
        fun createArgs(repositoryName: String) = bundleOf(
            RepositoryInfoViewModel.REPOSITORY_NAME to repositoryName
        )
    }
}