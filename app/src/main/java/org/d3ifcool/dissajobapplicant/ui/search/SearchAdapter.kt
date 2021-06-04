package org.d3ifcool.dissajobapplicant.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.dissajobapplicant.data.source.local.entity.history.SearchHistoryEntity
import org.d3ifcool.dissajobapplicant.databinding.SearchHistoryItemBinding
import org.d3ifcool.dissajobapplicant.ui.search.callback.SearchHistoryDeleteClickCallback
import org.d3ifcool.dissajobapplicant.ui.search.callback.SearchHistoryItemClickCallback

class SearchAdapter(
    private val onItemClickCallback: SearchHistoryItemClickCallback,
    private val deleteCallback: SearchHistoryDeleteClickCallback
) :
    PagedListAdapter<SearchHistoryEntity, SearchAdapter.SearchHistoryViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchHistoryEntity>() {
            override fun areItemsTheSame(
                oldItem: SearchHistoryEntity,
                newItem: SearchHistoryEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SearchHistoryEntity,
                newItem: SearchHistoryEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val itemSearchHistoryBinding =
            SearchHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchHistoryViewHolder(itemSearchHistoryBinding)
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val job = getItem(position)
        if (job != null) {
            holder.bindItem(job)
        }
    }

    inner class SearchHistoryViewHolder(private val binding: SearchHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(items: SearchHistoryEntity) {
            with(binding) {
                tvSearchedText.text = items.searchText.toString()
                imgDeleteHistory.setOnClickListener {
                    deleteCallback.deleteSearchHistory(items.id)
                }

                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(items.searchText.toString())
                }
            }
        }
    }
}