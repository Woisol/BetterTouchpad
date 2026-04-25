package com.fasa70.bettertouchpad

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fasa70.bettertouchpad.ui.SettingsScreen
import com.fasa70.bettertouchpad.ui.components.MiuixSectionCard
import com.fasa70.bettertouchpad.ui.theme.BetterTouchpadTheme
import com.fasa70.bettertouchpad.ui.theme.MiuixRadius
import com.fasa70.bettertouchpad.ui.theme.MiuixSpacing
import top.yukonga.miuix.kmp.basic.Button as MiuixButton
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.NavigationBar
import top.yukonga.miuix.kmp.basic.NavigationBarDisplayMode
import top.yukonga.miuix.kmp.basic.NavigationBarItem
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text as MiuixText
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.GridView
import top.yukonga.miuix.kmp.icon.extended.Settings
import top.yukonga.miuix.kmp.theme.MiuixTheme

class MainActivity : ComponentActivity() {
    private lateinit var settingsRepo: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsRepo = SettingsRepository(applicationContext)
        enableEdgeToEdge()
        setContent {
            var selectedTab by rememberSaveable { mutableIntStateOf(0) }
            val tabLabels = listOf(
                stringResource(R.string.nav_home),
                stringResource(R.string.nav_settings)
            )
            val tabIcons = listOf(
                MiuixIcons.GridView,
                MiuixIcons.Settings
            )

            BetterTouchpadTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = stringResource(R.string.app_name),
                            subtitle = stringResource(R.string.app_desc)
                        )
                    },
                    bottomBar = {
                        NavigationBar(mode = NavigationBarDisplayMode.IconAndText) {
                            tabLabels.forEachIndexed { index, label ->
                                NavigationBarItem(
                                    selected = selectedTab == index,
                                    onClick = { selectedTab = index },
                                    icon = tabIcons[index],
                                    label = label
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (selectedTab) {
                            0 -> HomeScreen(repo = settingsRepo)
                            else -> SettingsScreen(repo = settingsRepo)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun HomeScreen(repo: SettingsRepository) {
        val settings by repo.settings.collectAsState()
        var running by remember { mutableStateOf(TouchpadService.isRunning) }
        val versionInfo = remember { readAppVersionInfo() }
        val uriHandler = LocalUriHandler.current

        LaunchedEffect(Unit) {
            while (true) {
                running = TouchpadService.isRunning
                kotlinx.coroutines.delay(1000)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = MiuixSpacing.md, vertical = MiuixSpacing.xs),
            verticalArrangement = Arrangement.spacedBy(MiuixSpacing.sm)
        ) {
            ServiceStatusCard(
                running = running,
                onToggle = {
                    setTouchpadServiceRunning(!running)
                    running = !running
                }
            )
            OverviewStatusCard(
                running = running,
                devicePath = settings.devicePath,
                autoDetectDevice = settings.autoDetectDevice,
                padMaxX = settings.padMaxX,
                padMaxY = settings.padMaxY,
                versionInfo = versionInfo
            )
            AboutCard(
                onOpenProject = {
                    uriHandler.openUri("https://github.com/Woisol/BetterTouchpad")
                },
                onOpenOrigin = {
                    uriHandler.openUri("https://github.com/fasa70/BetterTouchpad")
                }
            )
        }
    }

    @Composable
    private fun ServiceStatusCard(running: Boolean, onToggle: () -> Unit) {
        val cardBackground = if (running) MiuixTheme.colorScheme.primaryContainer else MiuixTheme.colorScheme.errorContainer
        val accentColor = if (running) MiuixTheme.colorScheme.onPrimary else MiuixTheme.colorScheme.error
        val icon = if (running) "✅" else "❌"

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp),
            colors = CardDefaults.defaultColors(color = cardBackground),
            cornerRadius = MiuixRadius.large,
            onClick = onToggle
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MiuixSpacing.lg, vertical = MiuixSpacing.lg)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 72.dp, bottom = MiuixSpacing.xl),
                    verticalArrangement = Arrangement.spacedBy(MiuixSpacing.xs)
                ) {
                    MiuixText(
                        text = if (running) stringResource(R.string.service_card_running_title)
                        else stringResource(R.string.service_card_stopped_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = accentColor
                    )
                    MiuixText(
                        text = if (running) stringResource(R.string.service_card_running_desc)
                        else stringResource(R.string.service_card_stopped_desc),
                        fontSize = 14.sp,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                    )
                    // Spacer(modifier = Modifier.height(MiuixSpacing.sm))
                    // MiuixButton(onClick = onToggle) {
                    //     MiuixText(
                    //         if (running) stringResource(R.string.stop_service)
                    //         else stringResource(R.string.start_service)
                    //     )
                    // }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .fillMaxWidth(),
                        // .padding(end = MiuixSpacing.lg, bottom = MiuixSpacing.lg)
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                    MiuixText(
                        text = stringResource(R.string.service_card_tap_hint),
                        fontSize = 12.sp,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                    )
//                    Spacer(modifier = Modifier.width(1f))
                    MiuixText(
                        text = icon,
                        fontSize = 38.sp,
                        // modifier = Modifier.align(Alignment.BottomEnd)

                    )
                }
            }
        }
    }

    @Composable
    private fun OverviewStatusCard(
        running: Boolean,
        devicePath: String,
        autoDetectDevice: Boolean,
        padMaxX: Int,
        padMaxY: Int,
        versionInfo: AppVersionInfo
    ) {
        MiuixSectionCard(
            title = stringResource(R.string.overview_status_title),
            subtitle = stringResource(R.string.overview_status_subtitle)
        ) {
            StatusText(
                text = stringResource(
                    R.string.overview_service_state,
                    if (running) stringResource(R.string.service_running) else stringResource(R.string.service_stopped)
                )
            )
            StatusText(
                text = if (autoDetectDevice) stringResource(R.string.overview_device_auto, devicePath)
                else stringResource(R.string.overview_device_manual, devicePath)
            )
            StatusText(text = stringResource(R.string.overview_coordinate_state, padMaxX, padMaxY))
            StatusText(text = stringResource(R.string.overview_version_state, versionInfo.name, versionInfo.code))
        }
    }

    @Composable
    private fun StatusText(text: String) {
        MiuixText(
            text = text,
            fontSize = 14.sp,
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary
        )
    }

    @Composable
    private fun AboutCard(onOpenProject: () -> Unit, onOpenOrigin: () -> Unit = {}) {
        MiuixSectionCard(
            title = stringResource(R.string.about_title),
            subtitle = stringResource(R.string.about_subtitle)
        ) {
            MiuixText(
                text = stringResource(R.string.footer_author),
                fontSize = 12.sp,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary
            )
            MiuixText(
                text = stringResource(R.string.footer_star),
                fontSize = 12.sp,
                color = MiuixTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onOpenProject)
            )
            MiuixText(
                text = stringResource(R.string.footer_star_origin),
                fontSize = 12.sp,
                color = MiuixTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onOpenOrigin)
            )
        }
    }

    private fun setTouchpadServiceRunning(running: Boolean) {
        val intent = Intent(this, TouchpadService::class.java)
        if (running) {
            startForegroundService(intent)
        } else {
            stopService(intent)
        }
    }

    private fun readAppVersionInfo(): AppVersionInfo {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }
        return AppVersionInfo(packageInfo.versionName ?: "-", versionCode)
    }

    private data class AppVersionInfo(val name: String, val code: Long)
}
