package org.d3ifcool.dissajobapplicant.ui.media

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.MediaEntity
import org.d3ifcool.dissajobapplicant.databinding.MediaItemBinding
import org.d3ifcool.dissajobapplicant.ui.media.callback.LoadPdfCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.OnDeleteBtnClickListener
import org.d3ifcool.dissajobapplicant.ui.media.callback.OnMediaClickListener

class MediaAdapter(
    private val loadPdfCallback: LoadPdfCallback,
    private val onItemClickCallback: OnMediaClickListener,
    private val onDeleteBtnClickCallback: OnDeleteBtnClickListener
) :
    PagedListAdapter<MediaEntity, MediaAdapter.MediaViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MediaEntity>() {
            override fun areItemsTheSame(
                oldItem: MediaEntity,
                newItem: MediaEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MediaEntity,
                newItem: MediaEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val itemsMediaBinding =
            MediaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(itemsMediaBinding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val media = getItem(position)
        if (media != null) {
            holder.bindItem(media)
        }
    }

    inner class MediaViewHolder(private val binding: MediaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(items: MediaEntity) {
            with(binding) {
                progressBar.visibility = View.VISIBLE
                loadPdfCallback.onLoadPdfData(items.fileId, object : LoadPdfCallback {
                    override fun onLoadPdfData(mediaId: String, callback: LoadPdfCallback) {
                    }

                    override fun onPdfDataReceived(mediaFile: ByteArray) {
                        progressBar.visibility = View.GONE
                        imgDeleteHistory.visibility = View.VISIBLE

                        pdfViewer.fromBytes(mediaFile)
                            .enableSwipe(true)
                            .swipeHorizontal(true)
                            .load()


                    }
                })

                tvMediaName.text = items.mediaName.toString()
                if (items.mediaDescription.toString() != "-") {
                    tvMediaDescription.visibility = View.VISIBLE
                    tvMediaDescription.text = items.mediaDescription.toString()
                }

                imgDeleteHistory.setOnClickListener {
                    onDeleteBtnClickCallback.onDeleteBtnClick(
                        items.id,
                        items.mediaName,
                        items.fileId
                    )
                }

                itemView.setOnClickListener {
                    onItemClickCallback.onClickItem(items)
                }
            }
        }
    }
}