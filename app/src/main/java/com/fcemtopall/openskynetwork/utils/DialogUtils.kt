package com.fcemtopall.openskynetwork.utils

import android.app.AlertDialog
import android.content.Context

class DialogUtils {

    companion object {

        fun showAlert(
            context: Context,
            title: Int,
            message: Int,
            buttonText: Int,
            buttonAction : () -> Unit
        ): AlertDialog {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle(title)
            alertDialogBuilder.setMessage(message)
            alertDialogBuilder.setPositiveButton(buttonText) { dialog, _ ->
                buttonAction.invoke()
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
            return alertDialog
        }
    }
}