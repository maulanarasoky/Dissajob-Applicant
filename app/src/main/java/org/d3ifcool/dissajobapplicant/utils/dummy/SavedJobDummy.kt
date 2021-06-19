package org.d3ifcool.dissajobapplicant.utils.dummy

import org.d3ifcool.dissajobapplicant.data.source.local.entity.job.SavedJobEntity

object SavedJobDummy {
    fun generateSavedJobsData(): List<SavedJobEntity> {
        val savedJob = ArrayList<SavedJobEntity>()
        savedJob.add(
            SavedJobEntity(
                "-MbkmH6lkMN7gWobRBvq",
                "-MaToCvLrfqh9fc595kh",
                "MiGz2j0NTyTJXxsAP8uLyIOv8QN2"
            )
        )
        return savedJob
    }
}