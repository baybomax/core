package com.libs.core.util

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.libs.core.BuildConfig

import java.io.*
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Utility class of file system.
 * @see java.io.File
 */
object Filer {

    /**
     * Copy a file content to another file.
     *
     * @param src Source file
     * @param dst Destination file.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun copy(src: File, dst: File) {
        val parent = dst.parentFile
        parent.mkdirs()

        val `in` = FileInputStream(src)
        val out = FileOutputStream(dst)

        // Transfer bytes from in to out
        val buf = ByteArray(1024 * 100)
        var len = `in`.read(buf)
        while (len > 0) {
            out.write(buf, 0, len)
            len = `in`.read(buf)
        }

        `in`.close()
        out.close()
    }

    /**
     * Plus specify file name end of the dir path.
     *
     * @param dirPath The path of directory.
     * @param filename The file name.
     * @return The absolute file path.
     */
    fun dirPlusFilename(dirPath: String, filename: String): String =
        if (!dirPath.endsWith(File.separator)) {
            dirPath + File.separator + filename
        } else
            dirPath + filename

    /**
     * Return the base name of given file path.
     * @param path The path of file.
     */
    fun basename(path: String): String = run {
        val idx = path.lastIndexOf(File.separator)

        return if (idx >= 0)
            path.substring(idx)
        else
            path
    }

    /**
     * Copy a asset file content to another file.
     *
     * @param context The context.
     * @param filename The filename inside the assets.
     * @param dst The destination file.
     */
    @Throws(IOException::class)
    fun copyAssetFile(context: Context, filename: String, dst: File) {
        val afd = context.assets.openFd(filename)
        val `in` = afd.createInputStream()
        val out = FileOutputStream(dst)

        val buf = ByteArray(1024 * 100)
        var len = `in`.read(buf)
        while (len > 0) {
            out.write(buf, 0, len)
            len = `in`.read(buf)
        }

        `in`.close()
        out.close()
    }

    /**
     *
     * @param path Path to the file.
     * @return content of the file, null if error.
     */
    fun readEntireFile(path: String): String? =
        try {
            val fis = FileInputStream(java.io.File(path))
            val ret = convertStreamToString(fis)

            //Make sure you close all streams.
            fis.close()

            ret
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

    /**
     * Read entire file from asset.
     *
     * @param context Context instance.
     * @param path path to the file in assets.
     * @return content of the file, null if error.
     */
    fun readEntireFile(context: Context, path: String): String? =
        try {
            val afd = context.assets.openFd(path)
            val fis = afd.createInputStream()
            val ret = convertStreamToString(fis)

            fis.close()

            ret
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

    /**
     * Write specify content string to specify path file.
     * @param path The file path.
     * @param content The content.
     * @return True if content successfully written to file, false otherwise.
     */
    fun writeToFile(path: String, content: String): Boolean =
        try {
            val fos = FileOutputStream(File(path))
            fos.write(content.toByteArray())

            fos.close()

            true
        } catch (e: IOException) {
            false
        }

    /**
     * Convert a stream content to string.
     *
     * @param is The input stream.
     * @return The content string.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun convertStreamToString(`is`: InputStream): String = run {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()

        var line = reader.readLine()
        while (line != null) {
            sb.append(line).append("\n")
            line = reader.readLine()
        }

        reader.close()

        sb.toString()
    }

    /**
     * So that adding "ext" to file path "a/b/cde.fg" will because "a/b/cde.ext.fg".
     *
     * @param path The original path
     * @param part The part to be appended at the end of the filename, just before the extension.
     * @return The new file path.
     */
    fun addExtension(path: String, part: String): String = run {
        val li = path.lastIndexOf('.')

        return if (li < 0) {
            "$path.$part"
        } else
            path.substring(0, li) + '.'.toString() + part + path.substring(li)

        // abc . def
        // 012 3 456
    }

    /**
     * Get extension name of specify file path.
     * @param path The file path.
     * @return The extension name.
     */
    fun getExtension(path: String?): String? = run {
        val i = path?.lastIndexOf('.') ?: -1

        return if (i > 0)
            path?.substring(i + 1)
        else
            null
    }

    /**
     * Replace the extension part of the file path to `newExt`
     *
     * @param path The file path to replace
     * @param newExt The new extension part
     * @return a new filepath with new extension.
     */
    fun replaceExtension(path: String, newExt: String): String = run {
        val li = path.lastIndexOf('.')

        return if (li < 0) {
            "$path.$newExt"
        } else
            path.substring(0, li) + '.'.toString() + newExt

    }

    /**
     * Return the file name without the extension
     * @param fileName the file name
     */
    fun getNameWithoutExt(fileName: String?): String {
        val i = fileName?.lastIndexOf(".") ?: -1

        return if (i > 0)
            fileName?.substring(0, i) ?: ""
        else
            ""
    }

    /**
     * Return the uri of specified file
     * @param file the file
     */
    fun getUri(file: File, act: Activity): Uri = run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            FileProvider.getUriForFile(
                act,
                "${BuildConfig.APPLICATION_ID}.provider",
                file
            )
        else
            Uri.fromFile(file)
    }

    /**
     * Return the md5 string of specified file
     * @param file the file
     */
    fun getMd5(file: File): String? {
        try {
            if (!file.exists()) return null

            val digest = MessageDigest.getInstance("MD5")
            val randomAccessFile = RandomAccessFile(file, "r")

            val bytes = ByteArray(1024 * 1024 * 10)
            var len = randomAccessFile.read(bytes)

            while (len != -1) {
                digest.update(bytes, 0, len)
                len = randomAccessFile.read(bytes)
            }

            val bigInteger = BigInteger(1, digest.digest())
            var md5 = bigInteger.toString(16)
            while (md5.length < 32) {
                md5 = "0$md5"
            }

            return md5
        } catch (e: Exception) {
            return null
        }
    }

}



