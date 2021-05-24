package org.d3ifcool.dissajobapplicant.ui.application

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.interview.InterviewEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityApplicationDetailsBinding
import org.d3ifcool.dissajobapplicant.ui.job.JobViewModel
import org.d3ifcool.dissajobapplicant.ui.question.InterviewViewModel
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterViewModel
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.vo.Status

class ApplicationDetailsActivity : AppCompatActivity() {

    companion object {
        const val APPLICATION_ID = "application_id"
        const val JOB_ID = "job_id"
        const val RECRUITER_ID = "recruiter_id"
    }

    private lateinit var activityApplicationDetailsBinding: ActivityApplicationDetailsBinding

    private lateinit var recruiterViewModel: RecruiterViewModel

    private lateinit var applicationViewModel: ApplicationViewModel

    private lateinit var jobViewModel: JobViewModel

    private lateinit var interviewViewModel: InterviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityApplicationDetailsBinding =
            ActivityApplicationDetailsBinding.inflate(layoutInflater)
        setContentView(activityApplicationDetailsBinding.root)

        val applicationId = intent.getStringExtra(APPLICATION_ID)
        val recruiterId = intent.getStringExtra(RECRUITER_ID)
        val jobId = intent.getStringExtra(JOB_ID)
        val factory = ViewModelFactory.getInstance(this)
        recruiterViewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]
        applicationViewModel = ViewModelProvider(this, factory)[ApplicationViewModel::class.java]
        jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
        interviewViewModel = ViewModelProvider(this, factory)[InterviewViewModel::class.java]
        recruiterViewModel.getRecruiterData(recruiterId.toString()).observe(this) { profile ->
            if (profile.data != null) {
                when (profile.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> populateRecruiterData(profile.data)
                    Status.ERROR -> {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        applicationViewModel.getApplicationById(applicationId.toString())
            .observe(this) { application ->
                if (application.data != null) {
                    when (application.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> {
                            val applicationData = ApplicationEntity(
                                application.data.id,
                                application.data.applicantId.toString(),
                                application.data.jobId.toString(),
                                application.data.applyDate.toString(),
                                application.data.status.toString(),
                                application.data.isMarked.toString().toBoolean(),
                            )

                            populateApplicationData(applicationData)
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        jobViewModel.getJobDetails(jobId.toString()).observe(this) { jobDetails ->
            if (jobDetails.data != null) {
                when (jobDetails.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        populateJobData(jobDetails.data)
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        interviewViewModel.getInterviewAnswers(jobId.toString()).observe(this) { interview ->
            if (interview.data != null) {
                when (interview.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        val interviewData = InterviewEntity(
                            interview.data[0]?.id.toString(),
                            interview.data[0]?.applicantId.toString(),
                            interview.data[0]?.jobId.toString(),
                            interview.data[0]?.firstAnswer.toString(),
                            interview.data[0]?.secondAnswer.toString(),
                            interview.data[0]?.thirdAnswer.toString()
                        )

                        populateInterviewData(interviewData)
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun populateRecruiterData(recruiterProfile: RecruiterEntity) {
        activityApplicationDetailsBinding.recruiterProfileSection.tvRecruiterName.text =
            recruiterProfile.fullName.toString()
        activityApplicationDetailsBinding.recruiterProfileSection.tvEmail.text =
            recruiterProfile.email.toString()
        activityApplicationDetailsBinding.recruiterProfileSection.tvPhoneNumber.text =
            recruiterProfile.phoneNumber.toString()
        activityApplicationDetailsBinding.recruiterProfileSection.tvRecruiterAddress.text =
            recruiterProfile.address.toString()

        if (recruiterProfile.imagePath != "-") {
            val storageRef = Firebase.storage.reference
            val circularProgressDrawable = CircularProgressDrawable(this)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide.with(this)
                .load(storageRef.child("recruiter/profile/images/${recruiterProfile.imagePath}"))
                .transform(RoundedCorners(20))
                .apply(RequestOptions.placeholderOf(circularProgressDrawable))
                .error(R.drawable.ic_profile_gray_24dp)
                .into(activityApplicationDetailsBinding.recruiterProfileSection.imgRecruiterPicture)
        }
    }

    private fun populateApplicationData(applicationData: ApplicationEntity) {
        activityApplicationDetailsBinding.applicationDetailsSection.tvApplicationStatus.text =
            applicationData.status.toString()
    }

    private fun populateJobData(jobData: JobDetailsEntity) {
        activityApplicationDetailsBinding.applicationDetailsSection.tvJobTitle.text =
            jobData.title.toString()
        activityApplicationDetailsBinding.applicationDetailsSection.tvJobDescription.text =
            jobData.description.toString()
    }

    private fun populateInterviewData(interviewData: InterviewEntity) {
        activityApplicationDetailsBinding.additionalInformationSection.etFirstQuestion.setText(
            interviewData.firstAnswer.toString()
        )
        activityApplicationDetailsBinding.additionalInformationSection.etSecondQuestion.setText(
            interviewData.secondAnswer.toString()
        )
        activityApplicationDetailsBinding.additionalInformationSection.etThirdQuestion.setText(
            interviewData.thirdAnswer.toString()
        )
    }
}