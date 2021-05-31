package org.d3ifcool.dissajobapplicant.ui.education

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.databinding.ActivityEducationLevelBinding

class EducationLevelActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val REQUEST_DEGREE = 300
        const val RESULT_DEGREE = 301
        const val SELECTED_DEGREE = "selected_degree"
    }

    private lateinit var activityEducationLevelBinding: ActivityEducationLevelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEducationLevelBinding = ActivityEducationLevelBinding.inflate(layoutInflater)
        setContentView(activityEducationLevelBinding.root)

        activityEducationLevelBinding.toolbar.title =
            resources.getString(R.string.txt_education_level)
        setSupportActionBar(activityEducationLevelBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        activityEducationLevelBinding.tvElementaryLevel.setOnClickListener(this)
        activityEducationLevelBinding.tvJuniorLevel.setOnClickListener(this)
        activityEducationLevelBinding.tvSeniorLevel.setOnClickListener(this)
        activityEducationLevelBinding.tvVocationalLevel.setOnClickListener(this)
        activityEducationLevelBinding.tvDiplomaOneLevel.setOnClickListener(this)
        activityEducationLevelBinding.tvDiplomaTwoLevel.setOnClickListener(this)
        activityEducationLevelBinding.tvDiplomaThreeLevel.setOnClickListener(this)
        activityEducationLevelBinding.tvDiplomaFourLevel.setOnClickListener(this)
        activityEducationLevelBinding.tvBachelorLevel.setOnClickListener(this)
        activityEducationLevelBinding.tvUndergraduateLevel.setOnClickListener(this)
        activityEducationLevelBinding.tvPostgraduateLevel.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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