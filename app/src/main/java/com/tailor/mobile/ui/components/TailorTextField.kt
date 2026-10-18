package com.tailor.mobile.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tailor.mobile.ui.theme.ControlShape
import com.tailor.mobile.ui.theme.PureWhite
import com.tailor.mobile.ui.theme.RoseBg
import com.tailor.mobile.ui.theme.RoseText
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc400
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc700
import com.tailor.mobile.ui.theme.Zinc900

@Composable
fun TailorTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (!label.isNullOrBlank()) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Zinc700
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = isError,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            placeholder = placeholder?.let {
                {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = Zinc400
                    )
                }
            },
            shape = ControlShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = PureWhite,
                unfocusedContainerColor = PureWhite,
                disabledContainerColor = PureWhite,
                errorContainerColor = PureWhite,
                focusedBorderColor = Zinc900,
                unfocusedBorderColor = Zinc200,
                disabledBorderColor = Zinc200,
                errorBorderColor = RoseText,
                focusedTextColor = Zinc900,
                unfocusedTextColor = Zinc900,
                disabledTextColor = Zinc400,
                errorTextColor = Zinc900,
                cursorColor = Zinc900
            )
        )

        if (isError && !errorMessage.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = RoseText
            )
        }
    }
}
