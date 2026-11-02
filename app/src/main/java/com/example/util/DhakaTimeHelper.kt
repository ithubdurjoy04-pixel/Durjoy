package com.example.util

import com.example.domain.model.TempleTheme
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DhakaTimeHelper {
    val DHAKA_TIMEZONE: TimeZone = TimeZone.getTimeZone("Asia/Dhaka")

    fun getCurrentDhakaCalendar(): Calendar {
        val calendar = Calendar.getInstance(DHAKA_TIMEZONE)
        return calendar
    }

    fun getDhakaThemeForHour(hour24: Int): TempleTheme {
        return when (hour24) {
            in 4..5 -> TempleTheme.DAWN_MISTY
            in 6..10 -> TempleTheme.SUNRISE_MORNING
            in 11..14 -> TempleTheme.DAYLIGHT
            in 15..16 -> TempleTheme.GOLDEN_HOUR
            in 17..18 -> TempleTheme.EVENING_LAMPS
            else -> TempleTheme.NIGHT_DURGA // 19..23 and 0..3
        }
    }

    fun getCurrentDhakaTheme(): TempleTheme {
        val hour = getCurrentDhakaCalendar().get(Calendar.HOUR_OF_DAY)
        return getDhakaThemeForHour(hour)
    }

    fun getFormattedDhakaTime(isBengali: Boolean): String {
        val date = Date()
        val format = SimpleDateFormat("hh:mm a", Locale.US)
        format.timeZone = DHAKA_TIMEZONE
        val timeString = format.format(date)
        return if (isBengali) toBengaliDigits(timeString) else timeString
    }

    fun getFormattedDhakaDate(isBengali: Boolean): String {
        val date = Date()
        val format = SimpleDateFormat("EEEE, d MMMM yyyy", if (isBengali) Locale("bn", "BD") else Locale.US)
        format.timeZone = DHAKA_TIMEZONE
        val dateString = format.format(date)
        return if (isBengali) toBengaliDigits(dateString) else dateString
    }

    fun formatCurrency(amount: Double, isBengali: Boolean): String {
        val rounded = String.format(Locale.US, "%.2f", amount)
        val formatted = if (isBengali) toBengaliDigits(rounded) else rounded
        return "৳$formatted"
    }

    fun toBengaliDigits(input: String): String {
        val bengaliDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        val sb = StringBuilder()
        for (char in input) {
            if (char in '0'..'9') {
                sb.append(bengaliDigits[char - '0'])
            } else {
                sb.append(char)
            }
        }
        return sb.toString()
    }
}
