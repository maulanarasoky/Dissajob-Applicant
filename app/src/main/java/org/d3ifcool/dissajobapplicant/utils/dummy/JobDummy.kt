package org.d3ifcool.dissajobapplicant.utils.dummy

import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobDetailsEntity
import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.JobEntity

object JobDummy {
    fun generateJobsData(): List<JobEntity> {
        val jobs = ArrayList<JobEntity>()
        jobs.add(
            JobEntity(
                "-MaToCvLrfqh9fc595kh",
                "Mobile Developer - IOS",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
                "RT7zlmPyqte9uhi6W9zoNh3i88E2",
                "2021-05-24 21:39:19",
                isOpen = true,
                isOpenForDisability = false
            )
        )
        return jobs
    }

    fun generateJobDetails(): JobDetailsEntity {
        return JobDetailsEntity(
            "-MaToCvLrfqh9fc595kh",
            "Mobile Developer - IOS",
            "Lorem ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            "Jl. Telekomunikasi Jl. Terusan Buah Batu, Sukapura, Kec. Dayeuhkolot, Kota Bandung, Jawa Barat 40257",
            "Tidak ada",
            "Contract",
            "Remote",
            "Marketing & Advertising",
            "Tidak ditentukan",
            "RT7zlmPyqte9uhi6W9zoNh3i88E2",
            "2021-05-24 21:39:19",
            "2021-05-24 21:39:19",
            "-",
            isOpen = true,
            isOpenForDisability = false,
            "-"
        )
    }
}