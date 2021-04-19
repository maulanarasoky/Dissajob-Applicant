package org.d3ifcool.dissajobapplicant.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.dissajobapplicant.data.source.repository.applicant.ApplicantRepository
import org.d3ifcool.dissajobapplicant.data.source.repository.application.ApplicationRepository
import org.d3ifcool.dissajobapplicant.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobapplicant.data.source.repository.recruiter.RecruiterRepository
import org.d3ifcool.dissajobapplicant.ui.applicant.ApplicantViewModel
import org.d3ifcool.dissajobapplicant.ui.application.ApplicationViewModel
import org.d3ifcool.dissajobapplicant.ui.di.Injection
import org.d3ifcool.dissajobapplicant.ui.job.JobViewModel
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterViewModel
import org.d3ifcool.dissajobapplicant.ui.signin.SignInViewModel
import org.d3ifcool.dissajobapplicant.ui.signup.SignUpViewModel

class ViewModelFactory private constructor(
    private val jobRepository: JobRepository,
    private val applicationRepository: ApplicationRepository,
    private val applicantRepository: ApplicantRepository,
    private val recruiterRepository: RecruiterRepository
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                Injection.provideJobRepository(context),
                Injection.provideApplicationRepository(context),
                Injection.provideApplicantRepository(context),
                Injection.provideRecruiterRepository(context)
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(JobViewModel::class.java) -> {
                JobViewModel(jobRepository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(applicantRepository) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(applicantRepository) as T
            }
            modelClass.isAssignableFrom(ApplicationViewModel::class.java) -> {
                ApplicationViewModel(applicationRepository) as T
            }
            modelClass.isAssignableFrom(ApplicantViewModel::class.java) -> {
                ApplicantViewModel(applicantRepository) as T
            }
            modelClass.isAssignableFrom(RecruiterViewModel::class.java) -> {
                RecruiterViewModel(recruiterRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}