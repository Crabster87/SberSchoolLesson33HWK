package crabster.rudakov.sberschoollesson33hwk.utils

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackBarReceiver {

    fun show(view: View, message: String) {
        Snackbar
            .make(view, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(Color.YELLOW)
            .setTextColor(Color.RED)
            .show()
    }

}