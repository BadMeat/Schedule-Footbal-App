package com.example.user.sceball

import android.content.Context
import android.net.ConnectivityManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Bencoleng on 12/11/2018.
 */
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.GONE
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}


fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun isNetworkAvailable(context: Context?): Boolean {

    val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    if (activeNetwork != null) {
        // connected to the internet
        if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
            return true
        } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
            return true
        }
    }
    return false
}

fun convertSimpleData(curDate: String?): String {
    if (curDate != null) {
        val myDates = curDate.split("-".toRegex())
        val myMoon: String
        val tempMoon = myDates[1]
        myMoon = when {
            tempMoon.equals("1", true) -> "Jan"
            tempMoon.equals("2", true) -> "Feb"
            tempMoon.equals("3", true) -> "Mar"
            tempMoon.equals("4", true) -> "Apr"
            tempMoon.equals("5", true) -> "May"
            tempMoon.equals("6", true) -> "Jun"
            tempMoon.equals("7", true) -> "Jul"
            tempMoon.equals("8", true) -> "Aug"
            tempMoon.equals("9", true) -> "Sep"
            tempMoon.equals("10", true) -> "Oct"
            tempMoon.equals("11", true) -> "Nov"
            else -> "Dec"
        }
        return myDates[2] + " " + myMoon + " " + myDates[0]
    }
    return ""
}

fun convertDate(curDate: String?, fullDate: String?): String {
    if (curDate != null && fullDate != null) {
        val tempDate = myDate(fullDate)
        val myDates = curDate.split("-".toRegex())
        val myMoon: String
        val tempMoon = myDates[1]
        val w = SimpleDateFormat("EEEE", Locale.getDefault())
        val myDay = w.format(tempDate).substring(0, 3)
        myMoon = when {
            tempMoon.equals("1", true) -> "Jan"
            tempMoon.equals("2", true) -> "Feb"
            tempMoon.equals("3", true) -> "Mar"
            tempMoon.equals("4", true) -> "Apr"
            tempMoon.equals("5", true) -> "May"
            tempMoon.equals("6", true) -> "Jun"
            tempMoon.equals("7", true) -> "Jul"
            tempMoon.equals("8", true) -> "Aug"
            tempMoon.equals("9", true) -> "Sep"
            tempMoon.equals("10", true) -> "Oct"
            tempMoon.equals("11", true) -> "Nov"
            else -> "Dec"
        }
        return myDay + ", " + myDates[2] + " " + myMoon + " " + myDates[0]
    }
    return ""
}

fun myDate(curDate: String?): Date {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT+7")
    return simpleDateFormat.parse(curDate)
}

fun convertGMT(curDate: String?): String {
    val w = myDate(curDate)
    val calendar = Calendar.getInstance()
    calendar.time = w
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minutes = String.format("%02d", calendar.get(Calendar.MINUTE))
    return "$hour : $minutes"
}

