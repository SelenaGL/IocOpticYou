package com.example.opticyou.ui

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
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
 * Composable que mostra el menú d'opcions per als administradors.
 * @param options Mapa que associa els textos dels botons amb els valors corresponents.
 * @param onOptionClicked Lambda que es crida quan es selecciona una opció.
 * @param onLogoutClicked Lambda que es crida quan es fa clic a "Logout".
 */
@Composable
fun MenuAdminScreen(
    modifier: Modifier = Modifier,
    options: Map<String, String>,
    onOptionClicked: (String) -> Unit = {},
    onLogoutClicked: (MenuAdminViewModel, Context) -> Unit = { viewModel, context ->
        // Obtenim el token de la sessió
        val sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("session_token").apply()

    }
) {
    val viewModel:MenuAdminViewModel = viewModel()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("session_token", null)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("adminMenu")
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center) // Centra tot el contingut
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Centrar els elements horitzontalment
        ) {
            // Títol
            Text(stringResource(R.string.admin_welcome),
                fontSize = 25.sp)
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
                    println("Token que s'envia al logout: $token")  // Afegir aquest log aquí
                    if (token != null) {
                        // Passa el token a la funció logout del ViewModel
                        viewModel.logout(token)
                        // Elimina el token de SharedPreferences
                        sharedPreferences.edit().remove("session_token").apply()
                    }
                    // Finalment, crida el callback per navegar
                    onLogoutClicked(viewModel, context)
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
fun SelectOptionAdminPreview() {
    OpticYouTheme {
        MenuAdminScreen(
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

