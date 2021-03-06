package org.d3ifcool.dissajobapplicant.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.databinding.FragmentHomeBinding
import org.d3ifcool.dissajobapplicant.ui.job.JobAdapter
import org.d3ifcool.dissajobapplicant.ui.job.JobDetailsActivity
import org.d3ifcool.dissajobapplicant.ui.job.JobViewModel
import org.d3ifcool.dissajobapplicant.ui.job.callback.OnJobClickListener
import org.d3ifcool.dissajobapplicant.ui.recruiter.LoadRecruiterDataCallback
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterViewModel
import org.d3ifcool.dissajobapplicant.ui.search.SearchActivity
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.vo.Status

class HomeFragment : Fragment(), OnJobClickListener, LoadRecruiterDataCallback,
    View.OnClickListener {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding

    private lateinit var jobAdapter: JobAdapter

    private lateinit var recruiterViewModel: RecruiterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            showLoading(true)
            val factory = ViewModelFactory.getInstance(requireContext())
            recruiterViewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]
            val jobViewModel = ViewModelProvider(this, factory)[JobViewModel::class.java]
            jobAdapter = JobAdapter(this, this)
            jobViewModel.getJobs().observe(viewLifecycleOwner) { jobs ->
                if (jobs != null) {
                    when (jobs.status) {
                        Status.LOADING -> showLoading(true)
                        Status.SUCCESS -> {
                            showLoading(false)
                            if (jobs.data?.isNotEmpty() == true) {
                                jobAdapter.submitList(jobs.data)
                                jobAdapter.notifyDataSetChanged()
                                fragmentHomeBinding.tvNoData.visibility = View.GONE
                            } else {
                                fragmentHomeBinding.tvNoData.visibility = View.VISIBLE
                            }
                        }
                        Status.ERROR -> {
                            showLoading(false)
                            Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        with(fragmentHomeBinding.rvJob) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = jobAdapter
        }

        fragmentHomeBinding.header.searchBar.setOnClickListener(this)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            fragmentHomeBinding.progressBar.visibility = View.VISIBLE
        } else {
            fragmentHomeBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onLoadRecruiterData(
        recruiterId: String,
        callback: LoadRecruiterDataCallback
    ) {
        recruiterViewModel.getRecruiterData(recruiterId).observe(this) { recruiterDetails ->
            if (recruiterDetails != null) {
                recruiterDetails.data?.let { callback.onRecruiterDataReceived(it) }
            }
        }
    }

    override fun onRecruiterDataReceived(recruiterData: RecruiterEntity) {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.searchBar -> startActivity(Intent(activity, SearchActivity::class.java))
        }
    }

    override fun onItemClick(jobId: String) {
        val intent = Intent(activity, JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.EXTRA_ID, jobId)
        startActivity(intent)
    }
}