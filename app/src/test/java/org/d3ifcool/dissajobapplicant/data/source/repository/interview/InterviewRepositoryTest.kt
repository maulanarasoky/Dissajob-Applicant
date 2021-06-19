package org.d3ifcool.dissajobapplicant.data.source.repository.interview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.local.entity.interview.InterviewEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalInterviewSource
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteInterviewSource
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicationDummy
import org.d3ifcool.dissajobapplicant.utils.dummy.InterviewDummy
import org.d3ifcool.dissajobapplicant.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class InterviewRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteInterviewSource::class.java)
    private val local = mock(LocalInterviewSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val interviewRepository = FakeInterviewRepository(remote, local, appExecutors)

    private val applicationId = ApplicationDummy.generateApplicationsData()[0].id
    private val interviewData = InterviewDummy.generateInterviewData()

    @Test
    fun getInterviewDataTest() {
        val interviewLiveData = MutableLiveData<InterviewEntity>()
        `when`(local.getInterviewAnswers(applicationId)).thenReturn(interviewLiveData)
        interviewRepository.getInterviewAnswers(applicationId)

        val interviewEntity = Resource.success(InterviewDummy.generateInterviewData())
        verify(local).getInterviewAnswers(applicationId)
        assertNotNull(interviewEntity.data)
        assertEquals(interviewData.id, interviewEntity.data?.id)
        assertEquals(interviewData.applicationId, interviewEntity.data?.applicationId)
        assertEquals(interviewData.applicantId, interviewEntity.data?.applicantId)
        assertEquals(interviewData.firstAnswer, interviewEntity.data?.firstAnswer)
        assertEquals(interviewData.secondAnswer, interviewEntity.data?.secondAnswer)
        assertEquals(interviewData.thirdAnswer, interviewEntity.data?.thirdAnswer)
    }
}