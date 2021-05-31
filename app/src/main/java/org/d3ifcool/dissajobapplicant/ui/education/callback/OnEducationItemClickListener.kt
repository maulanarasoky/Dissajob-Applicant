package org.d3ifcool.dissajobapplicant.ui.education.callback

import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity

interface OnEducationItemClickListener {
    fun onClickItem(educationData: EducationEntity)
}