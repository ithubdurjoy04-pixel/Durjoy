package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.domain.model.TempleTheme

private fun createColorSchemeForTheme(theme: TempleTheme) = when (theme) {
    TempleTheme.DAWN_MISTY -> lightColorScheme(
        primary = DawnPrimary,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFF3E8FF),
        onPrimaryContainer = DawnPrimary,
        secondary = DawnAccent,
        background = DawnBackground,
        onBackground = Color(0xFF1E1B2E),
        surface = DawnSurface,
        onSurface = Color(0xFF1E1B2E),
        surfaceVariant = Color(0xFFEDE9FE),
        outline = Color(0xFFCBD5E1)
    )
    TempleTheme.SUNRISE_MORNING -> lightColorScheme(
        primary = SunrisePrimary,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFFEE2E2),
        onPrimaryContainer = SunrisePrimary,
        secondary = SunriseAccent,
        background = SunriseBackground,
        onBackground = Color(0xFF29151B),
        surface = SunriseSurface,
        onSurface = Color(0xFF29151B),
        surfaceVariant = Color(0xFFFEF3C7),
        outline = Color(0xFFE2E8F0)
    )
    TempleTheme.DAYLIGHT -> lightColorScheme(
        primary = DaylightPrimary,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFFDF2F4),
        onPrimaryContainer = DaylightPrimary,
        secondary = DaylightAccent,
        background = DaylightBackground,
        onBackground = Color(0xFF1C1917),
        surface = DaylightSurface,
        onSurface = Color(0xFF1C1917),
        surfaceVariant = Color(0xFFF5EBE6),
        outline = Color(0xFFE7E5E4)
    )
    TempleTheme.GOLDEN_HOUR -> lightColorScheme(
        primary = GoldenHourPrimary,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFFFEDD5),
        onPrimaryContainer = GoldenHourPrimary,
        secondary = GoldenHourAccent,
        background = GoldenHourBackground,
        onBackground = Color(0xFF2A1810),
        surface = GoldenHourSurface,
        onSurface = Color(0xFF2A1810),
        surfaceVariant = Color(0xFFFED7AA),
        outline = Color(0xFFE5E7EB)
    )
    TempleTheme.EVENING_LAMPS -> darkColorScheme(
        primary = EveningAccent,
        onPrimary = Color(0xFF20070B),
        primaryContainer = EveningSurface,
        onPrimaryContainer = EveningAccent,
        secondary = EveningPrimary,
        background = EveningBackground,
        onBackground = Color(0xFFFFF1F2),
        surface = EveningSurface,
        onSurface = Color(0xFFFFF1F2),
        surfaceVariant = Color(0xFF4A101A),
        outline = Color(0xFF881337)
    )
    TempleTheme.NIGHT_DURGA -> darkColorScheme(
        primary = NightDurgaAccent,
        onPrimary = Color(0xFF070B19),
        primaryContainer = Color(0xFF1E293B),
        onPrimaryContainer = NightDurgaAccent,
        secondary = Color(0xFF38BDF8),
        background = NightDurgaBackground,
        onBackground = Color(0xFFF8FAFC),
        surface = NightDurgaSurface,
        onSurface = Color(0xFFF8FAFC),
        surfaceVariant = Color(0xFF1E293B),
        outline = NightDurgaBorder
    )
}

@Composable
fun ShankhodhwaniTheme(
    activeTheme: TempleTheme = TempleTheme.DAYLIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = createColorSchemeForTheme(activeTheme)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                val isDark = activeTheme == TempleTheme.EVENING_LAMPS || activeTheme == TempleTheme.NIGHT_DURGA
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
