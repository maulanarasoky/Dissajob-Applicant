package org.d3ifcool.dissajobapplicant.utils.dummy

import org.d3ifcool.dissajobapplicant.data.source.local.entity.education.EducationEntity

object EducationDummy {
    fun generateEducationData(): List<EducationEntity> {
        val educations = ArrayList<EducationEntity>()
        educations.add(
            EducationEntity(
                "-Mb1mBQLRsOzNWLVYX4s",
                "Telkom University",
                "D3",
                "Rekayasa Perangkat Lunak Aplikasi",
                8,
                2018,
                8,
                2021,
                "Bentar lagi wisuda euyy",
                "3g3HKA3gakeEgodIcfOJ3XPhcAB3"
            )
        )

        return educations
    }
}