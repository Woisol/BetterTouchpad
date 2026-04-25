package com.fasa70.bettertouchpad.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fasa70.bettertouchpad.R
import com.fasa70.bettertouchpad.SettingsRepository
import com.fasa70.bettertouchpad.ui.components.MiuixSectionCard
import com.fasa70.bettertouchpad.ui.theme.MiuixSpacing
import top.yukonga.miuix.kmp.basic.Slider as MiuixSlider
import top.yukonga.miuix.kmp.basic.Switch as MiuixSwitch
import top.yukonga.miuix.kmp.basic.Text as MiuixText
import top.yukonga.miuix.kmp.basic.TextField as MiuixTextField
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SettingsScreen(repo: SettingsRepository) {
    val settings by repo.settings.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = MiuixSpacing.md, vertical = MiuixSpacing.xs),
        verticalArrangement = Arrangement.spacedBy(MiuixSpacing.sm)
    ) {
        MiuixText(
            text = stringResource(R.string.settings_page_title),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(vertical = MiuixSpacing.xs)
        )

        MiuixSectionCard(
            title = stringResource(R.string.section_feature_toggles),
            subtitle = stringResource(R.string.section_feature_toggles_subtitle)
        ) {
            FeatureSwitch(stringResource(R.string.toggle_single_finger_move), settings.singleFingerMove) {
                repo.update { copy(singleFingerMove = it) }
            }
            FeatureSwitch(stringResource(R.string.toggle_single_finger_tap), settings.singleFingerTap) {
                repo.update { copy(singleFingerTap = it) }
            }
            FeatureSwitch(stringResource(R.string.toggle_physical_click), settings.physicalClick) {
                repo.update { copy(physicalClick = it) }
            }
            FeatureSwitch(stringResource(R.string.toggle_double_tap_drag), settings.doubleTapDrag) {
                repo.update { copy(doubleTapDrag = it) }
            }
            FeatureSwitch(stringResource(R.string.toggle_two_finger_tap), settings.twoFingerTap) {
                repo.update { copy(twoFingerTap = it) }
            }
            FeatureSwitch(stringResource(R.string.toggle_two_finger_scroll), settings.twoFingerScroll) {
                repo.update { copy(twoFingerScroll = it) }
            }
            FeatureSwitch(stringResource(R.string.toggle_natural_scroll), settings.naturalScroll) {
                repo.update { copy(naturalScroll = it) }
            }
            FeatureSwitch(stringResource(R.string.toggle_edge_swipe), settings.edgeSwipe) {
                repo.update { copy(edgeSwipe = it) }
            }
            FeatureSwitch(stringResource(R.string.toggle_three_finger_move), settings.threeFingerMove) {
                repo.update { copy(threeFingerMove = it) }
            }
            FeatureSwitch(stringResource(R.string.toggle_three_finger_middle_click), settings.threeFingerMiddleClick) {
                repo.update { copy(threeFingerMiddleClick = it) }
            }
            MiuixText(
                text = stringResource(R.string.three_finger_note),
                fontSize = 12.sp,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary
            )
        }

        MiuixSectionCard(
            title = stringResource(R.string.section_sensitivity),
            subtitle = stringResource(R.string.section_sensitivity_subtitle)
        ) {
            SensitivityRow(
                label = stringResource(R.string.sensitivity_cursor),
                value = settings.cursorSensitivity,
                range = 0.01f..5.0f,
                onValueChange = { repo.update { copy(cursorSensitivity = it) } },
                onDone = { focusManager.clearFocus() }
            )
            SensitivityRow(
                label = stringResource(R.string.sensitivity_scroll),
                value = settings.scrollSensitivity,
                range = 0.01f..5.0f,
                onValueChange = { repo.update { copy(scrollSensitivity = it) } },
                onDone = { focusManager.clearFocus() }
            )
            SensitivityRow(
                label = stringResource(R.string.sensitivity_touch_inject),
                value = settings.touchInjectSpeed,
                range = 0.01f..3.0f,
                onValueChange = { repo.update { copy(touchInjectSpeed = it) } },
                onDone = { focusManager.clearFocus() }
            )
            SensitivityRow(
                label = stringResource(R.string.sensitivity_edge_threshold),
                value = settings.edgeThreshold,
                range = 0.01f..0.30f,
                onValueChange = { repo.update { copy(edgeThreshold = it) } },
                onDone = { focusManager.clearFocus() }
            )
        }

        MiuixSectionCard(
            title = stringResource(R.string.section_drag_interval),
            subtitle = stringResource(R.string.section_drag_interval_subtitle)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MiuixSlider(
                    value = settings.doubleTapIntervalMs.toFloat(),
                    onValueChange = { repo.update { copy(doubleTapIntervalMs = it.toInt()) } },
                    valueRange = 10f..500f,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(MiuixSpacing.xs))
                var dtText by remember(settings.doubleTapIntervalMs) {
                    mutableStateOf(settings.doubleTapIntervalMs.toString())
                }
                MiuixTextField(
                    value = dtText,
                    onValueChange = { dtText = it },
                    label = stringResource(R.string.section_drag_interval),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        dtText.toIntOrNull()?.takeIf { it in 50..500 }?.let {
                            repo.update { copy(doubleTapIntervalMs = it) }
                        }
                        focusManager.clearFocus()
                    }),
                    singleLine = true,
                    modifier = Modifier.width(90.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                )
            }
        }

        MiuixSectionCard(
            title = stringResource(R.string.section_axis_correction),
            subtitle = stringResource(R.string.section_axis_correction_subtitle)
        ) {
            FeatureSwitch(stringResource(R.string.toggle_swap_axes), settings.swapAxes) {
                repo.update { copy(swapAxes = it) }
            }
            FeatureSwitch(stringResource(R.string.toggle_invert_x), settings.invertX) {
                repo.update { copy(invertX = it) }
            }
            FeatureSwitch(stringResource(R.string.toggle_invert_y), settings.invertY) {
                repo.update { copy(invertY = it) }
            }
        }

        MiuixSectionCard(
            title = stringResource(R.string.section_compatibility),
            subtitle = stringResource(R.string.section_compatibility_subtitle)
        ) {
            FeatureSwitch(stringResource(R.string.toggle_exclusive_grab), settings.exclusiveGrab) {
                repo.update { copy(exclusiveGrab = it) }
            }
            FeatureSwitch(stringResource(R.string.toggle_auto_detect), settings.autoDetectDevice) {
                repo.update { copy(autoDetectDevice = it) }
            }

            if (!settings.autoDetectDevice) {
                var devicePathText by remember(settings.devicePath) { mutableStateOf(settings.devicePath) }
                MiuixTextField(
                    value = devicePathText,
                    onValueChange = { devicePathText = it },
                    label = stringResource(R.string.device_path_label),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (devicePathText.isNotBlank()) {
                            repo.update { copy(devicePath = devicePathText.trim()) }
                        }
                        focusManager.clearFocus()
                    }),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                CoordInput(stringResource(R.string.pad_max_x_label), settings.padMaxX.toString()) { v ->
                    v.toIntOrNull()?.takeIf { it > 0 }?.let { repo.update { copy(padMaxX = it) } }
                }
                CoordInput(stringResource(R.string.pad_max_y_label), settings.padMaxY.toString()) { v ->
                    v.toIntOrNull()?.takeIf { it > 0 }?.let { repo.update { copy(padMaxY = it) } }
                }
            } else {
                MiuixText(
                    text = stringResource(
                        R.string.device_detected_value,
                        settings.devicePath,
                        settings.padMaxX,
                        settings.padMaxY
                    ),
                    fontSize = 12.sp,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                )
            }
        }
    }
}

