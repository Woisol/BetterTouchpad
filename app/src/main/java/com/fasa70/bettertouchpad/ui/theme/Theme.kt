package com.fasa70.bettertouchpad.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController

@Composable
fun BetterTouchpadTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val mode = when {
        dynamicColor && darkTheme -> ColorSchemeMode.MonetDark
        dynamicColor -> ColorSchemeMode.MonetSystem
        darkTheme -> ColorSchemeMode.Dark
        else -> ColorSchemeMode.System
    }
    val controller = remember(mode) { ThemeController(colorSchemeMode = mode) }
    MiuixTheme(controller = controller) { content() }
}