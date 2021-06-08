package org.d3ifcool.dissajobapplicant.data.source.repository.interview

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
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

    override fun getInterviewAnswers(applicationId: String): LiveData<Resource<PagedList<InterviewEntity>>> {
        return object :
            NetworkBoundResource<PagedList<InterviewEntity>, List<InterviewResponseEntity>>(
                appExecutors
            ) {
            public override fun loadFromDB(): LiveData<PagedList<InterviewEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()
                return LivePagedListBuilder(
                    localInterviewSource.getInterviewAnswers(applicationId),
                    config
                ).build()
            }

            override fun shouldFetch(data: PagedList<InterviewEntity>?): Boolean =
                networkCallback.hasConnectivity() && loadFromDB() != createCall()

            public override fun createCall(): LiveData<ApiResponse<List<InterviewResponseEntity>>> =
                remoteInterviewSource.getInterviewAnswers(
                    applicationId,
                    object : LoadInterviewAnswersCallback {
                        override fun onAllInterviewAnswersReceived(interviewAnswers: List<InterviewResponseEntity>): List<InterviewResponseEntity> {
                            return interviewAnswers
                        }
                    })

            public override fun saveCallResult(data: List<InterviewResponseEntity>) {
                val interviewAnswerList = ArrayList<InterviewEntity>()
                for (response in data) {
                    val interviewAnswer = InterviewEntity(
                        response.id,
                        response.applicationId,
                        response.applicantId,
                        response.firstAnswer,
                        response.secondAnswer,
                        response.thirdAnswer
                    )
                    interviewAnswerList.add(interviewAnswer)
                }

                localInterviewSource.insertInterviewAnswers(interviewAnswerList)
            }
        }.asLiveData()
    }

    override fun insertInterviewAnswers(
        interviewAnswer: InterviewResponseEntity,
        callback: InsertInterviewAnswersCallback
    ) = appExecutors.diskIO()
        .execute { remoteInterviewSource.insertInterviewAnswer(interviewAnswer, callback) }


}