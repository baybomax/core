package com.libs.core.view

import android.app.Activity
import me.leefeng.promptlibrary.Builder
import me.leefeng.promptlibrary.PromptButton
import me.leefeng.promptlibrary.PromptDialog

/**
 * Prompt Window.
 */
class Prompt(private val activity: Activity,
             private val builder : Builder? = null) {

    private var prompt: PromptDialog? = null

    /**
     * Initial
     */
    init {
        build()
    }

    /**
     * Build
     */
    private fun build() {
        prompt = builder?.let {
            PromptDialog(it, activity)
        } ?: PromptDialog(activity)
    }

    /**
     * Check
     */
    private fun check() {
        if (null == prompt) {
            build()
        }
    }

    /**
     * Show custom
     */
    fun showCustom(icon: Int, res: String, withAnim: Boolean = true) {
        check()
        prompt?.showCustom(icon, res, withAnim)
    }

    /**
     * Show loading
     */
    fun showLoading(res: String, withAnim: Boolean = true) {
        check()
        prompt?.showLoading(res, withAnim)
    }

    /**
     * Show loading with delay
     */
    fun showLoadingWithDelay(res: String, time: Long) {
        check()
        prompt?.showLoadingWithDelay(res, time)
    }

    /**
     * Show custom loading
     */
    fun showCustomLoading(load_logo: Int, res: String) {
        check()
        prompt?.showCustomLoading(load_logo, res)
    }

    /**
     * Show custom loading with delay
     */
    fun showCustomLoadingWithDelay(load_logo: Int, res: String, time: Long) {
        check()
        prompt?.showCustomerLoadingWithDelay(load_logo, res, time)
    }

    /**
     * Show success
     */
    fun showSuccess(res: String, withAnim: Boolean = true) {
        check()
        prompt?.showSuccess(res, withAnim)
    }

    /**
     * Show success delay
     */
    fun showSuccess(res: String, time: Long) {
        check()
        prompt?.showSuccessDelay(res, time)
    }

    /**
     * Show error
     */
    fun showError(res: String, withAnim: Boolean = true) {
        check()
        prompt?.showError(res, withAnim)
    }

    /**
     * Show info
     */
    fun showInfo(res: String, withAnim: Boolean = true) {
        check()
        prompt?.showInfo(res, withAnim)
    }

    /**
     * Show warn
     */
    fun showWarn(res: String, withAnim: Boolean = true) {
        check()
        prompt?.showWarn(res, withAnim)
    }

    /**
     * Show alert sheet
     */
    fun showAlertSheet(title: String, withAnim: Boolean = true, vararg buttons: PromptButton) {
        check()
        prompt?.showAlertSheet(title, withAnim, *buttons)
    }

    /**
     * Show warn alert
     */
    fun showWarnAlert(text: String, button: PromptButton, withAnim: Boolean = true) {
        check()
        prompt?.showWarnAlert(text, button, withAnim)
    }

    /**
     * Show warn alert
     */
    fun showWarnAlert(text: String, button1: PromptButton, button2: PromptButton, withAnim: Boolean = true) {
        check()
        prompt?.showWarnAlert(text, button1, button2, withAnim)
    }

    /**
     * Dismiss
     */
    fun dismiss() {
        prompt?.dismiss()
    }

    /**
     * Dismiss immediately
     */
    fun dismissImmediately() {
        prompt?.dismissImmediately()
    }

    /**
     * Destroy
     */
    fun destroy() {
        dismiss()
        prompt = null
    }

    /**
     * Destroy immediately
     */
    fun destroyImmediately() {
        dismissImmediately()
        prompt = null
    }

    /**
     * On back pressed.
     */
    fun onBackPressed() = prompt?.onBackPressed() ?: true

}

