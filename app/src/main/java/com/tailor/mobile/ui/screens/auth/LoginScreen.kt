package com.tailor.mobile.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tailor.mobile.ui.components.TailorCard
import com.tailor.mobile.ui.components.TailorPrimaryButton
import com.tailor.mobile.ui.components.TailorTextField
import com.tailor.mobile.ui.theme.CardShape
import com.tailor.mobile.ui.theme.ControlShape
import com.tailor.mobile.ui.theme.RoseBg
import com.tailor.mobile.ui.theme.RoseBorder
import com.tailor.mobile.ui.theme.RoseText
import com.tailor.mobile.ui.theme.Zinc100
import com.tailor.mobile.ui.theme.Zinc200
import com.tailor.mobile.ui.theme.Zinc50
import com.tailor.mobile.ui.theme.Zinc500
import com.tailor.mobile.ui.theme.Zinc600
import com.tailor.mobile.ui.theme.Zinc900

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.loginState.collectAsState()
    val focusManager = LocalFocusManager.current
    var isPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onLoginSuccess()
            viewModel.resetState()
        }
    }

    Scaffold(
        containerColor = Zinc50
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header (DESIGN.md: Brand Header text murni "TAILOR SYSTEM", no vintage scissors)
                Text(
                    text = "TAILOR SYSTEM",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                    color = Zinc900
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Masuk ke akun penjahit Anda",
                    fontSize = 14.sp,
                    color = Zinc500
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Login Form Card
                TailorCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Error Banner if present
                    if (!state.errorMessage.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(ControlShape)
                                .background(RoseBg)
                                .border(1.dp, RoseBorder, ControlShape)
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = state.errorMessage ?: "",
                                fontSize = 13.sp,
                                color = RoseText,
                                lineHeight = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                    }

                    // Email Field
                    val emailError = state.fieldErrors?.get("email")?.firstOrNull()
                    TailorTextField(
                        value = state.email,
                        onValueChange = { viewModel.onEmailChange(it) },
                        label = "Email",
                        placeholder = "nama@domain.com",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        isError = emailError != null,
                        errorMessage = emailError,
                        enabled = !state.isLoading
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    val passwordError = state.fieldErrors?.get("password")?.firstOrNull()
                    TailorTextField(
                        value = state.password,
                        onValueChange = { viewModel.onPasswordChange(it) },
                        label = "Password",
                        placeholder = "Masukkan password",
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                viewModel.login()
                            }
                        ),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (isPasswordVisible) "Sembunyikan password" else "Tampilkan password",
                                    tint = Zinc500
                                )
                            }
                        },
                        isError = passwordError != null,
                        errorMessage = passwordError,
                        enabled = !state.isLoading
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit Button (CTA Zinc-900)
                    TailorPrimaryButton(
                        text = "Masuk",
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.login()
                        },
                        isLoading = state.isLoading
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Footer note
                Text(
                    text = "Tailor Management System • v1.0",
                    fontSize = 12.sp,
                    color = Zinc500
                )
            }
        }
    }
}