@Composable
private fun FeatureSwitch(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MiuixSpacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MiuixText(label, modifier = Modifier.weight(1f), fontSize = 14.sp)
        MiuixSwitch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

/**
 * A row with a slider + a small text field that both control the same Float value.
 * The slider covers [range] continuously (no fixed steps → smooth).
 * The text field lets the user type an exact value and confirms on Done / focus-loss.
 */
@Composable
private fun SensitivityRow(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    onDone: () -> Unit = {}
) {
    // Local text state — only committed to the repo when valid
    var textValue by remember(value) { mutableStateOf("%.3f".format(value)) }
    // Track whether the text field is being edited so we don't fight the slider
    var isEditing by remember { mutableStateOf(false) }

    fun commitText(raw: String) {
        val f = raw.toFloatOrNull() ?: return
        val clamped = f.coerceIn(range.start, range.endInclusive)
        onValueChange(clamped)
        textValue = "%.3f".format(clamped)
        isEditing = false
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MiuixSpacing.xxs)
    ) {
        MiuixText(
            label,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = MiuixSpacing.xxs)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MiuixSlider(
                value = value,
                onValueChange = {
                    if (!isEditing) {
                        onValueChange(it)
                        textValue = "%.3f".format(it)
                    }
                },
                valueRange = range,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(MiuixSpacing.xs))
            MiuixTextField(
                value = textValue,
                onValueChange = {
                    textValue = it
                    isEditing = true
                },
                label = label,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction    = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { commitText(textValue); onDone() }
                ),
                singleLine = true,
                modifier = Modifier.width(180.dp),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
            )
        }
    }
}

@Composable
private fun CoordInput(label: String, value: String, onValueChange: (String) -> Unit) {
    var text by remember(value) { mutableStateOf(value) }
    MiuixTextField(
        value = text,
        onValueChange = { newVal ->
            text = newVal
            onValueChange(newVal)
        },
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MiuixSpacing.xxs)
    )
}
