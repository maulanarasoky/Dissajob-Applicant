package org.d3ifcool.dissajobapplicant.data.source.repository.recruiter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalRecruiterSource
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteRecruiterSource
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.dummy.RecruiterDummy
import org.d3ifcool.dissajobapplicant.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class RecruiterRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteRecruiterSource::class.java)
    private val local = mock(LocalRecruiterSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val recruiterRepository = FakeRecruiterRepository(remote, local, appExecutors)

    private val recruiterData = RecruiterDummy.generateRecruiterDetails()

    @Test
    fun getRecruiterDataTest() {
        val recruiterLiveData = MutableLiveData<RecruiterEntity>()
        `when`(local.getRecruiterData(recruiterData.id)).thenReturn(recruiterLiveData)
        recruiterRepository.getRecruiterData(recruiterData.id)

        val recruiterEntity = Resource.success(RecruiterDummy.generateRecruiterDetails())
        verify(local).getRecruiterData(recruiterData.id)
        assertNotNull(recruiterEntity.data)
        assertEquals(recruiterData.id, recruiterEntity.data?.id)
        assertEquals(recruiterData.firstName, recruiterEntity.data?.firstName)
        assertEquals(recruiterData.lastName, recruiterEntity.data?.lastName)
        assertEquals(recruiterData.fullName, recruiterEntity.data?.fullName)
        assertEquals(recruiterData.email, recruiterEntity.data?.email)
        assertEquals(recruiterData.phoneNumber, recruiterEntity.data?.phoneNumber)
        assertEquals(recruiterData.address, recruiterEntity.data?.address)
        assertEquals(recruiterData.aboutMe, recruiterEntity.data?.aboutMe)
        assertEquals(recruiterData.imagePath, recruiterEntity.data?.imagePath)
    }
}