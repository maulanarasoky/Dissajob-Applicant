package org.d3ifcool.dissajobapplicant.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.databinding.ActivitySearchBinding
import org.d3ifcool.dissajobapplicant.ui.search.callback.*
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.database.AuthHelper
import org.d3ifcool.dissajobapplicant.vo.Status

class SearchActivity : AppCompatActivity(),
    SearchHistoryItemClickCallback,
    SearchHistoryDeleteClickCallback, DeleteSearchHistoryCallback, DeleteAllSearchHistoryCallback,
    View.OnClickListener {

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
        searchViewModel.getSearchHistories(AuthHelper.currentUser?.uid.toString())
            .observe(this) { searchHistory ->
                if (searchHistory.data != null) {
                    when (searchHistory.status) {
                        Status.LOADING -> {
                        }
                        Status.SUCCESS -> {
                            if (searchHistory.data.isNotEmpty()) {
                                searchAdapter.submitList(searchHistory.data)
                                searchAdapter.notifyDataSetChanged()
                                showHistory(true)
                            } else {
                                showHistory(false)
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

        activitySearchBinding.header.searchBar.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!TextUtils.isEmpty(
                            activitySearchBinding.header.searchBar.text.toString().trim()
                        )
                    ) {
                        showSearchResult(
                            activitySearchBinding.header.searchBar.text.toString().trim()
                        )
                    }
                    return true
                }
                return false
            }
        })

        activitySearchBinding.header.imgBackBtn.setOnClickListener(this)
    }


    private fun showHistory(state: Boolean) {
        if (state) {
            activitySearchBinding.tvSearchHistory.visibility = View.VISIBLE
            activitySearchBinding.tvDeleteHistory.visibility = View.VISIBLE
            activitySearchBinding.rvSearchHistory.visibility = View.VISIBLE

            activitySearchBinding.tvDeleteHistory.setOnClickListener(this)
        } else {
            activitySearchBinding.tvSearchHistory.visibility = View.GONE
            activitySearchBinding.tvDeleteHistory.visibility = View.GONE
            activitySearchBinding.rvSearchHistory.visibility = View.GONE
        }
    }

    private fun showSearchResult(searchText: String) {
        val intent = Intent(this, SearchResultActivity::class.java)
        intent.putExtra(
            SearchResultActivity.SEARCH_TEXT,
            searchText
        )
        startActivity(intent)
    }

    override fun onItemClicked(searchText: String) {
        showSearchResult(searchText)
    }

    override fun deleteSearchHistory(searchHistoryId: String) {
        searchViewModel.deleteSearchHistoryById(searchHistoryId, this)
    }

    override fun onSuccessDelete() {
        searchAdapter.notifyDataSetChanged()
    }

    override fun onFailureDelete(messageId: Int) {
        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessDeleteAllHistory() {
        showHistory(false)
    }

    override fun onFailureDeleteAllHistory(messageId: Int) {
        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBackBtn -> finish()
            R.id.tvDeleteHistory -> searchViewModel.deleteAllSearchHistories(
                AuthHelper.currentUser?.uid.toString(),
                this
            )
        }
    }
}