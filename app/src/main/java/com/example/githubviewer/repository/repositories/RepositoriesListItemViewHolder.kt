package com.example.githubviewer.repository.repositories

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.githubviewer.databinding.RepositoriesListItemBinding
import com.example.githubviewer.domain.model.Repo

class RepositoriesListItemViewHolder(
    private val binding: RepositoriesListItemBinding
) : ViewHolder(binding.root) {

    fun bind(repo: Repo) {
        binding.repositoryName.text = repo.name
        binding.programmingLanguage.text = repo.language
        if (repo.description.isBlank()) {
            binding.repositoryDescription.isVisible = false
        } else {
            binding.repositoryDescription.text = repo.description
        }
    }
}