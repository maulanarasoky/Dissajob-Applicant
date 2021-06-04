package org.d3ifcool.dissajobapplicant.ui.education

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobapplicant.databinding.EducationItemBinding
import org.d3ifcool.dissajobapplicant.ui.education.callback.OnEducationItemClickListener
import java.text.DateFormatSymbols

class EducationAdapter(private val onItemClickCallback: OnEducationItemClickListener) :
    PagedListAdapter<EducationEntity, EducationAdapter.EducationViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EducationEntity>() {
            override fun areItemsTheSame(
                oldItem: EducationEntity,
                newItem: EducationEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: EducationEntity,
                newItem: EducationEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {
        val itemsEducationBinding =
            EducationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EducationViewHolder(itemsEducationBinding)
    }

    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        val education = getItem(position)
        if (education != null) {
            holder.bindItem(education)
        }
    }

    inner class EducationViewHolder(private val binding: EducationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(items: EducationEntity) {
            with(binding) {
                tvSchoolName.text = items.schoolName.toString()
                if (items.fieldOfStudy != "-") {
                    tvEducationLevel.text = itemView.resources.getString(
                        R.string.txt_education_level_field_of_study,
                        items.educationLevel.toString(),
                        items.fieldOfStudy.toString()
                    )
                } else {
                    tvEducationLevel.text = items.educationLevel.toString()
                }
                val startMonth = DateFormatSymbols().months[items.startMonth - 1]
                val endMonth = DateFormatSymbols().months[items.endMonth - 1]
                val startDate = "$startMonth ${items.startYear}"
                val endDate = "$endMonth ${items.endYear}"

                tvEducationRangeDate.text =
                    itemView.resources.getString(R.string.txt_range_date, startDate, endDate)

                if (items.description != "-") {
                    tvEducationDescription.text = items.description.toString()
                    tvEducationDescription.visibility = View.VISIBLE
                }

                itemView.setOnClickListener {
                    onItemClickCallback.onClickItem(items)
                }
            }
        }
    }
}