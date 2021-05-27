package org.d3ifcool.dissajobapplicant.ui.cv

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.cv.CvResponseEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityUploadCvBinding
import org.d3ifcool.dissajobapplicant.ui.cv.callback.LoadCvFileCallback
import org.d3ifcool.dissajobapplicant.ui.cv.callback.LoadPdfCallback
import org.d3ifcool.dissajobapplicant.ui.cv.callback.OnClickCvListener
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import org.d3ifcool.dissajobapplicant.utils.database.AuthHelper
import org.d3ifcool.dissajobapplicant.vo.Status


class UploadCvActivity : AppCompatActivity(), View.OnClickListener, LoadPdfCallback,
    OnClickCvListener {

    companion object {
        //image pick code
        private const val FILE_PICK_CODE = 2000

        //Permission code
        private const val PERMISSION_CODE = 1001
    }

    private lateinit var cvFile: Uri

    private lateinit var activityUploadCvBinding: ActivityUploadCvBinding

    private lateinit var cvViewModel: CvViewModel

    private lateinit var cvAdapter: CvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUploadCvBinding = ActivityUploadCvBinding.inflate(layoutInflater)
        setContentView(activityUploadCvBinding.root)

        activityUploadCvBinding.toolbar.title = resources.getString(R.string.txt_curriculum_vitae)
        setSupportActionBar(activityUploadCvBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val applicantId = AuthHelper.currentUser?.uid.toString()
        val factory = ViewModelFactory.getInstance(this)
        cvViewModel = ViewModelProvider(this, factory)[CvViewModel::class.java]

        cvAdapter = CvAdapter(this)
        showCv(applicantId)
        with(activityUploadCvBinding.rvCv) {
            layoutManager = LinearLayoutManager(this@UploadCvActivity)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    this@UploadCvActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = cvAdapter
        }

        activityUploadCvBinding.btnSelectCv.setOnClickListener(this)
    }

    private fun showCv(applicantId: String) {
        cvViewModel.getApplicantCv(applicantId)
            .observe(this) { cvFiles ->
                if (cvFiles != null) {
                    when (cvFiles.status) {
                        Status.LOADING -> showLoading(true)
                        Status.SUCCESS -> {
                            showLoading(false)
                            if (cvFiles.data?.isNotEmpty() == true) {
                                showRecyclerViewCv(true)
                                cvAdapter.submitList(cvFiles.data)
                                cvAdapter.notifyDataSetChanged()
                            } else {
                                showRecyclerViewCv(false)
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
            activityUploadCvBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityUploadCvBinding.progressBar.visibility = View.GONE
        }
    }

    private fun showRecyclerViewCv(state: Boolean) {
        if (state) {
            activityUploadCvBinding.rvCv.visibility = View.VISIBLE
            activityUploadCvBinding.imgFolder.visibility = View.GONE
            activityUploadCvBinding.tvCvDescription.visibility = View.GONE
            activityUploadCvBinding.btnSelectCv.visibility = View.GONE
        } else {
            activityUploadCvBinding.rvCv.visibility = View.GONE
            activityUploadCvBinding.imgFolder.visibility = View.VISIBLE
            activityUploadCvBinding.tvCvDescription.visibility = View.VISIBLE
            activityUploadCvBinding.btnSelectCv.visibility = View.VISIBLE
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
                pickCvFile()
            }
        } else {
            //system OS is < Marshmallow
            pickCvFile()
        }
    }

    private fun pickCvFile() {
        val intentPDF = Intent(Intent.ACTION_GET_CONTENT)
        intentPDF.type = "application/pdf"
        intentPDF.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intentPDF, "Select File"), FILE_PICK_CODE)
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
                    pickCvFile()
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
                cvFile = data.data!!
                val intent = Intent(this, CvDetailsActivity::class.java)
                intent.putExtra(CvDetailsActivity.CV_FILE, cvFile.toString())
                startActivityForResult(intent, CvDetailsActivity.REQUEST_ADD)
            }
        } else if (requestCode == CvDetailsActivity.REQUEST_ADD) {
            if (resultCode == CvDetailsActivity.RESULT_ADD) {
                val applicantId = AuthHelper.currentUser?.uid.toString()
                showCv(applicantId)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_cv_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.addCv -> {
                checkPermission()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSelectCv -> checkPermission()
        }
    }

    override fun onLoadPdfData(cvId: String, callback: LoadPdfCallback) {
        cvViewModel.getCvById(cvId, object : LoadCvFileCallback {
            override fun onCvFileReceived(cvFile: ByteArray) {
                callback.onPdfDataReceived(cvFile)
            }
        })
    }

    override fun onPdfDataReceived(cvFile: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun onCvClick(cvData: CvResponseEntity) {
        TODO("Not yet implemented")
    }
}