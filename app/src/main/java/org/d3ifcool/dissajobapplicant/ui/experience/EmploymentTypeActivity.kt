package org.d3ifcool.dissajobapplicant.ui.experience

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.databinding.ActivityEmploymentTypeBinding

class EmploymentTypeActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val REQUEST_EMPLOYMENT = 100
        const val RESULT_EMPLOYMENT = 101
        const val SELECTED_EMPLOYMENT = "selected_employment"
    }

    private lateinit var activityEmploymentTypeBinding: ActivityEmploymentTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityEmploymentTypeBinding = ActivityEmploymentTypeBinding.inflate(layoutInflater)
        setContentView(activityEmploymentTypeBinding.root)

        activityEmploymentTypeBinding.toolbar.title =
            resources.getString(R.string.txt_employment_type)
        setSupportActionBar(activityEmploymentTypeBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        activityEmploymentTypeBinding.tvFullTimeType.setOnClickListener(this)
        activityEmploymentTypeBinding.tvPartTimeType.setOnClickListener(this)
        activityEmploymentTypeBinding.tvSelfEmployedType.setOnClickListener(this)
        activityEmploymentTypeBinding.tvFreelanceType.setOnClickListener(this)
        activityEmploymentTypeBinding.tvContractType.setOnClickListener(this)
        activityEmploymentTypeBinding.tvInternshipType.setOnClickListener(this)
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
            R.id.tvFullTimeType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_EMPLOYMENT, "Purnawaktu")
                setResult(RESULT_EMPLOYMENT, intent)
                finish()
            }
            R.id.tvPartTimeType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_EMPLOYMENT, "Paruh waktu")
                setResult(RESULT_EMPLOYMENT, intent)
                finish()
            }
            R.id.tvSelfEmployedType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_EMPLOYMENT, "Wiraswasta")
                setResult(RESULT_EMPLOYMENT, intent)
                finish()
            }
            R.id.tvFreelanceType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_EMPLOYMENT, "Pekerja lepas")
                setResult(RESULT_EMPLOYMENT, intent)
                finish()
            }
            R.id.tvContractType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_EMPLOYMENT, "Kontrak")
                setResult(RESULT_EMPLOYMENT, intent)
                finish()
            }
            R.id.tvInternshipType -> {
                val intent = Intent()
                intent.putExtra(SELECTED_EMPLOYMENT, "Magang")
                setResult(RESULT_EMPLOYMENT, intent)
                finish()
            }
        }
    }
}