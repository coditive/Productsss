package com.example.productsss.productadd

import android.app.Service
import android.icu.text.DecimalFormat
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.google.android.material.R.id.snackbar_text
import com.google.android.material.snackbar.Snackbar


fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            }
        })
        (view.findViewById<View?>(snackbar_text) as? TextView?)?.setSingleLine(false)
        show()
    }
}


fun View.showKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun TextView.truncateAfterTwoPrecisionDigits(value: String) {
    val decimalFormat = DecimalFormat("0.00")
    val truncatedValue = decimalFormat.format(value.toDouble())
    this.text = truncatedValue
}