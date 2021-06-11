package org.d3ifcool.dissajobapplicant.data.source.repository.interview

import androidx.lifecycle.LiveData
import org.d3ifcool.dissajobapplicant.data.NetworkBoundResource
import org.d3ifcool.dissajobapplicant.data.source.local.entity.interview.InterviewEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalInterviewSource
import org.d3ifcool.dissajobapplicant.data.source.remote.ApiResponse
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.interview.InterviewResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteInterviewSource
import org.d3ifcool.dissajobapplicant.ui.question.InsertInterviewAnswersCallback
import org.d3ifcool.dissajobapplicant.ui.question.LoadInterviewAnswersCallback
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.NetworkStateCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class InterviewRepository private constructor(
    private val remoteInterviewSource: RemoteInterviewSource,
    private val localInterviewSource: LocalInterviewSource,
    private val appExecutors: AppExecutors,
    private val networkCallback: NetworkStateCallback
) :
    InterviewDataSource {

    companion object {
        @Volatile
        private var instance: InterviewRepository? = null

        fun getInstance(
            remoteInterviewSource: RemoteInterviewSource,
            localInterviewSource: LocalInterviewSource,
            appExecutors: AppExecutors,
            networkCallback: NetworkStateCallback
        ): InterviewRepository =
            instance ?: synchronized(this) {
                instance ?: InterviewRepository(
                    remoteInterviewSource,
                    localInterviewSource,
                    appExecutors,
                    networkCallback
                )
            }
    }

    override fun getInterviewAnswers(applicationId: String): LiveData<Resource<InterviewEntity>> {
        return object :
            NetworkBoundResource<InterviewEntity, InterviewResponseEntity>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<InterviewEntity> =
                localInterviewSource.getInterviewAnswers(applicationId)

            override fun shouldFetch(data: InterviewEntity?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()

            public override fun createCall(): LiveData<ApiResponse<InterviewResponseEntity>> =
                remoteInterviewSource.getInterviewAnswers(
                    applicationId,
                    object : LoadInterviewAnswersCallback {
                        override fun onAllInterviewAnswersReceived(interviewAnswers: InterviewResponseEntity): InterviewResponseEntity {
                            return interviewAnswers
                        }
                    })

            public override fun saveCallResult(data: InterviewResponseEntity) {
                val interview = InterviewEntity(
                    data.id,
                    data.applicationId,
                    data.applicantId,
                    data.firstAnswer,
                    data.secondAnswer,
                    data.thirdAnswer
                )
                localInterviewSource.insertInterviewAnswers(interview)
            }
        }.asLiveData()
    }

    override fun insertInterviewAnswers(
        interviewAnswer: InterviewResponseEntity,
        callback: InsertInterviewAnswersCallback
    ) = appExecutors.diskIO()
        .execute { remoteInterviewSource.insertInterviewAnswer(interviewAnswer, callback) }


}