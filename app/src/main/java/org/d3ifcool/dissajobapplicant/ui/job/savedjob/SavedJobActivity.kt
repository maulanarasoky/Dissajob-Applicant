package org.d3ifcool.dissajobapplicant.ui.job.savedjob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobapplicant.databinding.ActivitySavedJobBinding
import org.d3ifcool.dissajobapplicant.ui.job.JobDetailsActivity
import org.d3ifcool.dissajobapplicant.ui.job.JobViewModel
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.vo.Status

class SavedJobActivity : AppCompatActivity() {

    private lateinit var activitySavedJobBinding: ActivitySavedJobBinding

    private lateinit var jobAdapter: SavedJobAdapter

    private lateinit var viewModel: JobViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySavedJobBinding = ActivitySavedJobBinding.inflate(layoutInflater)
        setContentView(activitySavedJobBinding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
        jobAdapter = SavedJobAdapter(this, this)
        viewModel.getSavedJobs().observe(this) { jobs ->
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

    override fun onItemClicked(jobId: String) {
        val intent = Intent(this, JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.EXTRA_ID, jobId)
        startActivity(intent)
    }
}