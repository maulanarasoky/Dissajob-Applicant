package org.d3ifcool.dissajobapplicant.data.source.repository.cv

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import org.d3ifcool.dissajobapplicant.data.source.local.entity.cv.CvEntity
import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.cv.CvResponseEntity
import org.d3ifcool.dissajobapplicant.ui.cv.AddCvCallback
import org.d3ifcool.dissajobapplicant.ui.profile.callback.UploadFileCallback
import org.d3ifcool.dissajobapplicant.vo.Resource

interface CvDataSource {
    fun getApplicantCv(applicantId: String): LiveData<Resource<PagedList<CvEntity>>>
    fun uploadCv(file: Uri, callback: UploadFileCallback)
    fun addCv(cvData: CvResponseEntity, callback: AddCvCallback)
}