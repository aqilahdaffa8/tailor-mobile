package com.tailor.mobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tailor.mobile.ui.theme.AmberBg
import com.tailor.mobile.ui.theme.AmberBorder
import com.tailor.mobile.ui.theme.AmberText
import com.tailor.mobile.ui.theme.ControlShape
import com.tailor.mobile.ui.theme.EmeraldBg
import com.tailor.mobile.ui.theme.EmeraldBorder
import com.tailor.mobile.ui.theme.EmeraldText
import com.tailor.mobile.ui.theme.PillShape
import com.tailor.mobile.ui.theme.RoseBg
import com.tailor.mobile.ui.theme.RoseBorder
import com.tailor.mobile.ui.theme.RoseText
import com.tailor.mobile.ui.theme.SlateBg
import com.tailor.mobile.ui.theme.SlateBorder
import com.tailor.mobile.ui.theme.SlateText

enum class BadgeVariant {
    SUCCESS, // Emerald (Selesai / Aktif)
    WARNING, // Amber (Proses / Menunggu)
    ERROR,   // Rose (Terlambat / Batal / Error)
    NEUTRAL  // Slate (Draf / Netral)
}

/**
 * Status Badge with Soft Pastel Ring Border (DESIGN.md)
 */
@Composable
fun StatusBadge(
    text: String,
    variant: BadgeVariant,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, borderColor) = when (variant) {
        BadgeVariant.SUCCESS -> Triple(EmeraldBg, EmeraldText, EmeraldBorder)
        BadgeVariant.WARNING -> Triple(AmberBg, AmberText, AmberBorder)
        BadgeVariant.ERROR -> Triple(RoseBg, RoseText, RoseBorder)
        BadgeVariant.NEUTRAL -> Triple(SlateBg, SlateText, SlateBorder)
    }

    Box(
        modifier = modifier
            .clip(PillShape)
            .background(bgColor)
            .border(BorderStroke(1.dp, borderColor), PillShape)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp, // Enforce min 12sp
            fontWeight = FontWeight.Medium
        )
    }
}
