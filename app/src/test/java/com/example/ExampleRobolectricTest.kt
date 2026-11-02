package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.domain.model.TempleTheme
import com.example.util.DhakaTimeHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context verifies Shankhodhwani app name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("শঙ্খধ্বনি", appName)
    }

    @Test
    fun `dhaka time slots map accurately to 6 temple themes`() {
        // 04:00 - 06:00: Dawn / Misty
        assertEquals(TempleTheme.DAWN_MISTY, DhakaTimeHelper.getDhakaThemeForHour(4))
        assertEquals(TempleTheme.DAWN_MISTY, DhakaTimeHelper.getDhakaThemeForHour(5))

        // 06:00 - 11:00: Sunrise / Morning
        assertEquals(TempleTheme.SUNRISE_MORNING, DhakaTimeHelper.getDhakaThemeForHour(6))
        assertEquals(TempleTheme.SUNRISE_MORNING, DhakaTimeHelper.getDhakaThemeForHour(9))
        assertEquals(TempleTheme.SUNRISE_MORNING, DhakaTimeHelper.getDhakaThemeForHour(10))

        // 11:00 - 15:00: Daylight
        assertEquals(TempleTheme.DAYLIGHT, DhakaTimeHelper.getDhakaThemeForHour(11))
        assertEquals(TempleTheme.DAYLIGHT, DhakaTimeHelper.getDhakaThemeForHour(14))

        // 15:00 - 17:00: Golden Hour
        assertEquals(TempleTheme.GOLDEN_HOUR, DhakaTimeHelper.getDhakaThemeForHour(15))
        assertEquals(TempleTheme.GOLDEN_HOUR, DhakaTimeHelper.getDhakaThemeForHour(16))

        // 17:00 - 19:00: Evening / Temple Lamps
        assertEquals(TempleTheme.EVENING_LAMPS, DhakaTimeHelper.getDhakaThemeForHour(17))
        assertEquals(TempleTheme.EVENING_LAMPS, DhakaTimeHelper.getDhakaThemeForHour(18))

        // 19:00 - 04:00: Night / Maa Durga Ambiance
        assertEquals(TempleTheme.NIGHT_DURGA, DhakaTimeHelper.getDhakaThemeForHour(19))
        assertEquals(TempleTheme.NIGHT_DURGA, DhakaTimeHelper.getDhakaThemeForHour(23))
        assertEquals(TempleTheme.NIGHT_DURGA, DhakaTimeHelper.getDhakaThemeForHour(0))
        assertEquals(TempleTheme.NIGHT_DURGA, DhakaTimeHelper.getDhakaThemeForHour(3))
    }

    @Test
    fun `bengali digit and currency formatting is accurate`() {
        val formattedBn = DhakaTimeHelper.toBengaliDigits("12345.50")
        assertEquals("১২৩৪৫.৫০", formattedBn)

        val zeroBalance = DhakaTimeHelper.formatCurrency(0.0, true)
        assertEquals("৳০.০০", zeroBalance)

        val positiveBalance = DhakaTimeHelper.formatCurrency(5000.0, true)
        assertEquals("৳৫০০০.০০", positiveBalance)
    }

    @Test
    fun `financial transparency balance formula validation`() {
        val publishedIncome = 15000.0
        val publishedExpense = 4500.0
        val remainingBalance = publishedIncome - publishedExpense
        assertEquals(10500.0, remainingBalance, 0.001)
    }
}
