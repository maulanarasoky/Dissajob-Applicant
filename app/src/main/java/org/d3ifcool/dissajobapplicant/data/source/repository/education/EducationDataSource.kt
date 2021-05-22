package org.d3ifcool.dissajobapplicant.data.source.repository.education

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.education.EducationResponseEntity
import org.d3ifcool.dissajobapplicant.utils.InsertToDatabaseCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

interface EducationDataSource {
    fun getApplicantEducations(applicantId: String): LiveData<Resource<PagedList<EducationEntity>>>
    fun addApplicantEducation(
        education: EducationResponseEntity,
        callback: InsertToDatabaseCallback
    )
}