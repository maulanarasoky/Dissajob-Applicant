package org.d3ifcool.dissajobapplicant.utils

import android.content.Context
import java.io.*

object FileUtils {
    @Throws(IOException::class)
    fun copy(inputStream: InputStream?, output: File?) {
        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(output)
            var read = 0
            val bytes = ByteArray(1024)
            while (inputStream!!.read(bytes).also { read = it } != -1) {
                outputStream.write(bytes, 0, read)
            }
        } finally {
            try {
                inputStream?.close()
            } finally {
                outputStream?.close()
            }
        }
    }

    @Throws(IOException::class)
    fun downloadFile(context: Context, assetName: String, filePath: String, fileName: String?) {

        val dirPath = "${context.filesDir}/${filePath}"
        val outFile = File(dirPath)
        //Create New File if not present
        if (!outFile.exists()) {
            outFile.mkdirs()
        }
        val outFile1 = File(dirPath, "/$fileName.pdf")
        copy(context.assets.open(assetName), outFile1)
    }
}