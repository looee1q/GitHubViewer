package com.example.githubviewer.repository.repositories

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.githubviewer.databinding.RepositoriesListItemBinding
import com.example.githubviewer.domain.model.RepositoryInfo

class RepositoriesListItemViewHolder(
    private val binding: RepositoriesListItemBinding
) : ViewHolder(binding.root) {

    fun bind(repositoryInfo: RepositoryInfo) {
        binding.repositoryName.text = repositoryInfo.name
        binding.programmingLanguage.text = repositoryInfo.language
        if (repositoryInfo.description.isBlank()) {
            binding.repositoryDescription.isVisible = false
        } else {
            binding.repositoryDescription.text = repositoryInfo.description
        }
    }
}