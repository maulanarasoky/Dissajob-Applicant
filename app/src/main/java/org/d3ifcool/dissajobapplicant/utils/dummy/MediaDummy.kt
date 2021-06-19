package org.d3ifcool.dissajobapplicant.utils.dummy

import org.d3ifcool.dissajobapplicant.data.source.local.entity.media.MediaEntity

object MediaDummy {
    fun generateMediaData(): List<MediaEntity> {
        val media = ArrayList<MediaEntity>()
        media.add(
            MediaEntity(
                "-MbZdc6AMdW8kMN_OogO",
                "CV (1).pdf",
                "Ini adalah deskripsi",
                "LnxCzv0t4dQVBXV4mAt2diT2gGZ2",
                "-MbZdaxRKSw497hgBWOe"
            )
        )

        return media
    }
}