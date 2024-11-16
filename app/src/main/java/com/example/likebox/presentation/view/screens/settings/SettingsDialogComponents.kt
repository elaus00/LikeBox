package com.example.likebox.presentation.view.screens.settings

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.likebox.presentation.view.theme.mainColor

@Composable
fun ExportDialog(
    onDismiss: () -> Unit,
    onExport: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Export Data",
                color = mainColor
            )
        },
        text = {
            Text("This will export all your liked content and settings. The exported data can be used to restore your content on another device.")
        },
        confirmButton = {
            TextButton(
                onClick = onExport,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = mainColor
                )
            ) {
                Text("Export")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ImportDialog(
    onDismiss: () -> Unit,
    onImport: (String) -> Unit
) {
    var jsonInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Import Data",
                color = mainColor
            )
        },
        text = {
            OutlinedTextField(
                value = jsonInput,
                onValueChange = { jsonInput = it },
                label = { Text("Paste exported data here") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = mainColor,
                    focusedLabelColor = mainColor
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onImport(jsonInput) },
                enabled = jsonInput.isNotBlank(),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = mainColor
                )
            ) {
                Text("Import")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ResetSettingsDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Reset Settings",
                color = Color.Red
            )
        },
        text = {
            Text("This will reset all settings to their default values. This action cannot be undone. Your liked content will not be affected.")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Red
                )
            ) {
                Text("Reset")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

