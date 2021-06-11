package org.d3ifcool.dissajobapplicant.data.source.local.source

import androidx.lifecycle.LiveData
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

    fun getInterviewAnswers(applicationId: String): LiveData<InterviewEntity> =
        mInterviewDao.getInterviewAnswers(applicationId)

    fun insertInterviewAnswers(answers: InterviewEntity) =
        mInterviewDao.insertInterviewAnswers(answers)
}