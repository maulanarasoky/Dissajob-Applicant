package org.d3ifcool.dissajobapplicant.data.source.local.source

import androidx.paging.DataSource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.room.EducationDao

class LocalEducationSource private constructor(
    private val mEducationDao: EducationDao
) {

    companion object {
        private var INSTANCE: LocalEducationSource? = null

        fun getInstance(educationDao: EducationDao): LocalEducationSource =
            INSTANCE ?: LocalEducationSource(educationDao)
    }

    fun getApplicantEducations(applicantId: String): DataSource.Factory<Int, EducationEntity> =
        mEducationDao.getApplicantEducations(applicantId)

    fun insertApplicantEducations(educations: List<EducationEntity>) =
        mEducationDao.insertApplicantEducations(educations)

    fun deleteEducationById(id: String) = mEducationDao.deleteEducationById(id)

    fun deleteAllApplicantEducations(applicantId: String) =
        mEducationDao.deleteAllApplicantEducations(applicantId)
}