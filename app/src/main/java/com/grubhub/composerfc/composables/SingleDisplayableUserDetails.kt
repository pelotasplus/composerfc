package com.grubhub.composerfc.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class UserDetailsDisplayable(
    val name: String = "",
    val age: Int = 0
)

@Composable
fun SingleDisplayableUserDetails(
    displayable: UserDetailsDisplayable?,
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
                "Name: ${displayable?.name}"
            )
        }
        Row {
            Text(
                "Age: ${displayable?.age}"
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
