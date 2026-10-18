package com.tailor.mobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tailor.mobile.ui.theme.CardShape
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.Zinc200

/**
 * Standard Data / Metric Container Card - Pure White, 16dp radius, subtle Zinc 200 border (DESIGN.md)
 */
@Composable
fun TailorCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = PureWhite,
    borderColor: Color = Zinc200,
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = CardShape,
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}
