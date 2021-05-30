package org.d3ifcool.dissajobapplicant.data.source.local.entity.media

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "files")
data class FileEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "file_byte_array", typeAffinity = ColumnInfo.BLOB)
    var fileByteArray: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileEntity

        if (id != other.id) return false
        if (!fileByteArray.contentEquals(other.fileByteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + fileByteArray.contentHashCode()
        return result
    }
}