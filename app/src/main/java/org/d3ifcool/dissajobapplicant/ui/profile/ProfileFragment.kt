package org.d3ifcool.dissajobapplicant.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.experience.ExperienceEntity
import org.d3ifcool.dissajobapplicant.databinding.FragmentProfileBinding
import org.d3ifcool.dissajobapplicant.ui.application.ApplicationActivity
import org.d3ifcool.dissajobapplicant.ui.education.AddEditEducationActivity
import org.d3ifcool.dissajobapplicant.ui.education.EducationAdapter
import org.d3ifcool.dissajobapplicant.ui.education.EducationViewModel
import org.d3ifcool.dissajobapplicant.ui.education.callback.OnEducationItemClickListener
import org.d3ifcool.dissajobapplicant.ui.experience.AddEditExperienceActivity
import org.d3ifcool.dissajobapplicant.ui.experience.ExperienceAdapter
import org.d3ifcool.dissajobapplicant.ui.experience.ExperienceViewModel
import org.d3ifcool.dissajobapplicant.ui.experience.callback.OnExperienceItemClickListener
import org.d3ifcool.dissajobapplicant.ui.job.savedjob.SavedJobActivity
import org.d3ifcool.dissajobapplicant.ui.media.MediaActivity
import org.d3ifcool.dissajobapplicant.ui.settings.SettingsActivity
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.vo.Status

