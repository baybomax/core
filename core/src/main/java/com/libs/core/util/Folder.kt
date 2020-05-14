package com.libs.core.util

import android.annotation.SuppressLint
import android.content.ContextWrapper
import android.net.Uri
import com.libs.core.extents.slashes
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility class to handle file system related folders.
 */
object Folder {

    @SuppressLint("SimpleDateFormat")
    private val FILENAME_FORMAT = SimpleDateFormat("yyyy-MM-dd_HHmmss")

    /**
     * Return an Uri instance pointing to a file in /raw/ folder.
     *
     * @param context The context.
     * @param filename The filename.
     * @return The Uri.
     */
    fun getRawUri(context: ContextWrapper, filename: String): Uri {
        return Uri.parse("android.resource://" + context.packageName + "/raw/" + filename)
    }

    fun generatePath(root: String, prefix: String, baseFile: File, suffix: String): String {
        return generatePath(root, prefix, baseFile.name, suffix)
    }

    fun generatePath(root: String, prefix: String, date: Date, suffix: String): String {
        return generatePath(root, prefix, FILENAME_FORMAT.format(date), suffix)
    }

    fun generatePath(root: String, prefix: String, baseName: String, suffix: String): String {
        val filename = prefix + baseName + suffix
        val path = (root + filename).slashes()
        File(path).parentFile.mkdirs()
        return path
    }

    /**
     * Recursively delete a file or directory.
     *
     * @param fileOrDirectory The file or directory to delete.
     */
    private fun deleteRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory && null != fileOrDirectory.listFiles())
            for (child in fileOrDirectory.listFiles())
                deleteRecursive(child)
        fileOrDirectory.delete()
    }

    /**
     * Remove the contents of given directory; and make sure the directory contains a file
     *
     * named ".no"
     * @param dirname The directory to clean.
     */
    fun cleanDir(dirname: String) {
        val d = File(dirname)
        deleteRecursive(d)
        d.mkdirs()
        val n = File(d, ".no")
        try {
            n.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}


