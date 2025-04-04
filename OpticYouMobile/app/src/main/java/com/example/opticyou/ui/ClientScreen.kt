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
import com.example.opticyou.data.Client
import com.example.opticyou.data.LoginResponse
import androidx.compose.ui.tooling.preview.Preview

/**
 * Pantalla composable per gestionar el CRUD de clients.
 *
 * @param modifier Modifier opcional.
 * @param navigate Funció de navegació (per exemple, al login).
 * @param viewModel ViewModel que gestiona els clients.
 */
@Composable
fun ClientScreen(
    modifier: Modifier = Modifier,
    navigate: (LoginResponse) -> Unit = {},
    viewModel: ClientViewModel = viewModel()
) {
    val context = LocalContext.current

    // Recuperem el token des de SharedPreferences
    val sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)
    val storedToken = sharedPreferences.getString("session_token", "") ?: ""
    viewModel.setAuthToken(storedToken)

    // Variables per al formulari
    var selectedClient by remember { mutableStateOf<Client?>(null) }
    var nom by remember { mutableStateOf("") }
    var contrasenya by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefon by remember { mutableStateOf("") }
    var sexe by remember { mutableStateOf("") }
    var dataNaixament by remember { mutableStateOf("") }
    var clinicaId by remember { mutableStateOf("") }
    var historialId by remember { mutableStateOf("") }

    // Observem la llista de clients des del ViewModel
    val clients by viewModel.clients.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Gestió de Clients", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nom,
            onValueChange = { nom = it },
            label = { Text("Nom") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = contrasenya,
            onValueChange = { contrasenya = it },
            label = { Text("Contrasenya") },
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
            value = sexe,
            onValueChange = { sexe = it },
            label = { Text("Sexe") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = dataNaixament,
            onValueChange = { dataNaixament = it },
            label = { Text("Data de naixement") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = clinicaId,
            onValueChange = { clinicaId = it },
            label = { Text("ID Clínica") },
            modifier = Modifier.fillMaxWidth()
        )
//        Spacer(modifier = Modifier.height(8.dp))
//
//        OutlinedTextField(
//            value = historialId,
//            onValueChange = { historialId = it },
//            label = { Text("ID Historial") },
//            modifier = Modifier.fillMaxWidth()
//        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botó "Alta"
            Button(
                onClick = {
                    viewModel.createClient(
                        nom,
                        email,
                        contrasenya,
                        telefon,
                        sexe,
                        dataNaixament,
                        clinicaId.toLongOrNull() ?: 0L,
                        historialId.toLongOrNull() ?: 0L
                    ) { success ->
                        if (success) {
                            Toast.makeText(context, "Client creat correctament", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Error en la creació del client", Toast.LENGTH_SHORT).show()
                        }
                    }
                    // Reinicia els camps
                    selectedClient = null
                    nom = ""
                    email = ""
                    contrasenya = ""
                    telefon = ""
                    sexe = ""
                    dataNaixament = ""
                    clinicaId = ""
                    //historialId = ""
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Alta")
            }
            // Botó "Modificació"
            Button(
                onClick = {
                    if (selectedClient != null) {
                        val updatedClient = selectedClient!!.copy(
                            nom = nom,
                            email = email,
                            contrasenya = contrasenya,
                            telefon = telefon,
                            sexe = sexe,
                            dataNaixament = dataNaixament,
                            clinicaId = clinicaId.toLongOrNull() ?: selectedClient!!.clinicaId,
                            historialId = historialId.toLongOrNull() ?: selectedClient!!.historialId
                        )
                        viewModel.updateClient(updatedClient) { success ->
                            if (success) {
                                Toast.makeText(context, "Client actualitzat correctament", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Error en l'actualització", Toast.LENGTH_SHORT).show()
                            }
                        }
                        selectedClient = null
                        nom = ""
                        email = ""
                        contrasenya = ""
                        telefon = ""
                        sexe = ""
                        dataNaixament = ""
                        clinicaId = ""
                        //historialId = ""
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = selectedClient != null
            ) {
                Text("Modificació")
            }
        }
        Row {
            // Botó "Baixa"
            Button(
                onClick = {
                    if (selectedClient != null) {
                        viewModel.deleteClient(selectedClient!!) { success ->
                            if (success) {
                                Toast.makeText(context, "Client eliminat correctament", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Error en l'eliminació", Toast.LENGTH_SHORT).show()
                            }
                        }
                        selectedClient = null
                        nom = ""
                        email = ""
                        contrasenya = ""
                        telefon = ""
                        sexe = ""
                        dataNaixament = ""
                        clinicaId = ""
                        //historialId = ""
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = selectedClient != null
            ) {
                Text("Baixa")
            }
            // Botó "Consulta"
            Button(
                onClick = {
                    viewModel.loadClients { success ->
                        if (success) {
                            Toast.makeText(context, "Clients carregats correctament", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Error al carregar els clients", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Consulta")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(clients) { client ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Omple el formulari amb les dades del client seleccionat
                            selectedClient = client
                            nom = client.nom
                            email = client.email
                            contrasenya = "****" //per seguretat no es retorna la contrasenya
                            telefon = client.telefon
                            sexe = client.sexe
                            dataNaixament = client.dataNaixament
                            clinicaId = client.clinicaId.toString()
                            //historialId = client.historialId.toString()
                        }
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = client.nom, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientCrudScreenPreview() {
    val previewViewModel = ClientViewModel()
    ClientScreen(navigate = {}, viewModel = previewViewModel)
}