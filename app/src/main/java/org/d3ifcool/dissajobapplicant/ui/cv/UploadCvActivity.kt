package org.d3ifcool.dissajobapplicant.ui.cv

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.databinding.ActivityUploadCvBinding


class UploadCvActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        //image pick code
        private const val FILE_PICK_CODE = 2000

        //Permission code
        private const val PERMISSION_CODE = 1001
    }

    private lateinit var cvFile: Uri

    private lateinit var activityUploadCvBinding: ActivityUploadCvBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUploadCvBinding = ActivityUploadCvBinding.inflate(layoutInflater)
        setContentView(activityUploadCvBinding.root)

        activityUploadCvBinding.btnSelectCv.setOnClickListener(this)
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
                intent.putExtra(CvDetailsActivity.CV_FILE, cvFile)
                startActivity(intent)
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnSelectCv -> checkPermission()
        }
    }
}