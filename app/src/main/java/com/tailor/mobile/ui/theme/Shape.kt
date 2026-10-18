package com.tailor.mobile.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Strict radius standard from DESIGN.md:
// 1. Interactive controls (Button, Input, Badge): 8dp (rounded-lg)
// 2. Containers & Cards (Metrics, Tables, Modals): 12dp - 16dp (rounded-xl, rounded-2xl)
val TailorShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),       // Controls, inputs, buttons
    medium = RoundedCornerShape(12.dp),     // Modal, small cards
    large = RoundedCornerShape(16.dp),      // Cards, container
    extraLarge = RoundedCornerShape(24.dp)
)

val ControlShape = RoundedCornerShape(8.dp)
val CardShape = RoundedCornerShape(16.dp)
val DialogShape = RoundedCornerShape(16.dp)
val PillShape = RoundedCornerShape(999.dp)
