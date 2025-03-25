package com.example.opticyou.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.opticyou.data.Center
import com.example.opticyou.data.LoginResponse
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.tooling.preview.Preview


/**
 * Pantalla composable per gestionar els centres.
 *
 * @param modifier
 * @param navigate Funció per navegar a una altra pantalla, passant un [LoginResponse] com a paràmetre.
 * @param viewModel viewModel de la pantalla CenterScreen.
 */
@Composable
fun CenterScreen(
    modifier: Modifier = Modifier,
    navigate: (LoginResponse) -> Unit = {},
    viewModel: CenterViewModel = viewModel()
) {

    // Variables locals per al formulari
    var selectedCenter by remember { mutableStateOf<Center?>(null) }
    var nom by remember { mutableStateOf("") }
    var direccio by remember { mutableStateOf("") }
    var telefon by remember { mutableStateOf("") }
    var horariObertura by remember { mutableStateOf("") }
    var horariTancament by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Observem la llista de centres des del ViewModel
    val centers by viewModel.centres.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Gestió de Centres", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nom,
            onValueChange = { nom = it },
            label = { Text("Nom") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = direccio,
            onValueChange = { direccio = it },
            label = { Text("Adreça") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = telefon,
            onValueChange = { telefon = it },
            label = { Text("Telèfon") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = horariObertura,
            onValueChange = { horariObertura = it },
            label = { Text("Horari Obertura") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = horariTancament,
            onValueChange = { horariTancament = it },
            label = { Text("Horari Tancament") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    viewModel.addCentre(
                        nom,
                        direccio,
                        telefon,
                        horariObertura,
                        horariTancament,
                        email
                    )
                    selectedCenter = null
                    nom = ""
                    direccio = ""
                    telefon = ""
                    horariObertura = ""
                    horariTancament = ""
                    email = ""
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Alta")
            }
            Button(
                onClick = {
                    if (selectedCenter != null) {
                        val updatedCenter = selectedCenter!!.copy(
                            nom = nom,
                            direccio = direccio,
                            telefon = telefon,
                            horari_opertura = horariObertura,
                            horari_tancament = horariTancament,
                            email = email
                        )
                        viewModel.updateCentre(updatedCenter)
                        selectedCenter = null
                        nom = ""
                        direccio = ""
                        telefon = ""
                        horariObertura = ""
                        horariTancament = ""
                        email = ""
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = selectedCenter != null
            ) {
                Text("Modificació")
            }
        }
        Row {
            Button(
                onClick = {
                    if (selectedCenter != null) {
                        viewModel.deleteCentre(selectedCenter!!)
                        selectedCenter = null
                        nom = ""
                        direccio = ""
                        telefon = ""
                        horariObertura = ""
                        horariTancament = ""
                        email = ""
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = selectedCenter != null
            ) {
                Text("Baixa")
            }
            // Botó de consulta per refrescar la llista
            Button(
                onClick = {
                    viewModel.loadCentres()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Consulta")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(centers) { center ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Omplim el formulari amb les dades del centre seleccionat
                            selectedCenter = center
                            nom = center.nom
                            direccio = center.direccio
                            telefon = center.telefon
                            horariObertura = center.horari_opertura
                            horariTancament = center.horari_tancament
                            email = center.email
                        }
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = center.nom, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CenterCrudScreenPreview() {
    val previewViewModel = CenterViewModel()
    CenterScreen(
        navigate = {},
        viewModel = previewViewModel
    )
}