package com.example.passwordmanager.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordmanager.ui.model.PasswordEntryEntity

@Composable
fun AccountDetailsSheet(
    entry: PasswordEntryEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        // drag handle
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
            text = "Account Details", fontSize = 16.sp, fontWeight = FontWeight.Bold,
            color = Color.Blue
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Account Type",
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            fontSize = 13.sp
        )
        Text(text = entry.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Username / Email",
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            fontSize = 13.sp
        )
        Text(text = entry.username, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Password",
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            fontSize = 13.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(text = "•".repeat(6), fontSize = 14.sp)
            Spacer(modifier = Modifier.weight(1f))
            // optional eye icon placeholder
            IconButton(onClick = { /* show/hide in advanced version */ }) {
                Icon(imageVector = Icons.Default.VisibilityOff, contentDescription = "Hidden")
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = { onEdit() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = "Edit", color = Color.White)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = { onDelete() },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = "Delete", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { onClose() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Close")
        }
    }
}