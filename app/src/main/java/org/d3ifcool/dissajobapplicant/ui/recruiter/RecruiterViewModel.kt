package org.d3ifcool.dissajobapplicant.ui.recruiter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.recruiter.RecruiterRepository
import org.d3ifcool.dissajobapplicant.vo.Resource

class RecruiterViewModel(private val recruiterRepository: RecruiterRepository) : ViewModel() {
    fun getRecruiterData(recruiterId: String): LiveData<Resource<RecruiterEntity>> =
        recruiterRepository.getRecruiterData(recruiterId)

}