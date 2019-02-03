package learning.shadow.com.airvedaassignment.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TextView


fun View.gone() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun convertDpToPx(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}

fun showSnack(isConnected: Boolean, view: View) {
    val message: String
    val color: Int
    if (isConnected) {
        message = "Good! Connected to Internet"
        color = Color.WHITE
    } else {
        message = "Sorry! Not connected to internet"
        color = Color.RED
    }

    val snackbar = Snackbar
        .make(view, message, Snackbar.LENGTH_LONG)

    val sbView = snackbar.view
    val textView = sbView.findViewById(android.support.design.R.id.snackbar_text) as TextView
    textView.setTextColor(color)
    snackbar.show()
}

fun Activity.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}