package org.d3ifcool.dissajobapplicant.ui.cv

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.cv.CvEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.cv.CvResponseEntity
import org.d3ifcool.dissajobapplicant.data.source.repository.cv.CvRepository
import org.d3ifcool.dissajobapplicant.ui.cv.callback.AddCvCallback
import org.d3ifcool.dissajobapplicant.ui.cv.callback.LoadCvFileCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

class CvViewModel(private val cvRepository: CvRepository) : ViewModel() {
    fun getApplicantCv(applicantId: String): LiveData<Resource<PagedList<CvEntity>>> =
        cvRepository.getApplicantCv(applicantId)

    fun uploadCv(
        cv: Uri,
        callback: UploadFileCallback
    ) = cvRepository.uploadCv(cv, callback)

    fun addCv(
        cvData: CvResponseEntity,
        callback: AddCvCallback
    ) = cvRepository.addCv(cvData, callback)

    fun getCvById(
        cvId: String,
        callback: LoadCvFileCallback
    ) = cvRepository.getCvById(cvId, callback)
}