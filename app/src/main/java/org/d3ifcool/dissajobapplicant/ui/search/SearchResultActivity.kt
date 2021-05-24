package org.d3ifcool.dissajobapplicant.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.history.SearchHistoryResponseEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivitySearchResultBinding
import org.d3ifcool.dissajobapplicant.ui.job.JobAdapter
import org.d3ifcool.dissajobapplicant.ui.job.JobDetailsActivity
import org.d3ifcool.dissajobapplicant.ui.job.JobViewModel
import org.d3ifcool.dissajobapplicant.ui.job.callback.ItemClickListener
import org.d3ifcool.dissajobapplicant.ui.recruiter.LoadRecruiterDataCallback
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterViewModel
import org.d3ifcool.dissajobapplicant.ui.search.callback.AddSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.DateUtils
import org.d3ifcool.dissajobapplicant.utils.database.AuthHelper
import org.d3ifcool.dissajobapplicant.vo.Status

class SearchResultActivity : AppCompatActivity(), ItemClickListener, LoadRecruiterDataCallback,
    AddSearchHistoryCallback {

    companion object {
        const val SEARCH_TEXT = "search_text"
    }

    private lateinit var activitySearchResultBinding: ActivitySearchResultBinding

    private lateinit var recruiterViewModel: RecruiterViewModel

    private lateinit var searchViewModel: SearchViewModel

    private lateinit var jobViewModel: JobViewModel

    private lateinit var jobAdapter: JobAdapter

    private lateinit var searchText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearchResultBinding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(activitySearchResultBinding.root)

        searchText = intent.getStringExtra(SEARCH_TEXT).toString()

        activitySearchResultBinding.toolbar.title = searchText
        setSupportActionBar(activitySearchResultBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        jobAdapter = JobAdapter(this, this)
        recruiterViewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]
        searchViewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]
        jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]

        addSearchHistory(searchText)
    }

    fun searchJob(searchText: String) {
        jobViewModel.searchJob(searchText).observe(this) { jobs ->
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

    private fun addSearchHistory(searchText: String) {
        val searchDate = DateUtils.getCurrentDate()
        val applicantId = AuthHelper.currentUser?.uid.toString()
        val searchHistory = SearchHistoryResponseEntity(
            "",
            searchText,
            searchDate,
            applicantId
        )
        searchViewModel.addSearchHistory(searchHistory, this)
    }

    override fun onSuccessAdding() {
        searchJob(searchText)
    }

    override fun onFailureAdding(messageId: Int) {
        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClicked(jobId: String) {
        val intent = Intent(this, JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.EXTRA_ID, jobId)
        startActivity(intent)
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
}