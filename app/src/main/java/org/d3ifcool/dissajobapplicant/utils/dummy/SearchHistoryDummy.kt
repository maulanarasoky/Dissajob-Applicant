package org.d3ifcool.dissajobapplicant.utils.dummy

import org.d3ifcool.dissajobapplicant.data.source.local.entity.history.SearchHistoryEntity

object SearchHistoryDummy {
    fun generateSearchHistoriesData(): List<SearchHistoryEntity> {
        val searchHistories = ArrayList<SearchHistoryEntity>()
        searchHistories.add(
            SearchHistoryEntity(
                "-MaU-Ds6DDTFkbrLE1uX",
                "ios",
                "2021-05-24 23:40:02",
                "MiGz2j0NTyTJXxsAP8uLyIOv8QN2"
            )
        )
        return searchHistories
    }
}