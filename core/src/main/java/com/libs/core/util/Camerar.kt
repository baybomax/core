package com.libs.core.util

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.libs.core.R
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Camerar utility to take photo & pick album.
 */
object Camerar {

    const val TAKE_PICTURE_FROM_GALLERY = 100
    const val TAKE_PICTURE_FROM_CAMERA  = 200
    const val TAKE_PICTURE_FROM_CROP    = 300
}

/**
 * Take photo
 *
 * @param act The activity
 * @return The file of photo
 */
fun Camerar.openCamera(act: Activity, file: File) {
    // open camera

    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
        if (null != it.resolveActivity(act.packageManager)) {
            // 1). get uri
            val uri: Uri = Filer.getUri(file, act)

            // 2). put extra
            it.putExtra(MediaStore.EXTRA_OUTPUT, uri)

            // 3). start activity
            act.startActivityForResult(it, TAKE_PICTURE_FROM_CAMERA)
        } else {
            Toaster.showToast(act, R.string.camera_open_error)
        }
    }
}

/**
 * Pick gallery
 *
 * @param act The activity
 */
fun Camerar.openGallery(act: Activity) {
    // open gallery

    // 1). config intent
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"

    // 2). start activity
    act.startActivityForResult(intent, TAKE_PICTURE_FROM_GALLERY)
}

/**
 * Crop the pic
 *
 * @param act The activity
 * @param uri The pic uri
 * @return The cropped path
 */
fun Camerar.cropPhoto(act: Activity, uri: Uri) = Intent("com.android.camera.action.CROP").run {
    // open crop

    // 1). add read & write permission
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION )
    }

    // 2). setting crop
    setDataAndType(uri, "image/*")

    putExtra("crop"           , "true")
    putExtra("aspectX"        , 1     )
    putExtra("aspectY"        , 1     )
    putExtra("return-data"    , true  )
    putExtra("scale"          , true  )
    putExtra("scaleUpIfNeeded", true  )

    putExtra("outputX"        , 256)
    putExtra("outputY"        , 256)

    putExtra("outputFormat"   , Bitmap.CompressFormat.PNG)

    act.startActivityForResult(this, TAKE_PICTURE_FROM_CROP)
}

/**
 * Different phone take a picture with different degree, so do rotate to this picture
 *
 * @param path rotate file path which need
 * @return true/false
 */
fun Camerar.rotateIfNeed(path: String): Boolean = try {
    var degree = 0

    @Suppress("MoveVariableDeclarationIntoWhen")
    val orientation = ExifInterface(path)
        .getAttributeInt(
            ExifInterface.TAG_ORIENTATION   ,
            ExifInterface.ORIENTATION_NORMAL
        )

    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90  -> degree = 90
        ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
        ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
    }

    if (degree != 0) {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        var bitmap = BitmapFactory.decodeFile(path, options)

        val width = options.outWidth
        val height = options.outHeight

        val hh = 720.0f
        val ww = 720.0f
        var be = 1

        if (width > height && width > ww) {
            be = (width / ww).toInt() + 1
        }
        if (height > width && height > hh) {
            be = (height / hh).toInt() + 1
        }
        if (be <= 0) {
            be = 1
        }

        options.inSampleSize = be
        options.inJustDecodeBounds = false

        bitmap = BitmapFactory.decodeFile(path, options)

        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())

        val resizedBitmap = Bitmap
            .createBitmap(
                bitmap,
                0,
                0,
                bitmap.width ,
                bitmap.height,
                matrix,
                true
            )

        saveBitmap2File(resizedBitmap, path, 100)
    }

    true
} catch (e: IOException) {
    e.printStackTrace()
    false
}

/***
 * Save bitmap to file
 *
 * @param bm bitmap
 * @param path file path
 * @param quality quality of picture
 */
fun Camerar.saveBitmap2File(bm: Bitmap?, path: String, quality: Int) {
    if (null == bm || bm.isRecycled) return

    try {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
        val bos = BufferedOutputStream(
            FileOutputStream(file)
        )
        bm.compress(Bitmap.CompressFormat.PNG, quality, bos)
        bos.flush()
        bos.close()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (!bm.isRecycled) {
            bm.recycle()
        }
    }
}

