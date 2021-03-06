package org.d3ifcool.dissajobapplicant.ui.question

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.d3ifcool.dissajobapplicant.data.source.local.entity.interview.InterviewEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.interview.InterviewResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.interview.InterviewRepository
import org.d3ifcool.dissajobapplicant.vo.Resource

class InterviewViewModel(private val interviewRepository: InterviewRepository) : ViewModel() {
    fun getInterviewAnswers(applicationId: String): LiveData<Resource<InterviewEntity>> =
        interviewRepository.getInterviewAnswers(applicationId)

    fun insertInterviewAnswers(
        interviewAnswer: InterviewResponseEntity,
        callback: InsertInterviewAnswersCallback
    ) =
        interviewRepository.insertInterviewAnswers(interviewAnswer, callback)
}