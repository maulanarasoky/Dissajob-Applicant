package org.d3ifcool.dissajobapplicant.ui.job.savedjob

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivitySavedJobBinding
import org.d3ifcool.dissajobapplicant.ui.job.JobDetailsActivity
import org.d3ifcool.dissajobapplicant.ui.job.JobViewModel
import org.d3ifcool.dissajobapplicant.ui.job.callback.LoadJobByIdCallback
import org.d3ifcool.dissajobapplicant.ui.job.callback.OnJobClickListener
import org.d3ifcool.dissajobapplicant.ui.recruiter.LoadRecruiterDataCallback
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterViewModel
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.vo.Status

class SavedJobActivity : AppCompatActivity(), OnJobClickListener, LoadJobByIdCallback,
    LoadRecruiterDataCallback {

    private lateinit var activitySavedJobBinding: ActivitySavedJobBinding

    private lateinit var jobAdapter: SavedJobAdapter

    private lateinit var jobViewModel: JobViewModel

    private lateinit var recruiterViewModel: RecruiterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySavedJobBinding = ActivitySavedJobBinding.inflate(layoutInflater)
        setContentView(activitySavedJobBinding.root)

        activitySavedJobBinding.toolbar.title =
            resources.getString(R.string.txt_saved_job)
        setSupportActionBar(activitySavedJobBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
        recruiterViewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]
        jobAdapter = SavedJobAdapter(this, this, this)
        jobViewModel.getSavedJobs().observe(this) { jobs ->
            if (jobs.data != null) {
                when (jobs.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        if (jobs.data.isNotEmpty()) {
                            jobAdapter.submitList(jobs.data)
                            jobAdapter.notifyDataSetChanged()
                        } else {
                            activitySavedJobBinding.tvNoData.visibility = View.VISIBLE
                        }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        with(activitySavedJobBinding.rvJob) {
            layoutManager = LinearLayoutManager(this@SavedJobActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@SavedJobActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = jobAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            activitySavedJobBinding.progressBar.visibility = View.VISIBLE
        } else {
            activitySavedJobBinding.progressBar.visibility = View.GONE
        }
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

    override fun onLoadJobData(jobId: String, callback: LoadJobByIdCallback) {
        jobViewModel.getJobById(jobId).observe(this) { job ->
            if (job.data != null) {
                callback.onJobReceived(job.data)
            }
        }
    }

    override fun onJobReceived(jobEntity: JobEntity) {
    }

    override fun onLoadRecruiterData(recruiterId: String, callback: LoadRecruiterDataCallback) {
        recruiterViewModel.getRecruiterData(recruiterId).observe(this) { recruiterDetails ->
            if (recruiterDetails != null) {
                recruiterDetails.data?.let { callback.onRecruiterDataReceived(it) }
            }
        }
    }

    override fun onRecruiterDataReceived(recruiterData: RecruiterEntity) {
    }

    override fun onItemClick(jobId: String) {
        val intent = Intent(this, JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.EXTRA_ID, jobId)
        startActivity(intent)
    }
}