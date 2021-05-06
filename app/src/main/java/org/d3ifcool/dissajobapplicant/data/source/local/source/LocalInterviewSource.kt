package org.d3ifcool.dissajobapplicant.data.source.local.source

import androidx.paging.DataSource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.interview.InterviewEntity
import org.d3ifcool.dissajobapplicant.data.source.local.room.InterviewDao

class LocalInterviewSource private constructor(
    private val mInterviewDao: InterviewDao
) {

    companion object {
        private var INSTANCE: LocalInterviewSource? = null

        fun getInstance(interviewDao: InterviewDao): LocalInterviewSource =
            INSTANCE ?: LocalInterviewSource(interviewDao)
    }

    fun getInterviewAnswers(jobId: String): DataSource.Factory<Int, InterviewEntity> =
        mInterviewDao.getInterviewAnswers(jobId)

    fun insertInterviewAnswers(answers: List<InterviewEntity>) =
        mInterviewDao.insertInterviewAnswers(answers)
}