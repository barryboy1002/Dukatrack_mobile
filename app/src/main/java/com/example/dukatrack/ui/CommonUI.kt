package com.example.dukatrack.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dukatrack.ui.theme.TextMuted
import com.example.dukatrack.ui.theme.White

@Composable
fun ProFeatureDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(AppIcons.Star, contentDescription = null, tint = Color(0xFFFFD700))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pro Feature", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Text("This feature is available in the Dukatrack Desktop Pro version. Manage your stock, suppliers, and detailed reports seamlessly on a larger screen.")
        },
        confirmButton = {
            Button(
                onClick = { /* No-op for now */ },
                enabled = false,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                )
            ) {
                Text("Go to Desktop Pro", color = White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = TextMuted)
            }
        },
        containerColor = White,
        shape = RoundedCornerShape(16.dp)
    )
}
