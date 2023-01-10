package com.techwonders.doday.utility

import android.text.format.DateFormat
import java.util.*

@JvmField
val REQUEST_CODE_NOTE_ENTRY = 101
val REQUEST_CODE_DELIVERY_ENTRY = 102
val REQUEST_CODE_TRANSACTION = 103

val BREAKFAST_AMOUNT = "breakfast_amount"
val LUNCH_AMOUNT = "lunch_amount"
val DINNER_AMOUNT = "dinner_amount"


const val DATE_FORMAT: String = "dd/MM/yyyy"
fun getDateFromTimestamp(timestamp: String?): String {
    val ts = timestamp?.toLong()
    if (ts == null || ts <= 0) return ""
    //Get instance of calendar
    val calendar = Calendar.getInstance(Locale.getDefault())
    //get current date from ts
    calendar.timeInMillis = ts
    //return formatted date
    return DateFormat.format(DATE_FORMAT, calendar).toString()
}

fun isDateSameDay(oldTimestamp: String, newTimestamp: String) =
    (getDateFromTimestamp(oldTimestamp) == getDateFromTimestamp(newTimestamp))
