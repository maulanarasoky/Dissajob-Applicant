package org.d3ifcool.dissajobapplicant.ui.experience

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.experience.ExperienceEntity
import org.d3ifcool.dissajobapplicant.databinding.ExperienceItemBinding
import org.d3ifcool.dissajobapplicant.ui.experience.callback.OnExperienceItemClickListener
import java.text.DateFormatSymbols

class ExperienceAdapter(private val onItemClickCallback: OnExperienceItemClickListener) :
    PagedListAdapter<ExperienceEntity, ExperienceAdapter.ExperienceViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ExperienceEntity>() {
            override fun areItemsTheSame(
                oldItem: ExperienceEntity,
                newItem: ExperienceEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ExperienceEntity,
                newItem: ExperienceEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val itemsExperienceBinding =
            ExperienceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExperienceViewHolder(itemsExperienceBinding)
    }

    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
        val experience = getItem(position)
        if (experience != null) {
            holder.bindItem(experience)
        }
    }

    inner class ExperienceViewHolder(private val binding: ExperienceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(items: ExperienceEntity) {
            with(binding) {
                tvJobRole.text = items.title.toString()
                tvCompanyName.text = itemView.resources.getString(
                    R.string.txt_company_type,
                    items.companyName.toString(),
                    items.employmentType.toString()
                )
                val startMonth = DateFormatSymbols().months[items.startMonth - 1]
                val startDate = "$startMonth ${items.startYear}"
                val endDate = if (items.isCurrentlyWorking) {
                    itemView.resources.getString(R.string.txt_present)
                } else {
                    val endMonth = DateFormatSymbols().months[items.endMonth - 1]
                    "$endMonth ${items.endYear}"
                }

                tvJobRangeDate.text =
                    itemView.resources.getString(R.string.txt_range_date, startDate, endDate)

                itemView.setOnClickListener {
                    onItemClickCallback.onClickItem(items)
                }
            }
        }
    }
}