package org.d3ifcool.dissajobapplicant.utils.dummy

import org.d3ifcool.dissajobapplicant.data.source.local.entity.interview.InterviewEntity

object InterviewDummy {
    fun generateInterviewData(): InterviewEntity {
        return InterviewEntity(
            "-MbklxWzBgSk9MHdTC-T",
            "-MbklxQlRxjUnNpducxi",
            "MiGz2j0NTyTJXxsAP8uLyIOv8QN2",
            "Saya pekerja keras",
            "Saya tertarik dengan perusahaan bapak",
            "Saya memiliki pengalaman di bidang IOS selama 5 tahun"
        )
    }
}