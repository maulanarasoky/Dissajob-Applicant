package org.d3ifcool.dissajobapplicant.ui.education

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.databinding.ActivityEducationDegreeBinding

class EducationDegreeActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val REQUEST_DEGREE = 300
        const val RESULT_DEGREE = 301
        const val SELECTED_DEGREE = "selected_degree"
    }

    private lateinit var activityEducationDegreeBinding: ActivityEducationDegreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEducationDegreeBinding = ActivityEducationDegreeBinding.inflate(layoutInflater)
        setContentView(activityEducationDegreeBinding.root)

        activityEducationDegreeBinding.toolbar.title =
            resources.getString(R.string.txt_education_level)
        setSupportActionBar(activityEducationDegreeBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        activityEducationDegreeBinding.tvElementaryLevel.setOnClickListener(this)
        activityEducationDegreeBinding.tvJuniorLevel.setOnClickListener(this)
        activityEducationDegreeBinding.tvSeniorLevel.setOnClickListener(this)
        activityEducationDegreeBinding.tvVocationalLevel.setOnClickListener(this)
        activityEducationDegreeBinding.tvDiplomaOneLevel.setOnClickListener(this)
        activityEducationDegreeBinding.tvDiplomaTwoLevel.setOnClickListener(this)
        activityEducationDegreeBinding.tvDiplomaThreeLevel.setOnClickListener(this)
        activityEducationDegreeBinding.tvDiplomaFourLevel.setOnClickListener(this)
        activityEducationDegreeBinding.tvBachelorLevel.setOnClickListener(this)
        activityEducationDegreeBinding.tvUndergraduateLevel.setOnClickListener(this)
        activityEducationDegreeBinding.tvPostgraduateLevel.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvElementaryLevel -> {
                val intent = Intent()
                intent.putExtra(SELECTED_DEGREE, "SD")
                setResult(RESULT_DEGREE, intent)
                finish()
            }
            R.id.tvJuniorLevel -> {
                val intent = Intent()
                intent.putExtra(SELECTED_DEGREE, "SMP")
                setResult(RESULT_DEGREE, intent)
                finish()
            }
            R.id.tvSeniorLevel -> {
                val intent = Intent()
                intent.putExtra(SELECTED_DEGREE, "SMA")
                setResult(RESULT_DEGREE, intent)
                finish()
            }
            R.id.tvVocationalLevel -> {
                val intent = Intent()
                intent.putExtra(SELECTED_DEGREE, "SMK")
                setResult(RESULT_DEGREE, intent)
                finish()
            }
            R.id.tvDiplomaOneLevel -> {
                val intent = Intent()
                intent.putExtra(SELECTED_DEGREE, "D1")
                setResult(RESULT_DEGREE, intent)
                finish()
            }
            R.id.tvDiplomaTwoLevel -> {
                val intent = Intent()
                intent.putExtra(SELECTED_DEGREE, "D2")
                setResult(RESULT_DEGREE, intent)
                finish()
            }
            R.id.tvDiplomaThreeLevel -> {
                val intent = Intent()
                intent.putExtra(SELECTED_DEGREE, "D3")
                setResult(RESULT_DEGREE, intent)
                finish()
            }
            R.id.tvDiplomaFourLevel -> {
                val intent = Intent()
                intent.putExtra(SELECTED_DEGREE, "D4")
                setResult(RESULT_DEGREE, intent)
                finish()
            }
            R.id.tvBachelorLevel -> {
                val intent = Intent()
                intent.putExtra(SELECTED_DEGREE, "S1")
                setResult(RESULT_DEGREE, intent)
                finish()
            }
            R.id.tvUndergraduateLevel -> {
                val intent = Intent()
                intent.putExtra(SELECTED_DEGREE, "S2")
                setResult(RESULT_DEGREE, intent)
                finish()
            }
            R.id.tvPostgraduateLevel -> {
                val intent = Intent()
                intent.putExtra(SELECTED_DEGREE, "S3")
                setResult(RESULT_DEGREE, intent)
                finish()
            }
        }
    }
}