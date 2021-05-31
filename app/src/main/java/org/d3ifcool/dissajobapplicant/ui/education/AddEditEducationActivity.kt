package org.d3ifcool.dissajobapplicant.ui.education

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.education.EducationResponseEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityAddEditEducationBinding
import org.d3ifcool.dissajobapplicant.ui.education.callback.AddEducationCallback
import org.d3ifcool.dissajobapplicant.ui.education.callback.DeleteEducationCallback
import org.d3ifcool.dissajobapplicant.ui.education.callback.UpdateEducationCallback
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import java.text.DateFormatSymbols
import java.util.*


class AddEditEducationActivity : AppCompatActivity(), View.OnClickListener, AddEducationCallback,
    UpdateEducationCallback, DeleteEducationCallback {

    companion object {
        const val EDUCATION_DATA = "education_data"
    }

    private lateinit var activityAddEditEducationBinding: ActivityAddEditEducationBinding

    private lateinit var viewModel: EducationViewModel

    private lateinit var dialog: SweetAlertDialog

    private var startMonth = 0
    private var startYear = 0
    private var endMonth = 0
    private var endYear = 0

    private var isEdit = false

    private lateinit var educationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddEditEducationBinding = ActivityAddEditEducationBinding.inflate(layoutInflater)
        setContentView(activityAddEditEducationBinding.root)

        activityAddEditEducationBinding.toolbar.title =
            resources.getString(R.string.txt_educational_background)
        setSupportActionBar(activityAddEditEducationBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[EducationViewModel::class.java]

        val oldEducationData = intent.getParcelableExtra<EducationEntity>(EDUCATION_DATA)
        if (oldEducationData != null) {
            showOldEducationData(oldEducationData)
            activityAddEditEducationBinding.btnAddEducation.text =
                resources.getString(R.string.txt_update)
        }

        activityAddEditEducationBinding.etEducationLevel.setOnClickListener(this)
        activityAddEditEducationBinding.etStartDate.setOnClickListener(this)
        activityAddEditEducationBinding.etEndDate.setOnClickListener(this)
        activityAddEditEducationBinding.btnAddEducation.setOnClickListener(this)
    }

    private fun showOldEducationData(education: EducationEntity) {
        activityAddEditEducationBinding.etSchoolName.setText(education.schoolName.toString())
        activityAddEditEducationBinding.etEducationLevel.setText(education.educationLevel.toString())
        activityAddEditEducationBinding.etFieldOfStudy.setText(education.fieldOfStudy.toString())

        val startMonth = DateFormatSymbols().months[education.startMonth - 1]
        val endMonth = DateFormatSymbols().months[education.endMonth - 1]
        val startDate = "$startMonth ${education.startYear}"
        val endDate = "$endMonth ${education.endYear}"

        activityAddEditEducationBinding.etStartDate.setText(startDate)
        activityAddEditEducationBinding.etEndDate.setText(endDate)
        if (education.description != "-") {
            activityAddEditEducationBinding.etEducationDescription.setText(education.description.toString())
        }

        this.educationId = education.id
        this.startMonth = education.startMonth
        this.startYear = education.startYear
        this.endMonth = education.endMonth
        this.endYear = education.endYear
        isEdit = true
    }

    private fun submitEducation() {
        val schoolName = activityAddEditEducationBinding.etSchoolName.text.toString().trim()
        val educationDegree =
            activityAddEditEducationBinding.etEducationLevel.text.toString().trim()
        val fieldOfStudy = activityAddEditEducationBinding.etFieldOfStudy.text.toString().trim()
        var educationDescription =
            activityAddEditEducationBinding.etEducationDescription.text.toString().trim()

        if (TextUtils.isEmpty(educationDescription)) {
            educationDescription = "-"
        }

        val education = EducationResponseEntity(
            "",
            schoolName,
            educationDegree,
            fieldOfStudy,
            startMonth,
            startYear,
            endMonth,
            endYear,
            educationDescription,
            ""
        )

        if (!isEdit) {
            viewModel.addApplicantEducation(education, this)
        } else {
            val oldEducationData = intent.getParcelableExtra<EducationEntity>(EDUCATION_DATA)
            education.id = oldEducationData?.id.toString()
            education.applicantId = oldEducationData?.applicantId.toString()
            viewModel.updateApplicantEducation(education, this)
        }
    }

    private fun formValidation() {
        if (TextUtils.isEmpty(
                activityAddEditEducationBinding.etSchoolName.text.toString().trim()
            )
        ) {
            activityAddEditEducationBinding.etSchoolName.error =
                resources.getString(R.string.edit_text_error_alert, "Nama sekolah")
            return
        }

        if (TextUtils.isEmpty(
                activityAddEditEducationBinding.etEducationLevel.text.toString().trim()
            )
        ) {
            activityAddEditEducationBinding.etEducationLevel.error =
                resources.getString(R.string.edit_text_error_alert, "Tingkat pendidikan")
            return
        }

        if (TextUtils.isEmpty(
                activityAddEditEducationBinding.etFieldOfStudy.text.toString().trim()
            )
        ) {
            activityAddEditEducationBinding.etFieldOfStudy.error =
                resources.getString(R.string.edit_text_error_alert, "Jurusan")
            return
        }

        if (TextUtils.isEmpty(activityAddEditEducationBinding.etStartDate.text.toString().trim())) {
            activityAddEditEducationBinding.etStartDate.error =
                resources.getString(R.string.edit_text_error_alert, "Tanggal mulai")
            return
        }

        if (TextUtils.isEmpty(activityAddEditEducationBinding.etEndDate.text.toString().trim())) {
            activityAddEditEducationBinding.etEndDate.error =
                resources.getString(R.string.edit_text_error_alert, "Tanggal selesai")
            return
        }

        dialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_loading)
        dialog.setCancelable(false)
        dialog.show()

        submitEducation()
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
            if (view == activityAddEditEducationBinding.etStartDate) {
                startMonth = monthOfYear + 1
                startYear = year

                if (endMonth != 0 || endYear != 0) {
                    if (year > endYear) {
                        activityAddEditEducationBinding.tvInvalidStartDate.visibility = View.VISIBLE
                    } else {
                        if ((monthOfYear + 1) > endMonth) {
                            activityAddEditEducationBinding.tvInvalidStartDate.visibility =
                                View.VISIBLE
                        } else {
                            activityAddEditEducationBinding.tvInvalidStartDate.visibility =
                                View.GONE
                        }
                    }
                }

            } else if (view == activityAddEditEducationBinding.etEndDate) {
                endMonth = monthOfYear + 1
                endYear = year

                if (startMonth != 0 || startYear != 0) {
                    if (year < startYear) {
                        activityAddEditEducationBinding.tvInvalidEndDate.visibility =
                            View.VISIBLE
                    } else {
                        if ((monthOfYear + 1) > startMonth) {
                            activityAddEditEducationBinding.tvInvalidEndDate.visibility =
                                View.VISIBLE
                        } else {
                            activityAddEditEducationBinding.tvInvalidEndDate.visibility =
                                View.GONE
                        }
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
                dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                dialog.titleText = resources.getString(R.string.txt_education_delete_alert)
                dialog.confirmText = resources.getString(R.string.txt_delete)
                dialog.cancelText = resources.getString(R.string.txt_cancel)
                dialog.setCancelable(false)
                dialog.showCancelButton(true)
                dialog.setConfirmClickListener {
                    viewModel.deleteApplicantEducation(educationId, this)
                }.setCancelClickListener {
                    it.dismissWithAnimation()
                }
                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.etEducationLevel -> startActivityForResult(
                Intent(
                    this,
                    EducationLevelActivity::class.java
                ), EducationLevelActivity.REQUEST_DEGREE
            )
            R.id.etStartDate -> startEndDateListener(activityAddEditEducationBinding.etStartDate)
            R.id.etEndDate -> startEndDateListener(activityAddEditEducationBinding.etEndDate)
            R.id.btnAddEducation -> formValidation()
        }
    }

    override fun onSuccessAdding() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.tv_success_adding_education)
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            finish()
        }
        dialog.show()
    }

    override fun onFailureAdding(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId, "Riwayat pendidikan")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EducationLevelActivity.REQUEST_DEGREE) {
            if (resultCode == EducationLevelActivity.RESULT_DEGREE) {
                activityAddEditEducationBinding.etEducationLevel.setText(
                    data?.getStringExtra(
                        EducationLevelActivity.SELECTED_DEGREE
                    )
                )
            }
        }
    }

    override fun onSuccessUpdate() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_success_update, "Riwayat pendidikan")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            finish()
        }
        dialog.show()
    }

    override fun onFailureUpdate(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId, "Riwayat pendidikan")
        dialog.setCancelable(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
    }

    override fun onSuccessDelete() {
        dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.txt_success_delete, "Riwayat pendidikan")
        dialog.confirmText = resources.getString(R.string.dialog_ok)
        dialog.setCancelable(false)
        dialog.showCancelButton(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
            finish()
        }
        dialog.show()
    }

    override fun onFailureDelete(messageId: Int) {
        dialog.changeAlertType(SweetAlertDialog.WARNING_TYPE)
        dialog.titleText = resources.getString(messageId, "Riwayat pendidikan")
        dialog.confirmText = resources.getString(R.string.dialog_ok)
        dialog.setCancelable(false)
        dialog.showCancelButton(false)
        dialog.setConfirmClickListener {
            it.dismissWithAnimation()
        }
        dialog.show()
    }
}