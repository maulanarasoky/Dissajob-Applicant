package org.d3ifcool.dissajobapplicant.data.source.repository.education

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.education.EducationResponseEntity
import org.d3ifcool.dissajobapplicant.ui.education.callback.AddEducationCallback
import org.d3ifcool.dissajobapplicant.ui.education.callback.DeleteEducationCallback
import org.d3ifcool.dissajobapplicant.ui.education.callback.UpdateEducationCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

interface EducationDataSource {
    fun getApplicantEducations(applicantId: String): LiveData<Resource<PagedList<EducationEntity>>>
    fun addApplicantEducation(
        education: EducationResponseEntity,
        callback: AddEducationCallback
    )

    fun updateApplicantEducation(
        education: EducationResponseEntity,
        callback: UpdateEducationCallback
    )

    fun deleteApplicantEducation(
        id: String,
        callback: DeleteEducationCallback
    )
}