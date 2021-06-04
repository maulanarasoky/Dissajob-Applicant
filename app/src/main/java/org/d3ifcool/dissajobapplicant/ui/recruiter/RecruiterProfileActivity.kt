package org.d3ifcool.dissajobapplicant.ui.recruiter

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityRecruiterProfileBinding
import org.d3ifcool.dissajobapplicant.ui.job.JobAdapter
import org.d3ifcool.dissajobapplicant.ui.job.JobDetailsActivity
import org.d3ifcool.dissajobapplicant.ui.job.JobViewModel
import org.d3ifcool.dissajobapplicant.ui.job.callback.OnJobClickListener
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.vo.Status

class RecruiterProfileActivity : AppCompatActivity(), OnJobClickListener,
    LoadRecruiterDataCallback {

    companion object {
        const val RECRUITER_ID = "recruiter_id"
    }

    private lateinit var activityRecruiterProfileBinding: ActivityRecruiterProfileBinding

    private lateinit var recruiterViewModel: RecruiterViewModel

    private lateinit var jobViewModel: JobViewModel

    private lateinit var jobAdapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRecruiterProfileBinding = ActivityRecruiterProfileBinding.inflate(layoutInflater)
        setContentView(activityRecruiterProfileBinding.root)

        showLoading(true)
        val recruiterId = intent.getStringExtra(RECRUITER_ID)
        val factory = ViewModelFactory.getInstance(this)
        jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
        recruiterViewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]
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

        jobAdapter = JobAdapter(this, this)
        jobViewModel.getJobsByRecruiter(recruiterId.toString()).observe(this) { jobs ->
            if (jobs != null) {
                when (jobs.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        if (jobs.data?.isNotEmpty() == true) {
                            jobAdapter.submitList(jobs.data)
                            jobAdapter.notifyDataSetChanged()
                        } else {
                            activityRecruiterProfileBinding.recruiterPostedJobSection.tvNoData.visibility =
                                View.VISIBLE
                        }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        with(activityRecruiterProfileBinding.recruiterPostedJobSection.rvJob) {
            layoutManager = LinearLayoutManager(this@RecruiterProfileActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@RecruiterProfileActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = jobAdapter
        }
    }

    private fun populateRecruiterData(recruiterProfile: RecruiterEntity) {
        activityRecruiterProfileBinding.toolbar.title = recruiterProfile.fullName.toString()
        setSupportActionBar(activityRecruiterProfileBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        activityRecruiterProfileBinding.recruiterProfileSection.tvRecruiterName.text =
            recruiterProfile.fullName.toString()
        activityRecruiterProfileBinding.recruiterProfileSection.tvEmail.text =
            recruiterProfile.email.toString()
        activityRecruiterProfileBinding.recruiterProfileSection.tvPhoneNumber.text =
            recruiterProfile.phoneNumber.toString()
        activityRecruiterProfileBinding.recruiterProfileSection.tvRecruiterAddress.text =
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
                .into(activityRecruiterProfileBinding.recruiterProfileSection.imgRecruiterPicture)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            activityRecruiterProfileBinding.recruiterPostedJobSection.progressBar.visibility =
                View.VISIBLE
        } else {
            activityRecruiterProfileBinding.recruiterPostedJobSection.progressBar.visibility =
                View.GONE
        }
    }

    override fun onLoadRecruiterData(recruiterId: String, callback: LoadRecruiterDataCallback) {
        recruiterViewModel.getRecruiterData(recruiterId).observe(this) { recruiterDetails ->
            if (recruiterDetails != null) {
                recruiterDetails.data?.let { callback.onRecruiterDataReceived(it) }
            }
        }
    }

    override fun onRecruiterDataReceived(recruiterData: RecruiterEntity) {
        TODO("Not yet implemented")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(jobId: String) {
        val intent = Intent(this, JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.EXTRA_ID, jobId)
        startActivity(intent)
    }
}