package org.d3ifcool.dissajobapplicant.data.source.repository.recruiter

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.vo.Resource

interface RecruiterDataSource {
    fun getRecruiterData(recruiterId: String): LiveData<Resource<RecruiterEntity>>
}