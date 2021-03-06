package org.d3ifcool.dissajobapplicant.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3ifcool.dissajobapplicant.data.source.repository.applicant.ApplicantRepository
import org.d3ifcool.dissajobapplicant.data.source.repository.application.ApplicationRepository
import org.d3ifcool.dissajobapplicant.data.source.repository.education.EducationRepository
import org.d3ifcool.dissajobapplicant.data.source.repository.experience.ExperienceRepository
import org.d3ifcool.dissajobapplicant.data.source.repository.history.SearchHistoryRepository
import org.d3ifcool.dissajobapplicant.data.source.repository.interview.InterviewRepository
import org.d3ifcool.dissajobapplicant.data.source.repository.job.JobRepository
import org.d3ifcool.dissajobapplicant.data.source.repository.media.MediaRepository
import org.d3ifcool.dissajobapplicant.data.source.repository.notification.NotificationRepository
import org.d3ifcool.dissajobapplicant.data.source.repository.recruiter.RecruiterRepository
import org.d3ifcool.dissajobapplicant.ui.application.ApplicationViewModel
import org.d3ifcool.dissajobapplicant.ui.di.Injection
import org.d3ifcool.dissajobapplicant.ui.education.EducationViewModel
import org.d3ifcool.dissajobapplicant.ui.experience.ExperienceViewModel
import org.d3ifcool.dissajobapplicant.ui.job.JobViewModel
import org.d3ifcool.dissajobapplicant.ui.media.MediaViewModel
import org.d3ifcool.dissajobapplicant.ui.notification.NotificationViewModel
import org.d3ifcool.dissajobapplicant.ui.profile.ApplicantViewModel
import org.d3ifcool.dissajobapplicant.ui.question.InterviewViewModel
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterViewModel
import org.d3ifcool.dissajobapplicant.ui.search.SearchViewModel
import org.d3ifcool.dissajobapplicant.ui.signin.SignInViewModel
import org.d3ifcool.dissajobapplicant.ui.signup.SignUpViewModel

class ViewModelFactory private constructor(
    private val jobRepository: JobRepository,
    private val applicationRepository: ApplicationRepository,
    private val interviewRepository: InterviewRepository,
    private val applicantRepository: ApplicantRepository,
    private val recruiterRepository: RecruiterRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
    private val mediaRepository: MediaRepository,
    private val experienceRepository: ExperienceRepository,
    private val educationRepository: EducationRepository,
    private val notificationRepository: NotificationRepository
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                Injection.provideJobRepository(context),
                Injection.provideApplicationRepository(context),
                Injection.provideInterviewRepository(context),
                Injection.provideApplicantRepository(context),
                Injection.provideRecruiterRepository(context),
                Injection.provideSearchHistoryRepository(context),
                Injection.provideMediaRepository(context),
                Injection.provideExperienceRepository(context),
                Injection.provideEducationRepository(context),
                Injection.provideNotificationRepository(context)
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
            modelClass.isAssignableFrom(InterviewViewModel::class.java) -> {
                InterviewViewModel(interviewRepository) as T
            }
            modelClass.isAssignableFrom(ApplicantViewModel::class.java) -> {
                ApplicantViewModel(applicantRepository) as T
            }
            modelClass.isAssignableFrom(RecruiterViewModel::class.java) -> {
                RecruiterViewModel(recruiterRepository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(searchHistoryRepository) as T
            }
            modelClass.isAssignableFrom(MediaViewModel::class.java) -> {
                MediaViewModel(mediaRepository) as T
            }
            modelClass.isAssignableFrom(ExperienceViewModel::class.java) -> {
                ExperienceViewModel(experienceRepository) as T
            }
            modelClass.isAssignableFrom(EducationViewModel::class.java) -> {
                EducationViewModel(educationRepository) as T
            }
            modelClass.isAssignableFrom(NotificationViewModel::class.java) -> {
                NotificationViewModel(notificationRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}