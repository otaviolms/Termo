package br.com.otaviolms.termo.views.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun GameOverScreen() {
    Column {
        Text("GameOver")
        Button(onClick = {
        }) {
            Text("Recomeçar")
        }
    }
}