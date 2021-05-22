package org.d3ifcool.dissajobapplicant.data.source.local.source

import androidx.paging.DataSource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.experience.ExperienceEntity
import org.d3ifcool.dissajobapplicant.data.source.local.room.ExperienceDao

class LocalExperienceSource private constructor(
    private val mExperienceDao: ExperienceDao
) {

    companion object {
        private var INSTANCE: LocalExperienceSource? = null

        fun getInstance(experienceDao: ExperienceDao): LocalExperienceSource =
            INSTANCE ?: LocalExperienceSource(experienceDao)
    }

    fun getApplicantExperiences(applicantId: String): DataSource.Factory<Int, ExperienceEntity> =
        mExperienceDao.getApplicantExperiences(applicantId)

    fun insertApplicantExperiences(experiences: List<ExperienceEntity>) =
        mExperienceDao.insertApplicantExperiences(experiences)

}