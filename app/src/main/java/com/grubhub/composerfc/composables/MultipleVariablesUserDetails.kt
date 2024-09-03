package com.grubhub.composerfc.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MultipleVariablesUserDetails(
    name: String?,
    age: Int?,
    onButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row {
            Text(
                "Welcome"
            )
        }
        Row {
            Text(
                "Name: $name"
            )
        }
        Row {
            Text(
                "Age: $age"
            )
        }
        Row {
            Button(
                onClick = onButtonClicked
            ) {
                Text(
                    "Send email"
                )
            }
        }
    }
}
