package org.d3ifcool.dissajobapplicant.ui.question

import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.interview.InterviewResponseEntity

interface LoadInterviewAnswersCallback {
    fun onAllInterviewAnswersReceived(interviewAnswers: InterviewResponseEntity): InterviewResponseEntity
}