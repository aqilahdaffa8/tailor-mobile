package com.tailor.mobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tailor.mobile.ui.theme.ControlShape
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.RoseBg
import com.tailor.mobile.ui.theme.RoseBorder
import com.tailor.mobile.ui.theme.RoseText
import com.tailor.mobile.ui.theme.Zinc100
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc400
import com.tailor.mobile.ui.theme.Zinc700
import com.tailor.mobile.ui.theme.Zinc800
import com.tailor.mobile.ui.theme.Zinc900

enum class TailorButtonVariant {
    Primary,
    Secondary,
    Danger
}

/**
 * Universal Button component implementing DESIGN.md specifications
 */
@Composable
fun TailorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: TailorButtonVariant = TailorButtonVariant.Primary,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    when (variant) {
        TailorButtonVariant.Primary -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(44.dp),
                enabled = enabled && !isLoading,
                shape = ControlShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Zinc900,
                    contentColor = PureWhite,
                    disabledContainerColor = Zinc200,
                    disabledContentColor = Zinc400
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = PureWhite,
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (leadingIcon != null) {
                            leadingIcon()
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = text,
                            color = PureWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
        TailorButtonVariant.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.height(44.dp),
                enabled = enabled && !isLoading,
                shape = ControlShape,
                border = BorderStroke(1.dp, Zinc200),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = PureWhite,
                    contentColor = Zinc700,
                    disabledContentColor = Zinc400
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Zinc700,
                        strokeWidth = 2.dp
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (leadingIcon != null) {
                            leadingIcon()
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = text,
                            color = Zinc700,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
        TailorButtonVariant.Danger -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(44.dp),
                enabled = enabled && !isLoading,
                shape = ControlShape,
                border = BorderStroke(1.dp, RoseBorder),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RoseBg,
                    contentColor = RoseText
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = text,
                    color = RoseText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Primary CTA Button - Zinc 900 Charcoal with 8dp radius (DESIGN.md)
 */
@Composable
fun TailorPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    TailorButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        variant = TailorButtonVariant.Primary,
        enabled = enabled,
        isLoading = isLoading
    )
}

/**
 * Secondary / Outlined Button - Pure White with subtle Zinc-200 border and 8dp radius
 */
@Composable
fun TailorSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TailorButton(
        text = text,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        variant = TailorButtonVariant.Secondary,
        enabled = enabled
    )
}
