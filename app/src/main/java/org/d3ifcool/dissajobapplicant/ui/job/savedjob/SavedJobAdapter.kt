package org.d3ifcool.dissajobapplicant.ui.job.savedjob

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.SavedJobEntity
import org.d3ifcool.dissajobapplicant.databinding.JobItemBinding
import org.d3ifcool.dissajobapplicant.ui.job.callback.ItemClickListener
import org.d3ifcool.dissajobapplicant.ui.recruiter.LoadRecruiterDataCallback

class SavedJobAdapter(
    private val clickCallback: ItemClickListener,
    private val loadCallback: LoadRecruiterDataCallback
) : PagedListAdapter<SavedJobEntity, SavedJobAdapter.JobViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SavedJobEntity>() {
            override fun areItemsTheSame(
                oldItem: SavedJobEntity,
                newItem: SavedJobEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SavedJobEntity,
                newItem: SavedJobEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val itemsJobBinding = JobItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(itemsJobBinding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position)
        if (job != null) {
            holder.bindItem(job)
        }
    }

    inner class JobViewHolder(private val binding: JobItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(items: SavedJobEntity) {
            with(binding) {

            }
        }
    }
}