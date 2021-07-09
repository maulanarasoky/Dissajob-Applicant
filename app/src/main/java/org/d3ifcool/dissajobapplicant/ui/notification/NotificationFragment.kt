package org.d3ifcool.dissajobapplicant.ui.notification

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
import com.google.firebase.auth.FirebaseAuth
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.databinding.FragmentNotificationBinding
import org.d3ifcool.dissajobapplicant.ui.application.ApplicationDetailsActivity
import org.d3ifcool.dissajobapplicant.ui.recruiter.LoadRecruiterDataCallback
import org.d3ifcool.dissajobapplicant.ui.recruiter.RecruiterViewModel
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.vo.Status

class NotificationFragment : Fragment(), OnNotificationClickCallback, LoadRecruiterDataCallback {

    private lateinit var fragmentNotificationBinding: FragmentNotificationBinding

    private lateinit var notificationViewModel: NotificationViewModel

    private lateinit var recruiterViewModel: RecruiterViewModel

    private lateinit var notificationAdapter: NotificationAdapter

    private val applicantId: String = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentNotificationBinding =
            FragmentNotificationBinding.inflate(layoutInflater, container, false)
        return fragmentNotificationBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            showLoading(true)
            val factory = ViewModelFactory.getInstance(requireContext())
            recruiterViewModel = ViewModelProvider(this, factory)[RecruiterViewModel::class.java]
            notificationViewModel =
                ViewModelProvider(this, factory)[NotificationViewModel::class.java]
            notificationAdapter = NotificationAdapter(this, this)
            notificationViewModel.getNotifications(applicantId)
                .observe(viewLifecycleOwner) { applications ->
                    if (applications != null) {
                        when (applications.status) {
                            Status.LOADING -> showLoading(true)
                            Status.SUCCESS -> {
                                showLoading(false)
                                if (applications.data?.isNotEmpty() == true) {
                                    notificationAdapter.submitList(applications.data)
                                    notificationAdapter.notifyDataSetChanged()
                                    showRecyclerView(true)
                                } else {
                                    showRecyclerView(false)
                                }
                            }
                            Status.ERROR -> {
                                showLoading(false)
                                showRecyclerView(false)
                                Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
        }

        with(fragmentNotificationBinding.rvNotification) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = notificationAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            fragmentNotificationBinding.progressBar.visibility = View.VISIBLE
        } else {
            fragmentNotificationBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerView(state: Boolean) {
        if (state) {
            fragmentNotificationBinding.rvNotification.visibility = View.VISIBLE
            fragmentNotificationBinding.tvNoData.visibility = View.GONE
        } else {
            fragmentNotificationBinding.rvNotification.visibility = View.GONE
            fragmentNotificationBinding.tvNoData.visibility = View.VISIBLE
        }
    }

    override fun onLoadRecruiterData(recruiterId: String, callback: LoadRecruiterDataCallback) {
        recruiterViewModel.getRecruiterData(recruiterId)
            .observe(viewLifecycleOwner) { recruiterDetails ->
                if (recruiterDetails != null) {
                    if (recruiterDetails.data != null) {
                        callback.onRecruiterDataReceived(recruiterDetails.data)
                    }
                }
            }
    }

    override fun onRecruiterDataReceived(recruiterData: RecruiterEntity) {
    }

    override fun onItemClick(applicationId: String, jobId: String, recruiterId: String) {
        val intent = Intent(activity, ApplicationDetailsActivity::class.java)
        intent.putExtra(ApplicationDetailsActivity.APPLICATION_ID, applicationId)
        intent.putExtra(ApplicationDetailsActivity.JOB_ID, jobId)
        intent.putExtra(ApplicationDetailsActivity.RECRUITER_ID, recruiterId)
        activity?.startActivity(intent)
    }
}