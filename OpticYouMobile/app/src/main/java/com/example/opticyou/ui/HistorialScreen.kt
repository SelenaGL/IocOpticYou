package com.example.opticyou.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.opticyou.data.Historial
import com.example.opticyou.data.LoginResponse
import androidx.compose.ui.tooling.preview.Preview
import com.example.opticyou.Screens

/**
 * Pantalla composable per gestionar el CRUD dels historials.
 *
 * @param modifier Modifier opcional.
 * @param navigate Funció de navegació (per exemple, al login).
 * @param viewModel ViewModel que gestiona els clients.
 */
@Composable
fun HistorialScreen(
    modifier: Modifier = Modifier,
    navigate: (LoginResponse) -> Unit = {},
    navigateToDiagnostic: (Historial) -> Unit = {},
    viewModel: HistorialViewModel = viewModel()
) {
    val context = LocalContext.current

    // Recuperem el token des de SharedPreferences
    val sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)
    val storedToken = sharedPreferences.getString("session_token", "") ?: ""
    viewModel.setAuthToken(storedToken)

    // Variables per al formulari
    var selectedHistorial by remember { mutableStateOf<Historial?>(null) }
    var patologies by remember { mutableStateOf("") }

    // Observem la llista d'historials
    val historials by viewModel.historials.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Gestió d'Historials", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = patologies,
            onValueChange = { patologies = it },
            label = { Text("Patologies") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (selectedHistorial != null) {
            // Mostrem la data de creació (de forma informativa ja que l'assigna el servidor)
            Text("Data de creació: ${selectedHistorial?.dataCreacio}")
            Spacer(modifier = Modifier.height(8.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
              // Botó "Modificació"
            Button(
                onClick = {
                    if (selectedHistorial != null) {
                        val updatedHistorial = selectedHistorial!!.copy(patologies = patologies)
                        viewModel.updateHistorial(updatedHistorial) { success ->
                            if (success) {
                                Toast.makeText(context, "Historial actualitzat correctament", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Error en l'actualització", Toast.LENGTH_SHORT).show()
                            }
                        }
                        selectedHistorial = null
                        patologies = ""
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = selectedHistorial != null
            ) {
                Text("Modificació")
            }

            // Botó "Consulta"
            Button(
                onClick = {
                    viewModel.loadHistorials { success ->
                        if (success) {
                            Toast.makeText(context, "Historials carregats correctament", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Error en carregar els historials", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Consulta")
            }

            //Botó per navegar a Diagnòstic
            Button(
                onClick = {
                    selectedHistorial?.let { navigateToDiagnostic(it) }
                },
                enabled = selectedHistorial != null,
                modifier = Modifier.weight(1f)
            ) {
                Text("Diagnòstic")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(historials) { historial ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedHistorial = historial
                            patologies = historial.patologies
                        }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Historial #${historial.idhistorial}: ${historial.patologies}",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistorialCrudScreenPreview() {
    val previewViewModel = HistorialViewModel()
    HistorialScreen(navigate = {}, viewModel = previewViewModel)
}