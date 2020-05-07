package com.libs.core.view

import android.app.Dialog
import android.content.Context
import com.libs.core.R

/**
 *
 */
sealed class DialogInput
sealed class DialogOutput

interface DialogAction {
    /**
     * Should be call when action triggered.
     * @param act If positive action happen.
     * @param output The [DialogOutput] data
     */
    fun onAction(act: Boolean = true, output: DialogOutput?)
}

/**
 * Abstract super class [Dialog]
 * @constructor AbsDialog(context)
 */
abstract class AbsDialog(val ctx: Context) {

    protected var input : DialogInput?  = null
    protected var output: DialogOutput? = null
    protected var action: DialogAction? = null

    var dialog: Dialog? = null
        protected set

    protected abstract fun getLayoutId(): Int
    protected abstract fun handleLayout()

    protected open fun create() {
        dialog = Dialog(ctx, R.style.DialogTheme).apply {
            setContentView(getLayoutId())
            //setCancelable(false)
        }
    }

    /**
     * Display a dialog.
     */
    open fun show() {
        if (null == dialog) {
            build()
        } else {
            dialog?.show()
        }
    }

    /**
     * Create a dialog with specify layout, then display.
     */
    open fun build() {
        create()
        handleLayout()

        show()
    }

    /**
     * Dismiss.
     */
    open fun dismiss() {
        dialog?.dismiss()
    }

    /**
     * Destroy.
     */
    open fun destroy() {
        dismiss()
        dialog = null
    }
}


