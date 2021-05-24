package org.d3ifcool.dissajobapplicant.ui.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivitySearchResultBinding
import org.d3ifcool.dissajobapplicant.ui.job.JobAdapter
import org.d3ifcool.dissajobapplicant.ui.job.JobViewModel
import org.d3ifcool.dissajobapplicant.ui.job.callback.ItemClickListener
import org.d3ifcool.dissajobapplicant.ui.recruiter.LoadRecruiterDataCallback
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.vo.Status

class SearchResultActivity : AppCompatActivity(), ItemClickListener, LoadRecruiterDataCallback {

    companion object {
        const val SEARCH_TEXT = "search_text"
    }

    private lateinit var activitySearchResultBinding: ActivitySearchResultBinding

    private lateinit var jobViewModel: JobViewModel

    private lateinit var jobAdapter: JobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearchResultBinding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(activitySearchResultBinding.root)

        val searchText = intent.getStringExtra(SEARCH_TEXT)
        val factory = ViewModelFactory.getInstance(this)
        jobAdapter = JobAdapter(this, this)
        jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
        jobViewModel.searchJob(searchText.toString()).observe(this) { jobs ->
            if (jobs != null) {
                when (jobs.status) {
                    Status.LOADING -> showLoading(true)
                    Status.SUCCESS -> {
                        showLoading(false)
                        if (jobs.data?.isNotEmpty() == true) {
                            jobAdapter.submitList(jobs.data)
                            jobAdapter.notifyDataSetChanged()
                        } else {
                            activitySearchResultBinding.tvNoData.visibility = View.VISIBLE
                        }
                    }
                    Status.ERROR -> {
                        showLoading(false)
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        with(activitySearchResultBinding.rvJob) {
            layoutManager = LinearLayoutManager(this@SearchResultActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@SearchResultActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = jobAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            activitySearchResultBinding.progressBar.visibility = View.VISIBLE
        } else {
            activitySearchResultBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onItemClicked(jobId: String) {
        TODO("Not yet implemented")
    }

    override fun onLoadRecruiterData(recruiterId: String, callback: LoadRecruiterDataCallback) {
        TODO("Not yet implemented")
    }

    override fun onRecruiterDataReceived(recruiterData: RecruiterEntity) {
        TODO("Not yet implemented")
    }
}