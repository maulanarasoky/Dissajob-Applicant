package org.d3ifcool.dissajobapplicant.data.source.repository.applicant

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.vo.Resource

interface ApplicantDataSource {
    fun getApplicantData(applicantId: String): LiveData<Resource<ApplicantEntity>>
}