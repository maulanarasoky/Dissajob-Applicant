package org.d3ifcool.dissajobapplicant.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.databinding.FragmentHomeBinding
import org.d3ifcool.dissajobapplicant.ui.job.JobAdapter
import org.d3ifcool.dissajobapplicant.ui.job.JobViewModel
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterViewModel
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.vo.Status

class HomeFragment : Fragment(), JobAdapter.ItemClickListener, JobAdapter.LoadRecruiterDataCallback {

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
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            fragmentHomeBinding.progressBar.visibility = View.VISIBLE
        } else {
            fragmentHomeBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onItemClicked(jobId: String) {
//        val intent = Intent(activity, JobDetailsActivity::class.java)
//        intent.putExtra(JobDetailsActivity.EXTRA_ID, jobId)
//        startActivity(intent)
    }

    override fun onLoadRecruiterData(
        recruiterId: String,
        callback: JobAdapter.LoadRecruiterDataCallback
    ) {
        recruiterViewModel.getRecruiterData(recruiterId).observe(this) { applicantDetails ->
            if (applicantDetails != null) {
                applicantDetails.data?.let { callback.onRecruiterDataReceived(it) }
            }
        }
    }

    override fun onRecruiterDataReceived(recruiterData: RecruiterEntity) {
        TODO("Not yet implemented")
    }
}