package org.d3ifcool.dissajobapplicant.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.databinding.FragmentProfileBinding
import org.d3ifcool.dissajobapplicant.ui.settings.SettingsActivity
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.AuthHelper
import org.d3ifcool.dissajobapplicant.vo.Status

class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var fragmentProfileBinding: FragmentProfileBinding

    private lateinit var applicantViewModel: ApplicantViewModel

    private var isAboutMeExpanded = false

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
            applicantViewModel = ViewModelProvider(this, factory)[ApplicantViewModel::class.java]
            applicantViewModel.getApplicantDetails(AuthHelper.currentUser?.uid.toString())
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

            //Settings
            fragmentProfileBinding.header.imgSettings.setOnClickListener(this)
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
            fragmentProfileBinding.aboutMeSection.tvAboutMe.maxLines = 2
            false
        } else {
            //This will expand the textview if it is of 2 lines
            fragmentProfileBinding.aboutMeSection.tvAboutMe.maxLines = Integer.MAX_VALUE
            true
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgSettings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
            }
        }
    }
}