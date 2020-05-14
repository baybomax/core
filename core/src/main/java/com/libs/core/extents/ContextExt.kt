package com.libs.core.extents

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.core.os.ConfigurationCompat
import com.libs.core.util.Toaster

fun Context.showToast(resId: Int         ) = Toaster.showToast(this, resId)
fun Context.showToast(text : CharSequence) = Toaster.showToast(this, text )

/**
 * Convenient function to run on specify context thread.
 *
 * @param callback
 */
fun Context.runOnContextThread(callback: (()->Unit)?) = callback?.let {
    when (this) {
        is Activity -> runOnUiThread(it)
        else -> it.invoke()
    }
}

/**
 * Convenient function to check if application network is connected.
 *
 * @return True if network works, false otherwise.
 */
fun Context.isNetworkConnected(): Boolean = run {
    (applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?)
        ?.activeNetworkInfo != null
}

/**
 * Convenient function to check if application location is enabled.
 *
 * @return True if location enabled, false otherwise.
 */
fun Context.isLocationEnabled(): Boolean = run {
    (applicationContext.getSystemService(Context.LOCATION_SERVICE) as? LocationManager?)
        ?.isProviderEnabled(LocationManager.GPS_PROVIDER)
        ?: false
}

/**
 * Convenient function to get locale.
 */
fun Context.locale() =
    try {
        ConfigurationCompat.getLocales(resources.configuration)[0]
    } catch (e: Exception) {
        null
    }

/**
 * Convenient function to get locale language.
 */
fun Context.acceptLan() = locale()?.language ?: "en"

/**
 * Convenient function to goto login screen.
 */
fun Context.gotoLogin(clazz: Class<*>) = gotoNextAffinity(clazz)

/**
 * Convenient function to goto signUp screen.
 */
fun Context.gotoSignUp(clazz: Class<*>) = gotoNextAffinity(clazz)

/**
 * Convenient function to start activity.
 * NOTE: goto next screen.
 *
 * @param clazz The activity javaClass.
 * @param block The block with intent.
 * @see Activity.startActivity
 * @see Intent
 */
fun Context.gotoNext(clazz: Class<*>, block: ((Intent) -> Unit)? = null) {
    startActivity(
        Intent(this, clazz)
            .also {
                block?.invoke(it)
            }
    )
}

/**
 * Convenient function to start activity.
 * NOTE: This way shall clear task stack then start a new activity.
 *
 * @param clazz The activity javaClass.
 * @param block The block with intent.
 * @see Activity.startActivity
 * @see Intent
 */
@SuppressLint("ObsoleteSdkInt")
fun Context.gotoNextAffinity(clazz: Class<*>, block: ((Intent) -> Unit)? = null) {
    // 1. Whether sdk version > JELLY_BEAN
    val jellyBean= Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN

    // 2. start activity
    startActivity(
        Intent(this, clazz)
            .apply {
                if (!jellyBean) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK   or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                block?.invoke(this)
            }
    )

    // 3. finish
    if (this !is Activity) return

    if (jellyBean)
        finishAffinity()
    else
        finish()
}

/**
 * Convenient function to start activity for result.
 * NOTE: goto next screen with extras.
 *
 * @param clazz The activity javaClass
 * @param block The block with intent
 * @see Activity.startActivityForResult
 * @see Intent
 */
fun Context.gotoNextForResult(clazz: Class<*>, requestCode: Int, block: ((Intent) -> Unit)? = null) {
    if (this !is Activity) return

    startActivityForResult(
        Intent(this, clazz)
            .also {
                block?.invoke(it)
            },
        requestCode
    )
}

/**
 * Convenient function to back to last activity with result.
 * NOTE: back to last screen with extras.
 *
 * @param block The block with intent
 * @see Activity.setResult
 */
fun Context.backLastWithResult(block: ((Intent) -> Unit)?) {
    if (this !is Activity) return

    setResult(
        Activity.RESULT_OK,
        Intent().also {
            block?.invoke(it)
        }
    )

    finish()
}

/**
 * Convenient function to start browser with specified url
 * @param url the url to open
 */
fun Context.startBrowser(url: String) {
    startActivity(
        Intent().apply {
            action = Intent.ACTION_VIEW // "android.intent.action.VIEW"
            data   = Uri.parse(url)
        }
    )
}

/**
 * Convenient function to scan media 'video/mp4'
 * @param path a vararg path array
 */
fun Context.scanMediaMp4(vararg path: String?) = try {
    MediaScannerConnection
        .scanFile(
            this,
            path,
            arrayOf("video/mp4"),
            null
        )
} catch (e: Exception) {
}

/**
 * Convenient function to share something.
 * @param title the title of share view
 * @param setIntent set the intent parameters
 */
fun Context.share(title: String, setIntent: (Intent) -> Unit) {
    val intent = Intent(Intent.ACTION_SEND)
        .also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            setIntent(it)
        }

    // start send
    startActivity(Intent.createChooser(intent, title))
}

/**
 * Convenient function to share text
 * @param title the title of share view
 * @param text the text content to share
 */
fun Context.shareText(title: String, text: String) = share(title) {
    it.type = "text/plain"
    it.putExtra(Intent.EXTRA_TEXT, text)
}
