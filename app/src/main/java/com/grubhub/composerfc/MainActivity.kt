package com.grubhub.composerfc

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Column {
                    Row {
                        Button(
                            onClick = {
                                MultipleLiveDataFragment()
                                    .show(supportFragmentManager, "TAG")
                            },
                        ) {
                            Text("1. State with multiple LiveData-s")
                        }
                    }
                    Row {
                        Button(
                            onClick = {
                                SingleLiveDataFragment()
                                    .show(supportFragmentManager, "TAG")
                            }
                        ) {
                            Text("2. State with single LiveData")
                        }
                    }
                    Row {
                        Button(
                            onClick = {
                                SingleLiveDataWithDisplayableFragment()
                                    .show(supportFragmentManager, "TAG")
                            }
                        ) {
                            Text("3. Composable with Displayable class")
                        }
                    }
                    Row {
                        Button(
                            onClick = {
                                FlowsFragment()
                                    .show(supportFragmentManager, "TAG")
                            }
                        ) {
                            Text("4. Composable with single data class using Flows")
                        }
                    }
                    Row {
                        Button(
                            onClick = {
                                MviFragment()
                                    .show(supportFragmentManager, "TAG")
                            }
                        ) {
                            Text("5. Mvi Example")
                        }
                    }
                }
            }
        }
    }
}
