package org.d3ifcool.dissajobapplicant.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.databinding.ActivitySearchBinding
import org.d3ifcool.dissajobapplicant.ui.job.JobViewModel
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterProfileActivity
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.AuthHelper
import org.d3ifcool.dissajobapplicant.vo.Status

class SearchActivity : AppCompatActivity(), SearchHistoryItemClickCallback, SearchHistoryDeleteClickCallback {

    private lateinit var activitySearchBinding: ActivitySearchBinding

    private lateinit var searchViewModel: SearchViewModel

    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(activitySearchBinding.root)

        val factory = ViewModelFactory.getInstance(this)
        searchAdapter = SearchAdapter(this, this)
        searchViewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]
        searchViewModel.getSearchHistories(AuthHelper.currentUser?.uid.toString()).observe(this) { searchHistory ->
            if (searchHistory.data != null) {
                when (searchHistory.status) {
                    Status.LOADING -> {}
                    Status.SUCCESS -> {
                        if (searchHistory.data.isNotEmpty()) {
                            searchAdapter.submitList(searchHistory.data)
                            searchAdapter.notifyDataSetChanged()
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        with(activitySearchBinding.rvSearchHistory) {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@SearchActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = searchAdapter
        }

    }

    override fun onItemClicked(searchHistoryId: String) {
    }

    override fun deleteSearchHistory(searchHistoryId: String) {
        TODO("Not yet implemented")
    }
}