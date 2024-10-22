package com.example.githubviewer.repository.repositories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubviewer.R
import com.example.githubviewer.databinding.RepositoriesListFragmentBinding
import com.example.githubviewer.repository.repositorydetails.DetailInfoFragment
import com.example.githubviewer.repository.util.getColorFromFragment
import com.example.githubviewer.repository.util.setDivider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RepositoriesListFragment : Fragment() {

    private var _binding: RepositoriesListFragmentBinding? = null
    private val binding: RepositoriesListFragmentBinding get() = _binding!!

    private val viewModel: RepositoriesListViewModel by viewModels()

    private val repositoriesListAdapter by lazy {
        RepositoriesListAdapter {
            findNavController().navigate(
                resId = R.id.action_repositoriesListFragment_to_detailInfoFragment,
                args = DetailInfoFragment.createArgs(it.name)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RepositoriesListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenState.onEach { repositoriesListScreenState ->
            when (repositoriesListScreenState) {

                RepositoriesListScreenState.Initial -> Log.d(
                    "RepositoriesListFragmen",
                    "Состояние репозитория: Initial"
                )

                RepositoriesListScreenState.Loading -> {
                    Log.d("RepositoriesListFragmen", "Состояние репозитория: Loading")
                    showLoading()
                }

                is RepositoriesListScreenState.Loaded -> {
                    Log.d("RepositoriesListFragmen", "Состояние репозитория: Loaded")
                    showContent(repositoriesListScreenState)
                }

                RepositoriesListScreenState.Empty -> {
                    Log.d("RepositoriesListFragmen", "Состояние репозитория: Empty")
                    showNotionAboutEmptyRepositoriesList()
                }

                RepositoriesListScreenState.ErrorNoConnection -> {
                    Log.d("RepositoriesListFragmen", "Состояние репозитория: ErrorNoConnection")
                    showNotionAboutConnectionError()
                }

                is RepositoriesListScreenState.ErrorOther -> {
                    Log.d("RepositoriesListFragmen", "Состояние репозитория: ErrorOther")
                    showNotionAboutError()
                }
            }
        }.launchIn(lifecycleScope)

        binding.repositoriesRecyclerView.adapter = repositoriesListAdapter
        binding.repositoriesRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.repositoriesRecyclerView.setDivider(R.drawable.line_divider)

        binding.leaveProfileButton.setOnClickListener {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showContent(repositoriesListScreenState: RepositoriesListScreenState.Loaded) {
        binding.repositoriesRecyclerView.isVisible = true
        binding.progressBar.isVisible = false
        binding.errorNotificationContainer.isVisible = false
        binding.retryButton.isVisible = false
        repositoriesListAdapter.repositoriesList = repositoriesListScreenState.repos
    }

    private fun showLoading() {
        binding.repositoriesRecyclerView.isVisible = false
        binding.progressBar.isVisible = true
        binding.errorNotificationContainer.isVisible = false
        binding.retryButton.isVisible = false
    }

    private fun showNotionAboutEmptyRepositoriesList() {
        binding.repositoriesRecyclerView.isVisible = false
        binding.progressBar.isVisible = false
        binding.errorNotificationContainer.isVisible = true
        binding.retryButton.isVisible = true
        binding.errorImage.setImageResource(R.drawable.ic_empty_list)
        binding.errorMainDescription.text = getString(R.string.empty)
        binding.errorMainDescription.setTextColor(getColorFromFragment(R.color.secondary))
        binding.errorAuxiliaryDescription.text = getString(R.string.no_repositories_at_the_moment)
        binding.retryButton.text = getString(R.string.refresh)
    }

    private fun showNotionAboutConnectionError() {
        binding.repositoriesRecyclerView.isVisible = false
        binding.progressBar.isVisible = false
        binding.errorNotificationContainer.isVisible = true
        binding.retryButton.isVisible = true
        binding.errorImage.setImageResource(R.drawable.ic_no_connection)
        binding.errorMainDescription.text = getString(R.string.connection_error)
        binding.errorMainDescription.setTextColor(getColorFromFragment(R.color.red))
        binding.errorAuxiliaryDescription.text = getString(R.string.check_your_internet_connection)
        binding.retryButton.text = getString(R.string.retry)
    }

    private fun showNotionAboutError() {
        binding.repositoriesRecyclerView.isVisible = false
        binding.progressBar.isVisible = false
        binding.errorNotificationContainer.isVisible = true
        binding.retryButton.isVisible = true
        binding.errorImage.setImageResource(R.drawable.ic_something_error)
        binding.errorMainDescription.text = "Something error"
        binding.errorMainDescription.setTextColor(getColorFromFragment(R.color.red))
        binding.errorAuxiliaryDescription.text = "Check your something"
        binding.retryButton.text = getString(R.string.retry)
    }

}