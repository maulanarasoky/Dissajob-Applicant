package org.d3ifcool.dissajobapplicant.utils.dummy

import org.d3ifcool.dissajobapplicant.data.source.local.entity.application.ApplicationEntity

object ApplicationDummy {
    fun generateApplicationsData(): List<ApplicationEntity> {
        val applications = ArrayList<ApplicationEntity>()
        applications.add(
            ApplicationEntity(
                "-MbklxQlRxjUnNpducxi",
                "MiGz2j0NTyTJXxsAP8uLyIOv8QN2",
                "-MaToCvLrfqh9fc595kh",
                "2021-06-09 19:38:18",
                "2021-06-14 10:13:02",
                "Accepted",
                false,
                "jtXzHFCNlTfNkgMeBE7014kLnZq2"
            )
        )

        return applications
    }

    fun generateAcceptedApplicationsData(): List<ApplicationEntity> {
        val applications = ArrayList<ApplicationEntity>()
        applications.add(
            ApplicationEntity(
                "-MbklxQlRxjUnNpducxi",
                "MiGz2j0NTyTJXxsAP8uLyIOv8QN2",
                "-MaToCvLrfqh9fc595kh",
                "2021-06-09 19:38:18",
                "2021-06-14 10:13:02",
                "Accepted",
                false,
                "jtXzHFCNlTfNkgMeBE7014kLnZq2"
            )
        )

        return applications
    }

    fun generateRejectedApplicationsData(): List<ApplicationEntity> {
        val applications = ArrayList<ApplicationEntity>()
        applications.add(
            ApplicationEntity(
                "-McFEh8qqIoxXs8RQ_2X",
                "3g3HKA3gakeEgodIcfOJ3XPhcAB3",
                "-McFBprY0XskiHnOEizq",
                "2021-06-15 22:16:28",
                "2021-06-15 22:23:57",
                "Rejected",
                false,
                "j2Q9mq2eeHRMiPl9cbvWfymo95v2"
            )
        )

        return applications
    }

    fun generateMarkedApplicationsData(): List<ApplicationEntity> {
        val applications = ArrayList<ApplicationEntity>()
        applications.add(
            ApplicationEntity(
                "-McIbqTvNs2-X8aQBUet",
                "ZNTAt3NLdMP5ScSVyWgVBuon4aw1",
                "-McI_LmBqEQ9s014gzqX",
                "2021-06-16 14:00:50",
                "2021-06-16 14:01:26",
                "Accepted",
                true,
                "Uf8fFnrwiEQky0RoyNI8jiFDYsj1"
            )
        )

        return applications
    }
}