class ProfileFragment : Fragment(), View.OnClickListener, OnEducationItemClickListener,
    OnExperienceItemClickListener {

    private lateinit var fragmentProfileBinding: FragmentProfileBinding

    private lateinit var applicantViewModel: ApplicantViewModel

    private lateinit var experienceViewModel: ExperienceViewModel

    private lateinit var educationViewModel: EducationViewModel

    private lateinit var experienceAdapter: ExperienceAdapter

    private lateinit var educationAdapter: EducationAdapter

    private var isAboutMeExpanded = false

    private val applicantId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return fragmentProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireContext())
            experienceViewModel = ViewModelProvider(this, factory)[ExperienceViewModel::class.java]
            educationViewModel = ViewModelProvider(this, factory)[EducationViewModel::class.java]
            applicantViewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]
            applicantViewModel.getApplicantDetails(applicantId)
                .observe(viewLifecycleOwner) { profile ->
                    if (profile.data != null) {
                        when (profile.status) {
                            Status.LOADING -> {
                            }
                            Status.SUCCESS -> populateData(profile.data)
                            Status.ERROR -> {
                                Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            experienceAdapter = ExperienceAdapter(this)
            experienceViewModel.getApplicantExperiences(applicantId)
                .observe(viewLifecycleOwner) { experiences ->
                    if (experiences != null) {
                        when (experiences.status) {
                            Status.LOADING -> fragmentProfileBinding.workExperienceSection.progressBar.visibility =
                                View.VISIBLE
                            Status.SUCCESS -> {
                                fragmentProfileBinding.workExperienceSection.progressBar.visibility =
                                    View.GONE
                                if (experiences.data?.isNotEmpty() == true) {
                                    experienceAdapter.submitList(experiences.data)
                                    experienceAdapter.notifyDataSetChanged()
                                    fragmentProfileBinding.workExperienceSection.tvNoData.visibility =
                                        View.GONE
                                } else {
                                    fragmentProfileBinding.workExperienceSection.tvNoData.visibility =
                                        View.VISIBLE
                                }
                            }
                            Status.ERROR -> {
                                fragmentProfileBinding.workExperienceSection.progressBar.visibility =
                                    View.GONE
                                fragmentProfileBinding.workExperienceSection.tvNoData.visibility =
                                    View.VISIBLE
                                Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            with(fragmentProfileBinding.workExperienceSection.rvWorkExperience) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
//                addItemDecoration(
//                    DividerItemDecoration(
//                        requireContext(),
//                        DividerItemDecoration.VERTICAL
//                    )
//                )
                adapter = experienceAdapter
            }

            educationAdapter = EducationAdapter(this)
            educationViewModel.getApplicantEducations(applicantId)
                .observe(viewLifecycleOwner) { educations ->
                    if (educations != null) {
                        when (educations.status) {
                            Status.LOADING -> fragmentProfileBinding.educationalBackgroundSection.progressBar.visibility =
                                View.VISIBLE
                            Status.SUCCESS -> {
                                fragmentProfileBinding.educationalBackgroundSection.progressBar.visibility =
                                    View.GONE
                                if (educations.data?.isNotEmpty() == true) {
                                    educationAdapter.submitList(educations.data)
                                    educationAdapter.notifyDataSetChanged()
                                    fragmentProfileBinding.educationalBackgroundSection.tvNoData.visibility =
                                        View.GONE
                                } else {
                                    fragmentProfileBinding.educationalBackgroundSection.tvNoData.visibility =
                                        View.VISIBLE
                                }
                            }
                            Status.ERROR -> {
                                fragmentProfileBinding.educationalBackgroundSection.progressBar.visibility =
                                    View.GONE
                                fragmentProfileBinding.educationalBackgroundSection.tvNoData.visibility =
                                    View.VISIBLE
                                Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            with(fragmentProfileBinding.educationalBackgroundSection.rvEducationalBackground) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
//                addItemDecoration(
//                    DividerItemDecoration(
//                        requireContext(),
//                        DividerItemDecoration.VERTICAL
//                    )
//                )
                adapter = educationAdapter
            }

            //Settings
            fragmentProfileBinding.header.imgSettings.setOnClickListener(this)

            //Upload media section
            fragmentProfileBinding.menuSection.mediaSection.root.setOnClickListener(this)

            //My applications section
            fragmentProfileBinding.menuSection.applicationSection.root.setOnClickListener(this)

            //Saved job section
            fragmentProfileBinding.menuSection.savedJobSection.root.setOnClickListener(this)

            //Experience section
            fragmentProfileBinding.workExperienceSection.imgAddExperience.setOnClickListener(
                this
            )

            //Education section
            fragmentProfileBinding.educationalBackgroundSection.imgAddEducation.setOnClickListener(
                this
            )
        }
    }

    private fun populateData(applicantProfile: ApplicantEntity) {
        fragmentProfileBinding.profileSection.tvProfileName.text =
            applicantProfile.fullName.toString()
        fragmentProfileBinding.profileSection.tvEmail.text = applicantProfile.email.toString()
        fragmentProfileBinding.profileSection.tvPhoneNumber.text =
            applicantProfile.phoneNumber.toString()
        fragmentProfileBinding.aboutMeSection.tvAboutMe.text = applicantProfile.aboutMe.toString()
        fragmentProfileBinding.aboutMeSection.tvAboutMe.setOnClickListener(tvAboutMeClickListener)

        if (applicantProfile.imagePath != "-") {
            val storageRef = Firebase.storage.reference
            val circularProgressDrawable = CircularProgressDrawable(requireContext())
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide.with(requireContext())
                .load(storageRef.child("applicant/profile/images/${applicantProfile.imagePath}"))
                .transform(RoundedCorners(20))
                .apply(RequestOptions.placeholderOf(circularProgressDrawable))
                .error(R.drawable.ic_profile_gray_24dp)
                .into(fragmentProfileBinding.header.imgProfilePic)
        }
    }

    private val tvAboutMeClickListener = View.OnClickListener {
        isAboutMeExpanded = if (isAboutMeExpanded) {
            //This will shrink textview to 2 lines if it is expanded.
            fragmentProfileBinding.aboutMeSection.tvAboutMe.maxLines = 3
            false
        } else {
            //This will expand the textview if it is of 2 lines
            fragmentProfileBinding.aboutMeSection.tvAboutMe.maxLines = Integer.MAX_VALUE
            true
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgSettings -> startActivity(Intent(activity, SettingsActivity::class.java))
            R.id.mediaSection -> startActivity(Intent(activity, MediaActivity::class.java))
            R.id.applicationSection -> startActivity(
                Intent(
                    activity,
                    ApplicationActivity::class.java
                )
            )
            R.id.savedJobSection -> startActivity(Intent(activity, SavedJobActivity::class.java))
            R.id.imgAddExperience -> startActivity(
                Intent(
                    activity,
                    AddEditExperienceActivity::class.java
                )
            )
            R.id.imgAddEducation -> startActivity(
                Intent(
                    activity,
                    AddEditEducationActivity::class.java
                )
            )
        }
    }

    override fun onClickItem(educationData: EducationEntity) {
        val intent = Intent(activity, AddEditEducationActivity::class.java)
        intent.putExtra(AddEditEducationActivity.EDUCATION_DATA, educationData)
        startActivity(intent)
    }

    override fun onClickItem(experienceData: ExperienceEntity) {
        val intent = Intent(activity, AddEditExperienceActivity::class.java)
        intent.putExtra(AddEditExperienceActivity.EXPERIENCE_DATA, experienceData)
        startActivity(intent)
    }
}