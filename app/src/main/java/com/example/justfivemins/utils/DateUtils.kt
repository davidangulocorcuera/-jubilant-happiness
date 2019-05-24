package com.example.justfivemins.utils

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList



object DateUtils {
    val DATE_FORMAT_SERVER = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val DATE_FORMAT_USER = "dd MMMM yyyy"
    val DATE_FORMAT = "dd-MM-yyyy"
    val DATE_FORMAT_TRAVEL_DAY = "dd"
    val DATE_FORMAT_TRAVEL_MONTH = "MMMM"
    val DATE_FORMAT_TRAVEL_YEAR = "yyyy"
    private var formatOrigin: String? = null
    var formatTarget: String? = null
    private var dateStr: String? = null

    init {
        formatOrigin = DATE_FORMAT_SERVER
        formatTarget = DATE_FORMAT_SERVER
        dateStr = null
    }

    fun formatTarget(formatTarget: String): DateUtils {
        this.formatTarget = formatTarget
        return this
    }

    fun formatOrigin(formatOrigin: String): DateUtils {
        this.formatOrigin = formatOrigin
        return this
    }

    fun formatDate(calendar: Calendar?): String? {
        var dateFormatted: String? = null
        if (calendar != null) {
            try {
                val formatter = SimpleDateFormat(formatTarget, Locale.getDefault())
                calendar.add(Calendar.DATE, 1)
                dateFormatted = formatter.format(calendar.time)
                Log.v("taag",dateFormatted)


            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return dateFormatted
    }




    fun getDates(dateStart: String, dateEnd: String): List<Date> {
        val dates = ArrayList<Date>()
        val df1 = SimpleDateFormat(DATE_FORMAT_SERVER)

        var date1: Date? = null
        var date2: Date? = null

        try {
            date1 = df1.parse(dateStart)
            date2 = df1.parse(dateEnd)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val cal1 = Calendar.getInstance()
        cal1.setTime(date1)

        val cal2 = Calendar.getInstance()
        cal2.setTime(date2)

        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime())
            cal1.add(Calendar.DATE, 1)
        }
        return dates
    }


}