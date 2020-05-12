package com.libs.core.view

import android.app.Dialog
import android.content.Context
import com.libs.core.R

/**
 *
 */
sealed class DialogInput
sealed class DialogOutput
sealed class DIIgnore: DialogInput()
sealed class DOIgnore: DialogOutput()

interface DialogAction {
    /**
     * Should be call when action triggered.
     * @param act If positive action happen.
     * @param output The [DialogOutput] data
     */
    fun <O: DialogOutput> onAction(act: Boolean = true, output: O?)
}

/**
 * Abstract super class [Dialog]
 * @constructor AbsDialog(context)
 *
 * How to use:
 *  1.instance in onCreated()
 *  2.setAction() & build() in onCreated()
 *  3.show() when used
 *  4.dismiss() when unused
 */
abstract class AbsDialog<I: DialogInput, O: DialogOutput>(val ctx: Context) {

    protected var input : I?  = null
    protected var output: O?  = null
    var action: DialogAction? = null

    var dialog: Dialog? = null
        protected set

    protected abstract fun getLayoutId(): Int

    /**
     * Only do initial/setEvents
     */
    protected abstract fun handleLayout()

    protected open fun create() {
        dialog = Dialog(ctx, R.style.DialogTheme).apply {
            setContentView(getLayoutId())
            //setCancelable(false)
        }
    }

    /**
     * Should overwrite if there are data need display
     */
    protected open fun display() {
    }

    /**
     * Display a dialog.
     * @param input The input data
     */
    open fun show(input: I? = null) {
        this.input = input
        display()
        dialog?.show()
    }

    /**
     * Create a dialog with specify layout, then display.
     */
    open fun build() {
        create()
        handleLayout()
    }

    /**
     * Dismiss.
     */
    open fun dismiss() {
        dialog?.dismiss()
    }

}

//class TestDialog(ctx: Context): AbsDialog<DIIgnore, DOIgnore>(ctx){
//    override fun getLayoutId(): Int { return 0 }
//    override fun handleLayout() {}
//    override fun display() { super.display() }
//}

