package org.d3ifcool.dissajobapplicant.ui.search

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.history.SearchHistoryResponseEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivitySearchResultBinding
import org.d3ifcool.dissajobapplicant.ui.job.JobAdapter
import org.d3ifcool.dissajobapplicant.ui.job.JobDetailsActivity
import org.d3ifcool.dissajobapplicant.ui.job.JobViewModel
import org.d3ifcool.dissajobapplicant.ui.job.callback.OnJobClickListener
import org.d3ifcool.dissajobapplicant.ui.recruiter.LoadRecruiterDataCallback
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterViewModel
import org.d3ifcool.dissajobapplicant.ui.search.callback.AddSearchHistoryCallback
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.DateUtils
import org.d3ifcool.dissajobapplicant.utils.database.AuthHelper
import org.d3ifcool.dissajobapplicant.vo.Status

class SearchResultActivity : AppCompatActivity(), OnJobClickListener, LoadRecruiterDataCallback,
    AddSearchHistoryCallback, CompoundButton.OnCheckedChangeListener {

    companion object {
        const val SEARCH_TEXT = "search_text"
    }

    private lateinit var activitySearchResultBinding: ActivitySearchResultBinding

    private lateinit var recruiterViewModel: RecruiterViewModel

    private lateinit var searchViewModel: SearchViewModel

    private lateinit var jobViewModel: JobViewModel

    private lateinit var jobAdapter: JobAdapter

    private lateinit var searchText: String

    private lateinit var searchedJobs: PagedList<JobEntity>

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

        activitySearchResultBinding.chipFilter.setOnCheckedChangeListener(this)
    }

    fun searchJob(searchText: String, unFilter: Boolean) {
        if (unFilter) {
            jobAdapter.submitList(searchedJobs)
            jobAdapter.notifyDataSetChanged()
        } else {
            jobViewModel.searchJob(searchText).observe(this) { jobs ->
                if (jobs != null) {
                    when (jobs.status) {
                        Status.LOADING -> showLoading(true)
                        Status.SUCCESS -> {
                            showLoading(false)
                            if (jobs.data?.isNotEmpty() == true) {
                                searchedJobs = jobs.data
                                jobAdapter.submitList(jobs.data)
                                jobAdapter.notifyDataSetChanged()
                            } else {
                                activitySearchResultBinding.linearSearchResult.visibility =
                                    View.GONE
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
        }
    }

    private fun filterJob(searchText: String) {
        showLoading(true)
        jobViewModel.getFilteredJobs(searchText).observe(this) { jobs ->
            if (jobs != null) {
                showLoading(false)
                jobAdapter.submitList(jobs)
                jobAdapter.notifyDataSetChanged()
            } else {
                activitySearchResultBinding.linearSearchResult.visibility = View.GONE
                activitySearchResultBinding.tvNoData.visibility = View.VISIBLE
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            activitySearchResultBinding.linearSearchResult.visibility = View.GONE
            activitySearchResultBinding.progressBar.visibility = View.VISIBLE
        } else {
            activitySearchResultBinding.linearSearchResult.visibility = View.VISIBLE
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
        searchJob(searchText, false)

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

    override fun onFailureAdding(messageId: Int) {
        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
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

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {

            activitySearchResultBinding.chipFilter.chipBackgroundColor = ColorStateList.valueOf(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorPrimary
                )
            )

            activitySearchResultBinding.chipFilter.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
            )

            filterJob(searchText)
        } else {
            activitySearchResultBinding.chipFilter.chipBackgroundColor = ColorStateList.valueOf(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.white
                )
            )

            activitySearchResultBinding.chipFilter.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.black
                    )
                )
            )

            searchJob(searchText, true)
        }
    }

    override fun onItemClick(jobId: String) {
        val intent = Intent(this, JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.EXTRA_ID, jobId)
        startActivity(intent)
    }
}