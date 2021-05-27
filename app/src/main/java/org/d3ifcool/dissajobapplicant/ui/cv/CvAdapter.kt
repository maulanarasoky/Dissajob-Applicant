package org.d3ifcool.dissajobapplicant.ui.cv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.dissajobapplicant.data.source.local.entity.cv.CvEntity
import org.d3ifcool.dissajobapplicant.databinding.CvItemBinding
import org.d3ifcool.dissajobapplicant.ui.cv.callback.LoadPdfCallback
import java.io.InputStream

class CvAdapter(private val loadPdfCallback: LoadPdfCallback) :
    PagedListAdapter<CvEntity, CvAdapter.CvViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CvEntity>() {
            override fun areItemsTheSame(
                oldItem: CvEntity,
                newItem: CvEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CvEntity,
                newItem: CvEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CvViewHolder {
        val itemsCvBinding =
            CvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CvViewHolder(itemsCvBinding)
    }

    override fun onBindViewHolder(holder: CvViewHolder, position: Int) {
        val cv = getItem(position)
        if (cv != null) {
            holder.bindItem(cv)
        }
    }

    inner class CvViewHolder(private val binding: CvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(items: CvEntity) {
            with(binding) {

                loadPdfCallback.onLoadPdfData(items.fileId, object : LoadPdfCallback {
                    override fun onLoadPdfData(cvId: String, callback: LoadPdfCallback) {
                    }

                    override fun onPdfDataReceived(cvFile: ByteArray) {
                        pdfViewer.fromBytes(cvFile)
                            .enableSwipe(true)
                            .swipeHorizontal(true)
                            .load()

                        tvCvName.text = items.cvName.toString()
                        if (items.cvDescription.toString() != "-") {
                            tvCvDescription.visibility = View.VISIBLE
                            tvCvDescription.text = items.cvDescription.toString()
                        }
                    }
                })

//                tvCvName.text = items.cvName.toString()
//                if (items.cvDescription.toString() != "-") {
//                    tvCvDescription.visibility = View.VISIBLE
//                    tvCvDescription.text = items.cvDescription.toString()
//                }
//
//                itemView.setOnClickListener {
//                    cvClickCallback.onCvClick(items.cvName.toString(), items.fileId)
//                }
            }
        }
    }
}