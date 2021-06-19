package org.d3ifcool.dissajobapplicant.utils.dummy

import org.d3ifcool.dissajobapplicant.data.source.local.entity.experience.ExperienceEntity

object ExperienceDummy {
    fun generateExperienceData(): List<ExperienceEntity> {
        val experiences = ArrayList<ExperienceEntity>()
        experiences.add(
            ExperienceEntity(
                "-Mb2kLsBeyiKPt9cs8Qm",
                "Software Engineer",
                "Purnawaktu",
                "Proclub Telkom University",
                "Jl. Telekomunikasi no. 10",
                1,
                2019,
                0,
                0,
                "-",
                true,
                "3g3HKA3gakeEgodIcfOJ3XPhcAB3"
            )
        )

        return experiences
    }
}