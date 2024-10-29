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
            renderScreenState(it)
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

    private fun renderScreenState(state: RepositoriesListScreenState) {
        binding.repositoriesRecyclerView.isVisible = state is RepositoriesListScreenState.Loaded
        binding.progressBar.isVisible = state is RepositoriesListScreenState.Loading

        binding.errorNotificationContainer.root.isVisible =
            state is RepositoriesListScreenState.Empty
                    || state is RepositoriesListScreenState.ErrorNoConnection
                    || state is RepositoriesListScreenState.ErrorOther

        binding.retryButton.isVisible = state is RepositoriesListScreenState.Empty
                || state is RepositoriesListScreenState.ErrorNoConnection
                || state is RepositoriesListScreenState.ErrorOther

        repositoriesListAdapter.repositoriesList =
            if (state is RepositoriesListScreenState.Loaded) {
                state.repos
            } else {
                emptyList()
            }

        binding.retryButton.text = when (state) {
            is RepositoriesListScreenState.Empty -> getString(R.string.refresh)
            is RepositoriesListScreenState.ErrorOther -> getString(R.string.retry)
            is RepositoriesListScreenState.ErrorNoConnection -> getString(R.string.retry)
            else -> getString(R.string.empty_string)
        }

        with(binding.errorNotificationContainer.errorImage) {
            when (state) {
                is RepositoriesListScreenState.Empty -> setImageResource(R.drawable.ic_empty_list)
                is RepositoriesListScreenState.ErrorNoConnection -> {
                    setImageResource(R.drawable.ic_no_connection)
                }

                is RepositoriesListScreenState.ErrorOther -> {
                    setImageResource(R.drawable.ic_something_error)
                }

                else -> {}
            }
        }

        binding.errorNotificationContainer.errorMainDescription.text = when (state) {
            is RepositoriesListScreenState.Empty -> getString(R.string.empty)
            is RepositoriesListScreenState.ErrorNoConnection -> getString(R.string.connection_error)
            is RepositoriesListScreenState.ErrorOther -> state.error
            else -> getString(R.string.empty_string)
        }

        with(binding.errorNotificationContainer.errorMainDescription) {
            when (state) {
                is RepositoriesListScreenState.Empty -> {
                    setTextColor(getColorFromFragment(R.color.secondary))
                }

                is RepositoriesListScreenState.ErrorNoConnection,
                is RepositoriesListScreenState.ErrorOther -> {
                    setTextColor(getColorFromFragment(R.color.red))
                }

                else -> {}
            }
        }

        binding.errorNotificationContainer.errorAuxiliaryDescription.text = when (state) {
            is RepositoriesListScreenState.Empty -> {
                getString(R.string.no_repositories_at_the_moment)
            }

            is RepositoriesListScreenState.ErrorNoConnection -> {
                getString(R.string.check_your_internet_connection)
            }

            is RepositoriesListScreenState.ErrorOther -> getString(R.string.try_again_later)
            else -> getString(R.string.empty_string)
        }
    }
}