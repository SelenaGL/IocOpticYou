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
import com.example.opticyou.data.Diagnostic
import com.example.opticyou.data.LoginResponse

/**
 * Pantalla composable per gestionar el CRUD dels historials.
 *
 * @param modifier Modifier opcional.
 * @param navigate Funció de navegació
 * @param viewModel ViewModel que gestiona els clients.
 */
@Composable
fun DiagnosticScreen(
    historialId: Long,
    modifier: Modifier = Modifier,
    viewModel: DiagnosticViewModel = viewModel()
) {
    val context = LocalContext.current
    val token = context
        .getSharedPreferences("token", Context.MODE_PRIVATE)
        .getString("session_token","") ?: ""
    viewModel.setAuthToken(token)

    var selected by remember { mutableStateOf<Diagnostic?>(null) }
    var descripcio by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val diags by viewModel.diagnostics.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Gestió de Diagnòstics", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))

        // Únicament dos camps: descripcio i date
        OutlinedTextField(
            value = descripcio,
            onValueChange = { descripcio = it },
            label = { Text("Descripció") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Data") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                viewModel.addDiagnostic(descripcio, date, historialId) { ok ->
                    Toast.makeText(context,
                        if (ok) "Diagnòstic creat correctament" else "Error creant",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                selected = null; descripcio=""; date=""
            }, modifier = Modifier.weight(1f)) { Text("Alta") }

            Button(onClick = {
                selected?.let {
                    val upd = it.copy(
                        descripcio = descripcio,
                        date = date,
                        historialId = historialId
                    )
                    viewModel.updateDiagnostic(upd) { ok ->
                        Toast.makeText(context,
                            if (ok) "Diagnòstic actualitzat correctament" else "Error actualitzant",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    selected = null; descripcio=""; date=""
                }
            }, modifier = Modifier.weight(1f), enabled = selected != null) {
                Text("Modificació")
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                selected?.let {
                    viewModel.deleteDiagnostic(it) { ok ->
                        Toast.makeText(context,
                            if (ok) "Diagnòstic eliminat correctament" else "Error eliminant",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    selected = null; descripcio=""; date=""
                }
            }, modifier = Modifier.weight(1f), enabled = selected != null) {
                Text("Baixa")
            }

            // Botó Consulta use historialId ja tingut
            Button(onClick = {
                viewModel.loadDiagnosticById(historialId) { ok ->
                    Toast.makeText(context,
                        if (ok) "Diagnòstics carregats correctament" else "Error carregant",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, modifier = Modifier.weight(1f)) {
                Text("Consulta")
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(diags) { diag ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selected = diag
                        descripcio = diag.descripcio
                        date = diag.date.toString()
                    }
                    .padding(vertical = 8.dp)
                ) {
                    Text(text = "${diag.date}: ${diag.descripcio}",
                        modifier = Modifier.weight(1f))
                }
            }
        }
    }
}