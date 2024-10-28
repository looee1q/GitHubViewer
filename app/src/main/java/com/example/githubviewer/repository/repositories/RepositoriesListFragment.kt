package com.example.githubviewer.repository.repositories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubviewer.R
import com.example.githubviewer.databinding.RepositoriesListFragmentBinding
import com.example.githubviewer.repository.bindingfragment.BindingFragment
import com.example.githubviewer.repository.repositorydetails.DetailInfoFragment
import com.example.githubviewer.repository.util.getColorFromFragment
import com.example.githubviewer.repository.util.setDivider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RepositoriesListFragment : BindingFragment<RepositoriesListFragmentBinding>() {

    private val viewModel: RepositoriesListViewModel by viewModels()

    private val repositoriesListAdapter by lazy {
        RepositoriesListAdapter {
            findNavController().navigate(
                resId = R.id.action_repositoriesListFragment_to_detailInfoFragment,
                args = DetailInfoFragment.createArgs(it.name)
            )
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): RepositoriesListFragmentBinding {
        return RepositoriesListFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenState.onEach {
            render(it)
        }.launchIn(lifecycleScope)

        binding.repositoriesRecyclerView.adapter = repositoriesListAdapter
        binding.repositoriesRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.repositoriesRecyclerView.setDivider(R.drawable.line_divider)

        binding.logoutButton.setOnClickListener {
            viewModel.onLogoutButtonPressed()
            val navOptions = navOptions {
                popUpTo(
                    id = R.id.repositoriesListFragment,
                    popUpToBuilder = { inclusive = true }
                )
            }
            findNavController().navigate(
                resId = R.id.action_repositoriesListFragment_to_authFragment,
                args = null,
                navOptions = navOptions
            )
        }

        binding.retryButton.setOnClickListener {
            viewModel.onRetryButtonPressed()
        }
    }

    private fun render(repositoriesListScreenState: RepositoriesListScreenState) {
        when (repositoriesListScreenState) {
            RepositoriesListScreenState.Initial -> {}
            RepositoriesListScreenState.Loading -> showLoadingState()
            is RepositoriesListScreenState.Loaded -> showLoadedState(repositoriesListScreenState)
            RepositoriesListScreenState.Empty -> showEmptyState()
            RepositoriesListScreenState.ErrorNoConnection -> showNoConnectionState()
            is RepositoriesListScreenState.ErrorOther -> {
                showOtherErrorState(repositoriesListScreenState)
            }
        }
    }

    private fun showLoadingState() {
        binding.repositoriesRecyclerView.isVisible = false
        binding.progressBar.isVisible = true
        binding.errorNotificationContainer.root.isVisible = false
        binding.retryButton.isVisible = false
    }

    private fun showLoadedState(
        repositoriesListScreenState: RepositoriesListScreenState.Loaded
    ) {
        binding.repositoriesRecyclerView.isVisible = true
        binding.progressBar.isVisible = false
        binding.errorNotificationContainer.root.isVisible = false
        binding.retryButton.isVisible = false
        repositoriesListAdapter.repositoriesList = repositoriesListScreenState.repos
    }

    private fun showEmptyState() {
        binding.repositoriesRecyclerView.isVisible = false
        binding.progressBar.isVisible = false
        binding.errorNotificationContainer.root.isVisible = true
        binding.retryButton.isVisible = true
        with(binding.errorNotificationContainer) {
            errorImage.setImageResource(R.drawable.ic_empty_list)
            errorMainDescription.text = getString(R.string.empty)
            errorMainDescription.setTextColor(getColorFromFragment(R.color.secondary))
            errorAuxiliaryDescription.text = getString(R.string.no_repositories_at_the_moment)
        }
        binding.retryButton.text = getString(R.string.refresh)
    }

    private fun showNoConnectionState() {
        binding.repositoriesRecyclerView.isVisible = false
        binding.progressBar.isVisible = false
        binding.errorNotificationContainer.root.isVisible = true
        binding.retryButton.isVisible = true
        with(binding.errorNotificationContainer) {
            errorImage.setImageResource(R.drawable.ic_no_connection)
            errorMainDescription.text = getString(R.string.connection_error)
            errorMainDescription.setTextColor(getColorFromFragment(R.color.red))
            errorAuxiliaryDescription.text = getString(R.string.check_your_internet_connection)
        }
        binding.retryButton.text = getString(R.string.retry)
    }

    private fun showOtherErrorState(
        repositoriesListScreenState: RepositoriesListScreenState.ErrorOther
    ) {
        binding.repositoriesRecyclerView.isVisible = false
        binding.progressBar.isVisible = false
        binding.errorNotificationContainer.root.isVisible = true
        binding.retryButton.isVisible = true
        with(binding.errorNotificationContainer) {
            errorImage.setImageResource(R.drawable.ic_something_error)
            errorMainDescription.text = repositoriesListScreenState.error
            errorMainDescription.setTextColor(getColorFromFragment(R.color.red))
            errorAuxiliaryDescription.text = getString(R.string.try_again_later)
        }
        binding.retryButton.text = getString(R.string.retry)
    }
}