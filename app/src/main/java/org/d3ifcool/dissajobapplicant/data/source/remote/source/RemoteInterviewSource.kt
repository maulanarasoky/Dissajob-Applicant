package org.d3ifcool.dissajobapplicant.data.source.remote.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.interview.InterviewResponseEntity
import org.d3ifcool.dissajobapplicant.ui.question.InsertInterviewAnswersCallback
import org.d3ifcool.dissajobapplicant.ui.question.LoadInterviewAnswersCallback
import org.d3ifcool.dissajobapplicant.utils.EspressoIdlingResource
import org.d3ifcool.dissajobapplicant.utils.InterviewHelper

class RemoteInterviewSource private constructor(
    private val mInterviewHelper: InterviewHelper
) {
    companion object {
        @Volatile
        private var instance: RemoteInterviewSource? = null

        fun getInstance(interviewHelper: InterviewHelper): RemoteInterviewSource =
            instance ?: synchronized(this) {
                instance ?: RemoteInterviewSource(interviewHelper)
            }
    }

    fun getInterviewAnswers(
        jobId: String,
        callback: LoadInterviewAnswersCallback
    ): LiveData<ApiResponse<List<InterviewResponseEntity>>> {
        EspressoIdlingResource.increment()
        val resultAnswer = MutableLiveData<ApiResponse<List<InterviewResponseEntity>>>()
        mInterviewHelper.getInterviewAnswers(jobId, object : LoadInterviewAnswersCallback {
            override fun onAllInterviewAnswersReceived(interviewAnswers: List<InterviewResponseEntity>): List<InterviewResponseEntity> {
                resultAnswer.value =
                    ApiResponse.success(callback.onAllInterviewAnswersReceived(interviewAnswers))
                if (EspressoIdlingResource.espressoTestIdlingResource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
                return interviewAnswers
            }
        })
        return resultAnswer
    }

    fun insertInterviewAnswer(
        interviewAnswers: InterviewResponseEntity,
        callback: InsertInterviewAnswersCallback
    ) {
        EspressoIdlingResource.increment()
        mInterviewHelper.insertInterviewAnswer(
            interviewAnswers,
            object : InsertInterviewAnswersCallback {
                override fun onSuccess() {
                    callback.onSuccess()
                    EspressoIdlingResource.decrement()
                }

                override fun onFailure(messageId: Int) {
                    callback.onFailure(messageId)
                    EspressoIdlingResource.decrement()
                }
            })
    }
}