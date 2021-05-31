package org.d3ifcool.dissajobapplicant.ui.media

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.MediaEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityMediaBinding
import org.d3ifcool.dissajobapplicant.ui.media.callback.LoadPdfCallback
import org.d3ifcool.dissajobapplicant.ui.media.callback.OnClickEditMediaListener
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.database.AuthHelper
import org.d3ifcool.dissajobapplicant.vo.Status

class MediaActivity : AppCompatActivity(), View.OnClickListener, LoadPdfCallback,
    OnClickEditMediaListener {

    companion object {
        //image pick code
        private const val FILE_PICK_CODE = 2000

        //Permission code
        private const val PERMISSION_CODE = 1001
    }

    private lateinit var mediaFile: Uri

    private lateinit var activityMediaBinding: ActivityMediaBinding

    private lateinit var mediaViewModel: MediaViewModel

    private lateinit var mediaAdapter: MediaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMediaBinding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(activityMediaBinding.root)

        activityMediaBinding.toolbar.title = resources.getString(R.string.txt_media)
        setSupportActionBar(activityMediaBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val applicantId = AuthHelper.currentUser?.uid.toString()
        val factory = ViewModelFactory.getInstance(this)
        mediaViewModel = ViewModelProvider(this, factory)[MediaViewModel::class.java]

        mediaAdapter = MediaAdapter(this, this)
        showMedia(applicantId)
        with(activityMediaBinding.rvMedia) {
            recycledViewPool.setMaxRecycledViews(0, 0)
            layoutManager = LinearLayoutManager(this@MediaActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@MediaActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = mediaAdapter
        }

        activityMediaBinding.btnSelectMedia.setOnClickListener(this)
    }

    private fun showMedia(applicantId: String) {
        mediaViewModel.getApplicantMedia(applicantId)
            .observe(this) { mediaFiles ->
                if (mediaFiles != null) {
                    when (mediaFiles.status) {
                        Status.LOADING -> showLoading(true)
                        Status.SUCCESS -> {
                            showLoading(false)
                            if (mediaFiles.data?.isNotEmpty() == true) {
                                showRecyclerViewMedia(true)
                                mediaAdapter.submitList(mediaFiles.data)
                                mediaAdapter.notifyDataSetChanged()
                            } else {
                                showRecyclerViewMedia(false)
                            }
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            activityMediaBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityMediaBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerViewMedia(state: Boolean) {
        if (state) {
            activityMediaBinding.rvMedia.visibility = View.VISIBLE
            activityMediaBinding.imgFolder.visibility = View.GONE
            activityMediaBinding.tvMediaDescription.visibility = View.GONE
            activityMediaBinding.btnSelectMedia.visibility = View.GONE
        } else {
            activityMediaBinding.rvMedia.visibility = View.GONE
            activityMediaBinding.imgFolder.visibility = View.VISIBLE
            activityMediaBinding.tvMediaDescription.visibility = View.VISIBLE
            activityMediaBinding.btnSelectMedia.visibility = View.VISIBLE
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                //permission already granted
                pickMediaFile()
            }
        } else {
            //system OS is < Marshmallow
            pickMediaFile()
        }
    }

    private fun pickMediaFile() {
        val intentPDF = Intent(Intent.ACTION_GET_CONTENT)
        intentPDF.type = "application/pdf"
        intentPDF.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(intentPDF, "Select File"),
            FILE_PICK_CODE
        )
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickMediaFile()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == FILE_PICK_CODE) {
            if (data?.data != null) {
                data.data?.let {
                    contentResolver.query(it, null, null, null, null)
                }?.use { cursor ->
                    val name = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val size = cursor.getColumnIndex(OpenableColumns.SIZE)

                    cursor.moveToFirst()

                    val sizeInKb = cursor.getLong(size) / 1024

                    if (sizeInKb > 2048) {
                        Toast.makeText(this, "File tidak boleh diatas 2MB", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }

                    mediaFile = data.data!!
                    val intent = Intent(this, AddEditMediaActivity::class.java)
                    intent.putExtra(AddEditMediaActivity.MEDIA_FILE, mediaFile.toString())
                    intent.putExtra(AddEditMediaActivity.MEDIA_NAME, cursor.getString(name))
                    startActivityForResult(intent, AddEditMediaActivity.REQUEST_ADD)
                }
            }
        } else if (requestCode == AddEditMediaActivity.REQUEST_ADD) {
            if (resultCode == AddEditMediaActivity.RESULT_ADD) {
                showRecyclerViewMedia(true)
                showMedia(AuthHelper.currentUser?.uid.toString())
            }
        } else if (requestCode == AddEditMediaActivity.REQUEST_UPDATE) {
            if (resultCode == AddEditMediaActivity.RESULT_UPDATE) {
                showMedia(AuthHelper.currentUser?.uid.toString())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_toolbar_add_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.addMenu -> {
                checkPermission()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSelectMedia -> checkPermission()
        }
    }

    override fun onLoadPdfData(mediaId: String, callback: LoadPdfCallback) {
        mediaViewModel.getMediaById(mediaId).observe(this) { file ->
            if (file != null) {
                callback.onPdfDataReceived(file)
            } else {
                Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPdfDataReceived(mediaFile: ByteArray) {
    }

    override fun onClickBtnEdit(mediaData: MediaEntity) {
        val intent = Intent(this, AddEditMediaActivity::class.java)
        intent.putExtra(AddEditMediaActivity.MEDIA_DATA, mediaData)
        startActivityForResult(intent, AddEditMediaActivity.REQUEST_UPDATE)
    }
}