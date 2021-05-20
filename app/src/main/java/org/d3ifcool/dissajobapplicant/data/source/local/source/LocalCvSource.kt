package org.d3ifcool.dissajobapplicant.data.source.local.source

import androidx.paging.DataSource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.cv.CvEntity
import org.d3ifcool.dissajobapplicant.data.source.local.room.CvDao

class LocalCvSource private constructor(
    private val mCvDao: CvDao
) {

    companion object {
        private var INSTANCE: LocalCvSource? = null

        fun getInstance(cvDao: CvDao): LocalCvSource =
            INSTANCE ?: LocalCvSource(cvDao)
    }

    fun getApplicantCv(applicantId: String): DataSource.Factory<Int, CvEntity> =
        mCvDao.getApplicantCv(applicantId)

    fun insertCv(cv: List<CvEntity>) = mCvDao.insertCv(cv)
}