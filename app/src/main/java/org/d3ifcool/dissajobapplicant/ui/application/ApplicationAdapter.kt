package org.d3ifcool.dissajobapplicant.ui.application

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity
import org.d3ifcool.dissajobapplicant.databinding.JobItemBinding
import org.d3ifcool.dissajobapplicant.ui.job.callback.LoadJobByIdCallback
import org.d3ifcool.dissajobapplicant.ui.job.callback.OnJobClickListener
import org.d3ifcool.dissajobapplicant.ui.recruiter.LoadRecruiterDataCallback
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ApplicationAdapter(
    private val onItemClickCallback: OnJobClickListener,
    private val loadJobDataCallback: LoadJobByIdCallback,
    private val loadRecruiterDataCallback: LoadRecruiterDataCallback
) : PagedListAdapter<ApplicationEntity, ApplicationAdapter.JobViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ApplicationEntity>() {
            override fun areItemsTheSame(
                oldItem: ApplicationEntity,
                newItem: ApplicationEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ApplicationEntity,
                newItem: ApplicationEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val itemsJobBinding =
            JobItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(itemsJobBinding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position)
        if (job != null) {
            holder.bindItem(job)
        }
    }

    inner class JobViewHolder(private val binding: JobItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(items: ApplicationEntity) {
            loadJobData(items.jobId.toString())
        }

        private fun loadJobData(jobId: String) {
            loadJobDataCallback.onLoadJobData(jobId, object : LoadJobByIdCallback {
                override fun onLoadJobData(jobId: String, callback: LoadJobByIdCallback) {
                }

                override fun onJobReceived(jobEntity: JobEntity) {
                    with(binding) {
                        tvJobTitle.text = jobEntity.title.toString()
                        tvJobAddress.text = jobEntity.address.toString()
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        sdf.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
                        try {
                            val time: Long = sdf.parse(jobEntity.postedDate).time
                            val now = System.currentTimeMillis()
                            val ago =
                                DateUtils.getRelativeTimeSpanString(
                                    time,
                                    now,
                                    DateUtils.MINUTE_IN_MILLIS
                                )
                            tvJobPostedDate.text = ago
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }

                        loadRecruiterData(jobEntity.postedBy.toString())

                        itemView.setOnClickListener {
                            onItemClickCallback.onItemClick(jobId)
                        }
                    }
                }

            })
        }

        private fun loadRecruiterData(recruiterId: String) {
            loadRecruiterDataCallback.onLoadRecruiterData(recruiterId, object :
                LoadRecruiterDataCallback {
                override fun onLoadRecruiterData(
                    recruiterId: String,
                    callback: LoadRecruiterDataCallback
                ) {
                }

                override fun onRecruiterDataReceived(recruiterData: RecruiterEntity) {
                    with(binding) {
                        tvJobRecruiterName.text = recruiterData.fullName.toString()
                        if (recruiterData.imagePath != "-") {
                            val storageRef = Firebase.storage.reference
                            val circularProgressDrawable =
                                CircularProgressDrawable(itemView.context)
                            circularProgressDrawable.strokeWidth = 5f
                            circularProgressDrawable.centerRadius = 30f
                            circularProgressDrawable.start()
                            Glide.with(itemView.context)
                                .load(storageRef.child("recruiter/profile/images/${recruiterData.imagePath}"))
                                .transform(RoundedCorners(20))
                                .apply(RequestOptions.placeholderOf(circularProgressDrawable))
                                .error(R.drawable.ic_image_gray_24dp)
                                .into(imgRecruiterPicture)
                        }
                    }
                }

            })
        }
    }
}