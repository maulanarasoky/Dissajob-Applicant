package org.d3ifcool.dissajobapplicant.ui.education

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobapplicant.databinding.EducationItemBinding

class EducationAdapter :
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
                tvInstitutionName.text = items.schoolName.toString()
                tvEducationDegree.text = itemView.resources.getString(
                    R.string.txt_company_type,
                    items.degree.toString(),
                    items.fieldOfStudy.toString()
                )
                val startDate = "${items.startMonth} ${items.startYear}"
                val endDate = "${items.endMonth} ${items.endYear}"

                tvEducationRangeDate.text =
                    itemView.resources.getString(R.string.txt_range_date, startDate, endDate)
            }
        }
    }
}