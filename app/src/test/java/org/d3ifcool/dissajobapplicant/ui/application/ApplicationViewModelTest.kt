package org.d3ifcool.dissajobapplicant.ui.application

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.application.ApplicationRepository
import org.d3ifcool.dissajobapplicant.utils.dummy.RecruiterDummy
import org.d3ifcool.dissajobapplicant.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ApplicationViewModelTest {
    private lateinit var viewModel: ApplicationViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var applicationRepository: ApplicationRepository

    @Mock
    private lateinit var applicationObserver: Observer<Resource<PagedList<ApplicationEntity>>>

    @Mock
    private lateinit var pagedList: PagedList<ApplicationEntity>

    private val recruiterData = RecruiterDummy.generateRecruiterDetails()

    @Before
    fun setUp() {
        viewModel = ApplicationViewModel(applicationRepository)
    }

    @Test
    fun getApplicationsDataTest() {
        val dummyApplications = Resource.success(pagedList)
        `when`(dummyApplications.data?.size).thenReturn(1)
        val applications = MutableLiveData<Resource<PagedList<ApplicationEntity>>>()
        applications.value = dummyApplications

        `when`(applicationRepository.getApplications(recruiterData.id)).thenReturn(applications)
        val applicationEntities = viewModel.getApplications(recruiterData.id).value?.data
        verify(applicationRepository).getApplications(recruiterData.id)
        assertNotNull(applicationEntities)
        assertEquals(1, applicationEntities?.size)

        viewModel.getApplications(recruiterData.id).observeForever(applicationObserver)
        verify(applicationObserver).onChanged(dummyApplications)
    }

    @Test
    fun getAcceptedApplicationsDataTest() {
        val dummyAcceptedApplications = Resource.success(pagedList)
        `when`(dummyAcceptedApplications.data?.size).thenReturn(1)
        val acceptedApplications = MutableLiveData<Resource<PagedList<ApplicationEntity>>>()
        acceptedApplications.value = dummyAcceptedApplications

        `when`(applicationRepository.getAcceptedApplications(recruiterData.id)).thenReturn(
            acceptedApplications
        )
        val acceptedApplicationEntities =
            viewModel.getAcceptedApplications(recruiterData.id).value?.data
        verify(applicationRepository).getAcceptedApplications(recruiterData.id)
        assertNotNull(acceptedApplicationEntities)
        assertEquals(1, acceptedApplicationEntities?.size)

        viewModel.getAcceptedApplications(recruiterData.id).observeForever(applicationObserver)
        verify(applicationObserver).onChanged(dummyAcceptedApplications)
    }

    @Test
    fun getRejectedApplicationsDataTest() {
        val dummyRejectedApplications = Resource.success(pagedList)
        `when`(dummyRejectedApplications.data?.size).thenReturn(1)
        val rejectedApplications = MutableLiveData<Resource<PagedList<ApplicationEntity>>>()
        rejectedApplications.value = dummyRejectedApplications

        `when`(applicationRepository.getRejectedApplications(recruiterData.id)).thenReturn(
            rejectedApplications
        )
        val rejectedApplicationEntities =
            viewModel.getRejectedApplications(recruiterData.id).value?.data
        verify(applicationRepository).getRejectedApplications(recruiterData.id)
        assertNotNull(rejectedApplicationEntities)
        assertEquals(1, rejectedApplicationEntities?.size)

        viewModel.getRejectedApplications(recruiterData.id).observeForever(applicationObserver)
        verify(applicationObserver).onChanged(dummyRejectedApplications)
    }

    @Test
    fun getMarkedApplicationsDataTest() {
        val dummyMarkedApplications = Resource.success(pagedList)
        `when`(dummyMarkedApplications.data?.size).thenReturn(1)
        val markedApplications = MutableLiveData<Resource<PagedList<ApplicationEntity>>>()
        markedApplications.value = dummyMarkedApplications

        `when`(applicationRepository.getMarkedApplications(recruiterData.id)).thenReturn(
            markedApplications
        )
        val markedApplicationEntities =
            viewModel.getMarkedApplications(recruiterData.id).value?.data
        verify(applicationRepository).getMarkedApplications(recruiterData.id)
        assertNotNull(markedApplicationEntities)
        assertEquals(1, markedApplicationEntities?.size)

        viewModel.getMarkedApplications(recruiterData.id).observeForever(applicationObserver)
        verify(applicationObserver).onChanged(dummyMarkedApplications)
    }
}