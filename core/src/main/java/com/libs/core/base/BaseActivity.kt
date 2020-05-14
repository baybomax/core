package com.libs.core.base

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.libs.core.R
import com.libs.core.rx.RxBusPassenger
import com.libs.core.util.Loger
import com.libs.core.view.Prompt

@SuppressLint("Registered")
open class BaseActivity: AppCompatActivity(), View.OnClickListener {

    // ===================
    // Variable properties
    // ===================

    open val eventDefines: RxBusPassenger = RxBusPassenger()

    open val debug: Boolean = true

    var prompt: Prompt? = null
    var logger: Loger? = null

    // ===================
    // Lifecycle functions
    // ===================

    /**
     * Same as [.onCreate] but called for those activities created with
     * the attribute [android.R.attr.persistableMode] set to
     * `persistAcrossReboots`.
     *
     * @param savedInstanceState if the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in [.onSaveInstanceState].
     * ***Note: Otherwise it is null.***
     *
     * @see .onCreate
     * @see .onStart
     * @see .onSaveInstanceState
     * @see .onRestoreInstanceState
     * @see .onPostCreate
     */
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        // 1.Events bus on board.
        eventDefines.onboardBus()

        // 2.Initial prompt instance.
        prompt = Prompt(this)

        // 3.Initial log(er) instance.
        logger = Loger(javaClass.simpleName, debug)
    }

    /**
     * Destroy all fragments and loaders.
     */
    override fun onDestroy() {
        // 1.Events bus leave.
        eventDefines.leaveBus()

        // 2.Destroy prompt instance.
        prompt?.destroy()

        // 3.Destroy log(er) instance.
        logger = null

        super.onDestroy()
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     *
     * **WARNING:** Do NOT call this function directly.
     */
    override fun onBackPressed() {
        val onBackPressed = prompt?.onBackPressed() == true

        if (onBackPressedImpl() && onBackPressed) {
            super.onBackPressed()
        }
    }

    /**
     * Children classes should override this method to implement functionality of back key press.
     * @return true if it should call super.onBackPressed(); false will ignore this press action.
     */
    protected open fun onBackPressedImpl(): Boolean {
        return true
    }

    override fun onClick(v: View?) {
    }
}

/**
 * Permissions (Activity)
 */
@SuppressLint("Registered")
open class PermissionActivity : BaseActivity() {

    protected open var permissionsRequested = listOf<String>()

    private var permissionsRequestCode = 123
    private var permissionsGranted: (() -> Unit)? = null
    private val permissionsNotGranted = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logger?.TAG = "${this.javaClass.simpleName} Permission: "
    }
    @Synchronized
    protected fun checkPermissions() {
        permissionsNotGranted.clear()
        permissionsNotGranted.addAll(permissionsRequested.filter {
            PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, it)
        })
        requestPermissions()
    }

    @Synchronized
    protected fun checkPermissions(permissions: List<String>,
                                   permissionsGranted: (() -> Unit)? = null) {
        permissionsRequested    = permissions
        this.permissionsGranted = permissionsGranted

        checkPermissions()
    }

    protected open fun showRequestPermissionDialog() {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setMessage(R.string.please_grant_permissions)
            .setTitle(R.string.permissions)
            .setIcon(applicationInfo.loadIcon(packageManager))
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                permissionsGranted()
            }
            .setPositiveButton(R.string.grant_again) { _, _ ->
                requestPermissions()
            }
            .show()
    }

    private fun requestPermissions() {
        if (permissionsNotGranted.size > 0) {
            val arr = permissionsNotGranted.toTypedArray()
            logger?.w("requesting: $permissionsNotGranted")
            logger?.w("requesting: array length: ${arr.size}")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arr,
                    permissionsRequestCode
                )
            } else {
                permissionsGranted()
            }
        } else {
            permissionsGranted()
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on [.requestPermissions].
     *
     *
     * **Note:** It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     *
     *
     * @param requestCode The request code passed in [.requestPermissions].
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     * which is either [android.content.pm.PackageManager.PERMISSION_GRANTED]
     * or [android.content.pm.PackageManager.PERMISSION_DENIED]. Never null.
     *
     * @see .requestPermissions
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (permissionsRequestCode != requestCode) {
            return
        }
        for ((i, p) in grantResults.withIndex()) {
            if (PackageManager.PERMISSION_GRANTED == p) {
                permissionsNotGranted.remove(permissions[i])
            }
        }

        if (permissionsNotGranted.size == 0) {
            permissionsGranted()
        } else {
            logger?.w("permissions: ")
            permissions.forEach {
                logger?.w("    $it")
            }
            logger?.w("grantResults: ")
            grantResults.forEach {
                logger?.w("    $it")
            }
            logger?.w("Following permissions not granted! $permissionsNotGranted")
            showRequestPermissionDialog()
        }
    }

    open fun permissionsGranted() {
        permissionsGranted?.invoke()
    }
}

