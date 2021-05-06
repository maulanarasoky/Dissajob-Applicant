package org.d3ifcool.dissajobapplicant.ui.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.interview.InterviewEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.interview.InterviewResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.interview.InterviewRepository
import org.d3ifcool.dissajobapplicant.vo.Resource

class InterviewViewModel(private val interviewRepository: InterviewRepository) : ViewModel() {
    fun getInterviewAnswers(jobId: String): LiveData<Resource<PagedList<InterviewEntity>>> =
        interviewRepository.getInterviewAnswers(jobId)

    fun insertApplication(
        interviewAnswer: InterviewResponseEntity,
        callback: InsertInterviewAnswersCallback
    ) =
        interviewRepository.insertInterviewAnswers(interviewAnswer, callback)
}