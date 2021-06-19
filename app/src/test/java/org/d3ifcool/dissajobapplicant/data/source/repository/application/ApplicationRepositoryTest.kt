package org.d3ifcool.dissajobapplicant.data.source.repository.application

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase
import org.d3ifcool.dissajobapplicant.data.source.local.source.LocalApplicationSource
import org.d3ifcool.dissajobapplicant.data.source.remote.source.RemoteApplicationSource
import org.d3ifcool.dissajobapplicant.utils.AppExecutors
import org.d3ifcool.dissajobapplicant.utils.dummy.ApplicationDummy
import org.d3ifcool.dissajobapplicant.utils.dummy.RecruiterDummy
import org.junit.Rule
import org.mockito.Mockito.mock

class ApplicationRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteApplicationSource::class.java)
    private val local = mock(LocalApplicationSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val applicationRepository = FakeApplicationRepository(remote, local, appExecutors)

    private val applicationResponse = ApplicationDummy.generateApplicationsData()
    private val acceptedApplicationResponse = ApplicationDummy.generateAcceptedApplicationsData()
    private val rejectedApplicationResponse = ApplicationDummy.generateRejectedApplicationsData()
    private val markedApplicationResponse = ApplicationDummy.generateMarkedApplicationsData()
    private val recruiterData = RecruiterDummy.generateRecruiterDetails()
}