package org.d3ifcool.dissajobapplicant.ui.experience

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.experience.ExperienceEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.experience.ExperienceResponseEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityAddEditExperienceBinding
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import java.text.DateFormatSymbols
import java.util.*

class AddEditExperienceActivity : AppCompatActivity(), AddExperienceCallback, View.OnClickListener,
    CompoundButton.OnCheckedChangeListener, UpdateExperienceCallback {

    companion object {
        const val EXPERIENCE_DATA = "experience_data"
    }

    private lateinit var activityAddEditExperienceBinding: ActivityAddEditExperienceBinding

    private lateinit var viewModel: ExperienceViewModel

    private lateinit var dialog: SweetAlertDialog

    private var startMonth = 0
    private var startYear = 0
    private var endMonth = 0
    private var endYear = 0

    private var isEdit = false

    private lateinit var experienceId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddEditExperienceBinding = ActivityAddEditExperienceBinding.inflate(layoutInflater)
        setContentView(activityAddEditExperienceBinding.root)

        activityAddEditExperienceBinding.toolbar.title =
            resources.getString(R.string.txt_work_experience)
        setSupportActionBar(activityAddEditExperienceBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ExperienceViewModel::class.java]

        val oldExperienceData = intent.getParcelableExtra<ExperienceEntity>(EXPERIENCE_DATA)
        if (oldExperienceData != null) {
            showOldExperienceData(oldExperienceData)
            activityAddEditExperienceBinding.btnSubmitExperience.text =
                resources.getString(R.string.txt_update)
        }

        activityAddEditExperienceBinding.etEmploymentType.setOnClickListener(this)
        activityAddEditExperienceBinding.cbCurrentWorking.setOnCheckedChangeListener(this)
        activityAddEditExperienceBinding.etStartDate.setOnClickListener(this)
        activityAddEditExperienceBinding.etEndDate.setOnClickListener(this)
        activityAddEditExperienceBinding.btnSubmitExperience.setOnClickListener(this)
    }

    private fun showOldExperienceData(experience: ExperienceEntity) {
        activityAddEditExperienceBinding.etTitle.setText(experience.title.toString())
        activityAddEditExperienceBinding.etEmploymentType.setText(experience.employmentType.toString())
        activityAddEditExperienceBinding.etCompanyName.setText(experience.companyName.toString())
        activityAddEditExperienceBinding.etLocation.setText(experience.location.toString())

        activityAddEditExperienceBinding.cbCurrentWorking.isChecked = experience.isCurrentlyWorking

        val startMonth = DateFormatSymbols().months[experience.startMonth - 1]
        val startDate = "$startMonth ${experience.startYear}"

        val endDate = if (experience.isCurrentlyWorking) {
            activityAddEditExperienceBinding.etEndDate.isEnabled = false
            resources.getString(R.string.txt_currently_working)
        } else {
            val endMonth = DateFormatSymbols().months[experience.endMonth - 1]
            "$endMonth ${experience.endYear}"
        }

        activityAddEditExperienceBinding.etStartDate.setText(startDate)
        activityAddEditExperienceBinding.etEndDate.setText(endDate)
        if (experience.description != "-") {
            activityAddEditExperienceBinding.etExperienceDescription.setText(experience.description.toString())
        }

        this.experienceId = experience.id
        this.startMonth = experience.startMonth
        this.startYear = experience.startYear
        this.endMonth = experience.endMonth
        this.endYear = experience.endYear
        isEdit = true
    }

    private fun submitExperience() {
        val title = activityAddEditExperienceBinding.etTitle.text.toString().trim()
        val employmentType =
            activityAddEditExperienceBinding.etEmploymentType.text.toString().trim()
        val companyName = activityAddEditExperienceBinding.etCompanyName.text.toString().trim()
        val location = activityAddEditExperienceBinding.etLocation.text.toString().trim()
        var experienceDescription =
            activityAddEditExperienceBinding.etExperienceDescription.text.toString().trim()
        val isCurrentlyWorking = activityAddEditExperienceBinding.cbCurrentWorking.isChecked

        if (TextUtils.isEmpty(experienceDescription)) {
            experienceDescription = "-"
        }

        val experience = ExperienceResponseEntity(
            "",
            title,
            employmentType,
            companyName,
            location,
            startMonth,
            startYear,
            endMonth,
            endYear,
            experienceDescription,
            isCurrentlyWorking,
            ""
        )

        if (!isEdit) {
            viewModel.addApplicantExperience(experience, this)
        } else {
            val oldExperienceData = intent.getParcelableExtra<ExperienceEntity>(EXPERIENCE_DATA)
            experience.id = oldExperienceData?.id.toString()
            experience.applicantId = oldExperienceData?.applicantId.toString()
            viewModel.updateApplicantExperience(experience, this)
        }
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(
                activityAddEditExperienceBinding.etTitle.text.toString().trim()
            )
        ) {
            activityAddEditExperienceBinding.etTitle.error =
                resources.getString(R.string.edit_text_error_alert, "Judul")
            return
        }

        if (TextUtils.isEmpty(
                activityAddEditExperienceBinding.etEmploymentType.text.toString().trim()
            )
        ) {
            activityAddEditExperienceBinding.etEmploymentType.error =
                resources.getString(R.string.edit_text_error_alert, "Jenis pekerjaan")
            return
        }

        if (TextUtils.isEmpty(
                activityAddEditExperienceBinding.etCompanyName.text.toString().trim()
            )
        ) {
            activityAddEditExperienceBinding.etCompanyName.error =
                resources.getString(R.string.edit_text_error_alert, "Nama perusahaan")
            return
        }

        if (TextUtils.isEmpty(
                activityAddEditExperienceBinding.etLocation.text.toString().trim()
            )
        ) {
            activityAddEditExperienceBinding.etLocation.error =
                resources.getString(R.string.edit_text_error_alert, "Lokasi")
            return
        }

        if (TextUtils.isEmpty(
                activityAddEditExperienceBinding.etStartDate.text.toString().trim()
            )
        ) {
            activityAddEditExperienceBinding.etStartDate.error =
                resources.getString(R.string.edit_text_error_alert, "Tanggal mulai")
            return
        }

        if (TextUtils.isEmpty(activityAddEditExperienceBinding.etEndDate.text.toString().trim())) {
            activityAddEditExperienceBinding.etEndDate.error =
                resources.getString(R.string.edit_text_error_alert, "Tanggal selesai")
            return
        }

        if (activityAddEditExperienceBinding.tvInvalidStartDate.visibility == View.VISIBLE
            || activityAddEditExperienceBinding.tvInvalidEndDate.visibility == View.VISIBLE
        ) {
            return
        }

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_loading)
        dialog.setCancelable(false)
        dialog.show()

        submitExperience()
    }

    private fun startEndDateListener(view: EditText) {
        //Set default values
        val calendar = Calendar.getInstance()
        val yearSelected = calendar[Calendar.YEAR]
        val monthSelected = calendar[Calendar.MONTH]

        val dialogFragment = MonthYearPickerDialogFragment
            .getInstance(monthSelected, yearSelected)

        dialogFragment.show(supportFragmentManager, null)

        dialogFragment.setOnDateSetListener { year, monthOfYear ->
            if (view == activityAddEditExperienceBinding.etStartDate) {
                startMonth = monthOfYear + 1
                startYear = year

                if (endMonth != 0 || endYear != 0) {

                    activityAddEditExperienceBinding.tvInvalidStartDate.visibility =
                        View.GONE

                    activityAddEditExperienceBinding.tvInvalidEndDate.visibility =
                        View.GONE

                    if (year > endYear) {
                        activityAddEditExperienceBinding.tvInvalidStartDate.visibility =
                            View.VISIBLE
                    } else if (year == endYear) {
                        if ((monthOfYear + 1) > endMonth) {
                            activityAddEditExperienceBinding.tvInvalidStartDate.visibility =
                                View.VISIBLE
                        } else {
                            activityAddEditExperienceBinding.tvInvalidStartDate.visibility =
                                View.GONE
                        }
                    } else {
                        activityAddEditExperienceBinding.tvInvalidStartDate.visibility =
                            View.GONE

                        activityAddEditExperienceBinding.tvInvalidEndDate.visibility =
                            View.GONE
                    }
                }

            } else if (view == activityAddEditExperienceBinding.etEndDate) {
                endMonth = monthOfYear + 1
                endYear = year

                if (startMonth != 0 || startYear != 0) {

                    activityAddEditExperienceBinding.tvInvalidStartDate.visibility =
                        View.GONE

                    activityAddEditExperienceBinding.tvInvalidEndDate.visibility =
                        View.GONE

                    if (year < startYear) {
                        activityAddEditExperienceBinding.tvInvalidEndDate.visibility =
                            View.VISIBLE
                    } else if (year == startYear) {
                        if ((monthOfYear + 1) < startMonth) {
                            activityAddEditExperienceBinding.tvInvalidEndDate.visibility =
                                View.VISIBLE
                        } else {
                            activityAddEditExperienceBinding.tvInvalidEndDate.visibility =
                                View.GONE
                        }
                    } else {
                        activityAddEditExperienceBinding.tvInvalidStartDate.visibility =
                            View.GONE

                        activityAddEditExperienceBinding.tvInvalidEndDate.visibility =
                            View.GONE
                    }
                }
            }

            val month = DateFormatSymbols().months[monthOfYear]
            view.setText(
                resources.getString(
                    R.string.txt_start_end_date_value,
                    month.toString(),
                    year.toString()
                )
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.top_toolbar_delete_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.deleteMenu -> {
//                dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                dialog.titleText = resources.getString(R.string.txt_education_delete_alert)
//                dialog.confirmText = resources.getString(R.string.txt_delete)
//                dialog.cancelText = resources.getString(R.string.txt_cancel)
//                dialog.setCancelable(false)
//                dialog.showCancelButton(true)
//                dialog.setConfirmClickListener {
//                    viewModel.deleteApplicantEducation(educationId, this)
//                }.setCancelClickListener {
//                    it.dismissWithAnimation()
//                }
//                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.etEmploymentType -> startActivityForResult(
                Intent(
                    this,
                    EmploymentTypeActivity::class.java
                ), EmploymentTypeActivity.REQUEST_EMPLOYMENT
            )
            R.id.etStartDate -> startEndDateListener(activityAddEditExperienceBinding.etStartDate)
            R.id.etEndDate -> startEndDateListener(activityAddEditExperienceBinding.etEndDate)
            R.id.btnSubmitExperience -> formValidation()
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            activityAddEditExperienceBinding.etEndDate.isEnabled = false
            activityAddEditExperienceBinding.etEndDate.setText(resources.getString(R.string.txt_currently_working))
            endMonth = 0
            endYear = 0

            activityAddEditExperienceBinding.tvInvalidStartDate.visibility =
                View.GONE

            activityAddEditExperienceBinding.tvInvalidEndDate.visibility =
                View.GONE
        } else {
            activityAddEditExperienceBinding.etEndDate.isEnabled = true
            activityAddEditExperienceBinding.etEndDate.setText("")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EmploymentTypeActivity.REQUEST_EMPLOYMENT) {
            if (resultCode == EmploymentTypeActivity.RESULT_EMPLOYMENT) {
                activityAddEditExperienceBinding.etEmploymentType.setText(
                    data?.getStringExtra(
                        EmploymentTypeActivity.SELECTED_EMPLOYMENT
                    )
                )
            }
        }
    }

    override fun onSuccessAdding() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.tv_success_adding_experience)
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            finish()
        }
        dialog.show()
    }

    override fun onFailureAdding(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId, "Pengalaman kerja")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
    }

    override fun onSuccessUpdate() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_success_update, "Pengalaman kerja")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            finish()
        }
        dialog.show()
    }

    override fun onFailureUpdate(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId, "Pengalaman kerja")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
    }
}