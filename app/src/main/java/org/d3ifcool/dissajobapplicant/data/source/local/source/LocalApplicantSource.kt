package org.d3ifcool.dissajobapplicant.data.source.local.source

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.data.source.local.room.ApplicantDao

class LocalApplicantSource private constructor(
    private val mApplicantDao: ApplicantDao
) {

    companion object {
        private var INSTANCE: LocalApplicantSource? = null

        fun getInstance(applicantDao: ApplicantDao): LocalApplicantSource =
            INSTANCE ?: LocalApplicantSource(applicantDao)
    }

    fun getApplicantData(applicantId: String): LiveData<ApplicantEntity> =
        mApplicantDao.getApplicantData(applicantId)

    fun insertApplicantData(applicant: ApplicantEntity) =
        mApplicantDao.insertApplicantData(applicant)
}