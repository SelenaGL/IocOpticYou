
package com.example.opticyou.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opticyou.R
import com.example.opticyou.ui.theme.OpticYouTheme
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Composable that displays the list of of app options as buttons
 * [options] map between button text and parameter to pass to onOptionClicked function
 * [onOptionClicked] lambda that triggers a button action
 * [onLogoutClicked] lambda that triggers the logout action
 */
@Composable
fun MenuUserScreen(
    modifier: Modifier = Modifier,
    options: Map<String, String>,
    onOptionClicked: (String) -> Unit = {},
    onLogoutClicked: (MenuUserViewModel) -> Unit = {}
) {
    var viewModel:MenuUserViewModel = viewModel()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center) // Centra tot el contingut
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Centrar els elements horitzontalment
        ) {
            // Títol
            Text(stringResource(R.string.user_welcome),
                fontSize = 25.sp,
                modifier = Modifier.testTag("userMenu"))
            Spacer(modifier = Modifier.height(16.dp))

            // Mostra cada opció en una fila independent
            options.forEach { (text, value) ->
                FilledTonalButton(
                    onClick = { onOptionClicked(value) },
                    modifier = Modifier
                        .fillMaxWidth(0.6f) // Amplada fixa per a tots els botons
                        .height(50.dp) // Alçada uniforme
                ) {
                    Text(text, textAlign = TextAlign.Center)
                }
                Spacer(modifier = Modifier.height(8.dp)) // Espai entre botons
            }

            // Botó de logout
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.logout()
                    onLogoutClicked(viewModel)
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f) // Mateixa amplada que els altres botons
                    .height(50.dp) // Alçada uniforme
            ) {
                Text("Logout", textAlign = TextAlign.Center)
            }
        }
    }
}

    @Preview
    @Composable
    fun SelectOptionUserPreview() {
        OpticYouTheme {
            MenuUserScreen(
                options = mapOf(
                    "Historial clínic" to "1",
                    "Demanar cita" to "2",
                    "Veure cites" to "3",
                    "Pautes" to "4"
                ),
                modifier = Modifier.fillMaxHeight()
            )
        }
    }

