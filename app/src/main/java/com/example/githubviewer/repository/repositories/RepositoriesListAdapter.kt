package com.example.githubviewer.repository.repositories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubviewer.databinding.RepositoriesListItemBinding
import com.example.githubviewer.domain.model.Repo

class RepositoriesListAdapter(
    private val onRepositoryClickListener: (Repo) -> Unit
) : RecyclerView.Adapter<RepositoriesListItemViewHolder>() {

    var repositoriesList: List<Repo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RepositoriesListItemViewHolder {
        val binding = RepositoriesListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RepositoriesListItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return repositoriesList.size
    }

    override fun onBindViewHolder(holder: RepositoriesListItemViewHolder, position: Int) {
        holder.bind(repo = repositoriesList[position])
        holder.itemView.setOnClickListener {
            onRepositoryClickListener(repositoriesList[position])
        }
    }
}