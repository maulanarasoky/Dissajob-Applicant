package org.d3ifcool.dissajobapplicant.data.source.local.source

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.data.source.local.room.RecruiterDao

class LocalRecruiterSource private constructor(
    private val mRecruiterDao: RecruiterDao
) {

    companion object {
        private var INSTANCE: LocalRecruiterSource? = null

        fun getInstance(recruiterDao: RecruiterDao): LocalRecruiterSource =
            INSTANCE ?: LocalRecruiterSource(recruiterDao)
    }

    fun getRecruiterData(recruiterId: String): LiveData<RecruiterEntity> =
        mRecruiterDao.getRecruiterData(recruiterId)

    fun insertRecruiterData(recruiterProfile: RecruiterEntity) =
        mRecruiterDao.insertRecruiterData(recruiterProfile)
}