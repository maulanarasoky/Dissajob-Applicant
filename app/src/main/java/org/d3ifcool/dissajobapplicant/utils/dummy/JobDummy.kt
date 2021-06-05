package org.d3ifcool.dissajobapplicant.utils.dummy

import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobDetailsEntity

object JobDummy {
    fun generateJobDetails(): JobDetailsEntity {
        val job = JobDetailsEntity(
            "-MaTo4UtbI8qFCPuxA4j",
            "Mobile Developer - Android",
            "Lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            "Jl. Telekomunikasi Jl. Terusan Buah Batu, Sukapura, Kec. Dayeuhkolot, Kota Bandung, Jawa Barat 40257",
            "Tidak ada",
            "Full-time",
            "Onsite",
            "Marketing & Advertising",
            "Tidak ditentukan",
            "jtXzHFCNlTfNkgMeBE7014kLnZq2",
            "2021-05-24 21:38:44",
            "2021-05-24 21:38:44",
            "-",
            isOpen = true,
            isOpenForDisability = true,
            ""
        )

        return job
    }
}