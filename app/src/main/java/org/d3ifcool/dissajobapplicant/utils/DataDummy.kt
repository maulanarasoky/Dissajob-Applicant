package org.d3ifcool.dissajobapplicant.utils

import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.recruiter.RecruiterEntity

object DataDummy {
    fun generateJobDetails(): JobDetailsEntity {
        val job = JobDetailsEntity(
            "-MZ3qcbNLLpA5aGKnhbb",
            "Mobile Developer - Android",
            "Lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            "Jl. Sukapura Gg. Banteng no. 1033",
            "Tidak ada",
            "Full-time",
            "Remote",
            "Marketing & Advertising",
            "Tidak ditentukan",
            "RT7zlmPyqte9uhi6W9zoNh3i88E2",
            "2021-04-25 00:08:21",
            "2021-04-25 00:08:21",
            "-",
            true
        )

        return job
    }

    fun generateRecruiterDetails(): RecruiterEntity {
        val recruiter = RecruiterEntity(
            "RT7zlmPyqte9uhi6W9zoNh3i88E2",
            "Telkom",
            "University",
            "Telkom University",
            "hahaboi02@gmail.com",
            "Jl. Telekomunikasi No 01",
            "-",
            "HRD",
            "-MZ42Vx55oXuKRp2pPL-"
        )

        return recruiter
    }

    fun generateApplicantDetails(): ApplicantEntity {
        val applicant = ApplicantEntity(
            "MiGz2j0NTyTJXxsAP8uLyIOv8QN2",
            "Steve",
            "Richard",
            "Steve Richard",
            "lanaeuylana@gmail.com",
            "-",
            "123456789",
            "-MZEBFJbmCo9FGaLLbQv"
        )

        return applicant
    }
}