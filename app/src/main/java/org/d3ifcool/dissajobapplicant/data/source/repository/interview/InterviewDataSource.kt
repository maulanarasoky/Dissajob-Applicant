package org.d3ifcool.dissajobapplicant.data.source.repository.interview

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.interview.InterviewEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.interview.InterviewResponseEntity
import org.d3ifcool.dissajobapplicant.ui.question.InsertInterviewAnswersCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

interface InterviewDataSource {
    fun getInterviewAnswers(applicationId: String): LiveData<Resource<PagedList<InterviewEntity>>>
    fun insertInterviewAnswers(
        interviewAnswer: InterviewResponseEntity,
        callback: InsertInterviewAnswersCallback
    )
}