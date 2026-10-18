package com.tailor.mobile.ui.screens.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tailor.mobile.data.source.remote.response.PesananRingkasDto
import com.tailor.mobile.data.source.remote.response.ReminderDto
import com.tailor.mobile.ui.components.BadgeVariant
import com.tailor.mobile.ui.components.StatusBadge
import com.tailor.mobile.ui.components.TailorCard
import com.tailor.mobile.ui.theme.CardShape
import com.tailor.mobile.ui.theme.ControlShape
import com.tailor.mobile.ui.theme.PillShape
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.Zinc100
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc400
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc600
import com.tailor.mobile.ui.theme.Zinc700
import com.tailor.mobile.ui.theme.Zinc900
import com.tailor.mobile.util.Formatters

/**
 * Metric Card - High-density clean metric container (DESIGN.md)
 */
@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String? = null,
    badgeText: String? = null,
    badgeVariant: BadgeVariant = BadgeVariant.NEUTRAL,
    modifier: Modifier = Modifier
) {
    TailorCard(
        modifier = modifier,
        contentPadding = 14.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Zinc500
            )
            if (!badgeText.isNullOrBlank()) {
                StatusBadge(text = badgeText, variant = badgeVariant)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Zinc900,
            letterSpacing = (-0.4).sp
        )
        if (!subtitle.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Zinc500
            )
        }
    }
}

/**
 * Quick Action Shortcut Card
 */
@Composable
fun QuickActionButton(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(ControlShape)
            .clickable(onClick = onClick),
        shape = ControlShape,
        color = PureWhite,
        border = BorderStroke(1.dp, Zinc200)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(PillShape)
                    .background(Zinc100),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Zinc900
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Zinc900
            )
        }
    }
}

/**
 * Order Card for Dashboard List
 */
@Composable
fun DashboardPesananItem(
    pesanan: PesananRingkasDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .clickable(onClick = onClick),
        shape = CardShape,
        color = PureWhite,
        border = BorderStroke(1.dp, Zinc200)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pesanan.nomorPesanan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Zinc900
                )

                val badgeVariant = when (pesanan.statusBayar) {
                    "Lunas" -> BadgeVariant.SUCCESS
                    "DP" -> BadgeVariant.WARNING
                    else -> BadgeVariant.ERROR
                }
                StatusBadge(
                    text = pesanan.statusBayar ?: "Belum Bayar",
                    variant = badgeVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Zinc500
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = pesanan.pelanggan?.nama ?: "Pelanggan",
                    fontSize = 13.sp,
                    color = Zinc700,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Deadline: ${Formatters.formatTanggal(pesanan.deadline)}",
                    fontSize = 12.sp,
                    color = Zinc500
                )

                Text(
                    text = Formatters.formatRupiah(pesanan.totalTagihan),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Zinc900
                )
            }
        }
    }
}

/**
 * Reminder Item Card
 */
@Composable
fun ReminderCardItem(
    reminder: ReminderDto,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = ControlShape,
        color = PureWhite,
        border = BorderStroke(1.dp, Zinc200)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.judul,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Zinc900
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = reminder.pesan,
                    fontSize = 12.sp,
                    color = Zinc600,
                    lineHeight = 16.sp
                )
            }
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Tandai dibaca",
                    tint = Zinc500,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
