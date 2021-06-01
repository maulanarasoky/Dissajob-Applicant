package org.d3ifcool.dissajobapplicant.ui.experience.callback

import org.d3ifcool.dissajobapplicant.data.source.local.entity.experience.ExperienceEntity

interface OnExperienceItemClickListener {
    fun onClickItem(experienceData: ExperienceEntity)
}