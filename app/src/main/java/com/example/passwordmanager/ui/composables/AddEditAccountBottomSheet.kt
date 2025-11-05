package com.example.passwordmanager.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.ui.model.PasswordEntryEntity

@Composable
fun AddEditAccountBottomSheet(
    initial: PasswordEntryEntity? = null,
    onSave: (title: String, username: String, password: String) -> Unit,
    onCancel: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    var accountName by rememberSaveable(initial != null) { mutableStateOf(initial?.title ?: "") }
    var username by rememberSaveable(initial != null) { mutableStateOf(initial?.username ?: "") }
    var password by rememberSaveable(initial != null) { mutableStateOf(initial?.password ?: "") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 18.dp)) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(48.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFFE0E0E0))
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (initial == null) "Add New Account" else "Edit Account",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = accountName,
            onValueChange = { accountName = it; showError = false },
            placeholder = { Text("Account Name")},
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it; showError = false },
            placeholder = { Text("Username / Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it; showError = false },
            placeholder = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon =
                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (passwordVisible) "Hide" else "Show"
                    )
                }
            },
            shape = RoundedCornerShape(8.dp)
        )

        if (showError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Please fill all fields", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = {
                focusManager.clearFocus(force = true)
                if (accountName.isBlank() || username.isBlank() || password.isBlank()) {
                    showError = true
                } else {
                    onSave(accountName.trim(), username.trim(), password)
                    // NOTE: we do not clear fields here to allow UX for edit
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(26.dp)
        ) {
            Text(
                text = if (initial == null) "Add New Account" else "Save Changes",
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onCancel, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Cancel")
        }
    }
}