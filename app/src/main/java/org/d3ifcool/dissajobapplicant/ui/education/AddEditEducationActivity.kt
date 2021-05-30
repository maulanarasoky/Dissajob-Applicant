package org.d3ifcool.dissajobapplicant.ui.education

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment
import org.d3ifcool.dissajobapplicant.R
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.education.EducationResponseEntity
import org.d3ifcool.dissajobapplicant.databinding.ActivityAddEditEducationBinding
import org.d3ifcool.dissajobapplicant.ui.viewmodel.ViewModelFactory
import java.text.DateFormatSymbols
import java.util.*


class AddEditEducationActivity : AppCompatActivity(), View.OnClickListener, AddEducationCallback {

    private lateinit var activityAddEditEducationBinding: ActivityAddEditEducationBinding

    private lateinit var viewModel: EducationViewModel

    private lateinit var dialog: SweetAlertDialog

    private var startMonth = 0
    private var startYear = 0
    private var endMonth = 0
    private var endYear = 0

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

        activityAddEditEducationBinding.etEducationLevel.setOnClickListener(this)
        activityAddEditEducationBinding.etStartDate.setOnClickListener(this)
        activityAddEditEducationBinding.etEndDate.setOnClickListener(this)
        activityAddEditEducationBinding.btnAddEducation.setOnClickListener(this)
    }

    private fun addEducation() {
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

        viewModel.addApplicantEducation(education, this)
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

        addEducation()
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
            R.id.etEducationLevel -> startActivityForResult(
                Intent(
                    this,
                    EducationDegreeActivity::class.java
                ), EducationDegreeActivity.REQUEST_DEGREE
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
        if (requestCode == EducationDegreeActivity.REQUEST_DEGREE) {
            if (resultCode == EducationDegreeActivity.RESULT_DEGREE) {
                activityAddEditEducationBinding.etEducationLevel.setText(
                    data?.getStringExtra(
                        EducationDegreeActivity.SELECTED_DEGREE
                    )
                )
            }
        }
    }
}