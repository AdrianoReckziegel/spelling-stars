package com.spellingstars.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = primaryDark,
  primaryContainer = primaryContainerDark,
  secondary = secondaryDark,
  tertiary = tertiaryDark
)

private val LightColorScheme = lightColorScheme(
  primary = StarPurple,
  onPrimary = Color.White,
  primaryContainer = StarPurpleContainer,
  onPrimaryContainer = OnStarPurpleContainer,
  secondary = SkyBlue,
  onSecondary = Color.White,
  secondaryContainer = SkyBlueContainer,
  onSecondaryContainer = OnSkyBlueContainer,
  tertiary = CoralPink,
  onTertiary = Color.White,
  tertiaryContainer = CoralPinkContainer,
  onTertiaryContainer = OnCoralPinkContainer,
  background = WarmCreamBackground,
  onBackground = Color(0xFF1E1A2B),
  surface = CardSurfaceLight,
  onSurface = Color(0xFF1E1A2B),
  surfaceVariant = Color(0xFFF3EEFF),
  onSurfaceVariant = Color(0xFF4A435E),
  outline = SoftOutline
)

@Composable
fun SpellingStarsTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
