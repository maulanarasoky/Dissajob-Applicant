package org.d3ifcool.dissajobapplicant.utils.dummy

import org.d3ifcool.dissajobapplicant.data.source.local.entity.applicant.ApplicantEntity

object ApplicantDummy {
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