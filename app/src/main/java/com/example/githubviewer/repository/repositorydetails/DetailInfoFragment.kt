package com.example.githubviewer.repository.repositorydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.githubviewer.R
import com.example.githubviewer.databinding.DetailInfoFragmentBinding
import com.example.githubviewer.repository.bindingfragment.BindingFragment
import com.example.githubviewer.repository.util.openWebPage
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class DetailInfoFragment : BindingFragment<DetailInfoFragmentBinding>() {

    private val viewModel by viewModels<RepositoryInfoViewModel>()

    @Inject
    lateinit var markwon: Markwon

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DetailInfoFragmentBinding {
        return DetailInfoFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.screenHeaderTitle.text = requireArguments().getString(
            RepositoryInfoViewModel.REPOSITORY_NAME
        )

        binding.navigationBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.logoutButton.setOnClickListener {
            viewModel.onLogoutButtonPressed()
            val navOptions = navOptions {
                popUpTo(
                    id = R.id.repositoriesListFragment,
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
            openWebPage(
                context = requireContext(),
                url = binding.linkText.text.toString()
            )
        }

        binding.retryButton.setOnClickListener {
            viewModel.onRetryButtonPressed()
        }

        viewModel.screenState.onEach {
            renderScreenState(it)
            if (it is DetailInfoScreenState.Loaded) {
                renderReadmeState(it.readmeState)
            }
        }.launchIn(lifecycleScope)
    }

    private fun renderScreenState(state: DetailInfoScreenState) {
        binding.contentScrollView.isVisible = state is DetailInfoScreenState.Loaded
        binding.repositoryProgressBar.isVisible = state is DetailInfoScreenState.Loading

        binding.repositoryErrorNotificationContainer.root.isVisible =
            state is DetailInfoScreenState.ErrorNoConnection
                    || state is DetailInfoScreenState.ErrorOther

        binding.retryButton.isVisible = state is DetailInfoScreenState.ErrorNoConnection
                || state is DetailInfoScreenState.ErrorOther

        with(binding.repositoryErrorNotificationContainer.errorImage) {
            when (state) {
                is DetailInfoScreenState.ErrorNoConnection -> {
                    setImageResource(R.drawable.ic_no_connection)
                }

                is DetailInfoScreenState.ErrorOther -> {
                    setImageResource(R.drawable.ic_something_error)
                }

                else -> {}
            }
        }

        binding.repositoryErrorNotificationContainer.errorMainDescription.text = when (state) {
            is DetailInfoScreenState.ErrorNoConnection -> getString(R.string.connection_error)
            is DetailInfoScreenState.ErrorOther -> state.error
            else -> getString(R.string.empty_string)
        }

        binding.repositoryErrorNotificationContainer.errorAuxiliaryDescription.text = when (state) {
            is DetailInfoScreenState.ErrorNoConnection -> {
                getString(R.string.check_your_internet_connection)
            }

            is DetailInfoScreenState.ErrorOther -> getString(R.string.try_again_later)
            else -> getString(R.string.empty_string)
        }

        binding.linkText.text = if (state is DetailInfoScreenState.Loaded) {
            state.repoDetails.url
        } else {
            getString(R.string.empty_string)
        }

        binding.licenseInfoContainer.isVisible =
            state is DetailInfoScreenState.Loaded && state.repoDetails.licenseName.isNotEmpty()

        binding.licenseNameText.text =
            if (state is DetailInfoScreenState.Loaded
                && state.repoDetails.licenseName.isNotEmpty()
            ) {
                state.repoDetails.licenseName
            } else {
                getString(R.string.empty_string)
            }

        binding.starsCountNumber.text = if (state is DetailInfoScreenState.Loaded) {
            state.repoDetails.stargazersCount.toString()
        } else {
            getString(R.string.empty_string)
        }

        binding.starsTitle.text = if (state is DetailInfoScreenState.Loaded) {
            resources.getQuantityString(R.plurals.star_plurals, state.repoDetails.stargazersCount)
        } else {
            getString(R.string.empty_string)
        }

        binding.forksCountNumber.text = if (state is DetailInfoScreenState.Loaded) {
            state.repoDetails.forksCount.toString()
        } else {
            getString(R.string.empty_string)
        }

        binding.forksTitle.text = if (state is DetailInfoScreenState.Loaded) {
            resources.getQuantityString(R.plurals.fork_plurals, state.repoDetails.forksCount)
        } else {
            getString(R.string.empty_string)
        }

        binding.watchersCountNumber.text = if (state is DetailInfoScreenState.Loaded) {
            state.repoDetails.watchersCount.toString()
        } else {
            getString(R.string.empty_string)
        }

        binding.watchersTitle.text = if (state is DetailInfoScreenState.Loaded) {
            resources.getQuantityString(R.plurals.watcher_plurals, state.repoDetails.watchersCount)
        } else {
            getString(R.string.empty_string)
        }
    }

    private fun renderReadmeState(state: ReadmeState) {
        binding.readmeContent.isVisible = when (state) {
            is ReadmeState.Loaded, is ReadmeState.Empty, is ReadmeState.NotExists -> true
            else -> false
        }

        binding.readmeProgressBar.isVisible = state is ReadmeState.Loading

        when (state) {
            is ReadmeState.Loaded -> markwon.setMarkdown(binding.readmeContent, state.markdown)
            is ReadmeState.Empty -> markwon.setMarkdown(
                binding.readmeContent,
                getString(R.string.readme_is_empty)
            )

            is ReadmeState.NotExists -> markwon.setMarkdown(
                binding.readmeContent,
                getString(R.string.no_readme)
            )

            else -> {}
        }

        binding.readmeErrorNotificationContainer.root.isVisible =
            state is ReadmeState.ErrorNoConnection || state is ReadmeState.ErrorOther

        binding.retryButton.isVisible =
            state is ReadmeState.ErrorNoConnection || state is ReadmeState.ErrorOther

        with(binding.readmeErrorNotificationContainer.errorImage) {
            when (state) {
                is ReadmeState.ErrorNoConnection -> setImageResource(R.drawable.ic_no_connection)
                is ReadmeState.ErrorOther -> setImageResource(R.drawable.ic_something_error)
                else -> {}
            }
        }

        binding.readmeErrorNotificationContainer.errorMainDescription.text = when (state) {
            is ReadmeState.ErrorNoConnection -> getString(R.string.connection_error)
            is ReadmeState.ErrorOther -> state.error
            else -> getString(R.string.empty_string)
        }

        binding.readmeErrorNotificationContainer.errorAuxiliaryDescription.text = when (state) {
            is ReadmeState.ErrorNoConnection -> getString(R.string.check_your_internet_connection)
            is ReadmeState.ErrorOther -> getString(R.string.try_again_later)
            else -> getString(R.string.empty_string)
        }
    }

    companion object {
        fun createArgs(repositoryName: String) = bundleOf(
            RepositoryInfoViewModel.REPOSITORY_NAME to repositoryName
        )
    }
}