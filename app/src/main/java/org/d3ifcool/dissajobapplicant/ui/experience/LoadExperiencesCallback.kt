package org.d3ifcool.dissajobapplicant.ui.experience

import org.d3ifcool.dissajobapplicant.data.source.remote.response.entity.experience.ExperienceResponseEntity

interface LoadExperiencesCallback {
    fun onAllExperiencesReceived(experienceResponse: List<ExperienceResponseEntity>): List<ExperienceResponseEntity>
}