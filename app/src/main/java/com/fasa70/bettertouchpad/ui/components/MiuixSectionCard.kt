package com.fasa70.bettertouchpad.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fasa70.bettertouchpad.ui.theme.MiuixRadius
import com.fasa70.bettertouchpad.ui.theme.MiuixSpacing
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Text as MiuixText
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun MiuixSectionCard(
    title: String,
    subtitle: String? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = MiuixRadius.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MiuixSpacing.md, vertical = MiuixSpacing.sm),
            verticalArrangement = Arrangement.spacedBy(MiuixSpacing.xs)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(MiuixSpacing.xxs)) {
                MiuixText(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp
                )
                subtitle?.let {
                    MiuixText(
                        text = it,
                        fontSize = 12.sp,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                    )
                }
            }
            content()
        }
    }
}
