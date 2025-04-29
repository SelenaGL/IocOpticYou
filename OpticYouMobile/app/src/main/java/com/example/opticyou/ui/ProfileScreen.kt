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
 * Pantalla composable per gestionar el CRUD del rol client.
 *
 * @param modifier Modifier opcional.
 * @param navigate Funció de navegació (per exemple, al login).
 * @param viewModel ViewModel que gestiona els clients.
 */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navigate: (LoginResponse) -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current

    // Recuperem el token des de SharedPreferences
    val sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)
    val storedToken = sharedPreferences.getString("session_token", "") ?: ""
    val userId = sharedPreferences.getLong("session_user_id", 0L)
    viewModel.setAuthData(storedToken, userId)


    // Variables per al formulari
    var selectedClient by remember { mutableStateOf<Client?>(null) }
    var nom by remember { mutableStateOf("") }
    var contrasenya by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefon by remember { mutableStateOf("") }
    var sexe by remember { mutableStateOf("") }
    var dataNaixament by remember { mutableStateOf("") }

    // Observem el client des del ViewModel
    val client by viewModel.client.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Perfil Personal", style = MaterialTheme.typography.headlineSmall)
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

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botó "Consulta"
            Button(
                onClick = {
                    viewModel.loadProfile { success ->
                        if (success) {
                            viewModel.client.value?.let { loaded ->
                                nom = loaded.nom
                                email = loaded.email
                                contrasenya = "****"
                                telefon = loaded.telefon
                                sexe = loaded.sexe
                                dataNaixament = loaded.dataNaixament

                                selectedClient = loaded
                            }
                            Toast.makeText(
                                context,
                                "Client carregat correctament",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Error al carregar el client",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Consulta")
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
                            dataNaixament = dataNaixament
                        )
                        viewModel.updateProfile(updatedClient) { success ->
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Client actualitzat correctament",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error en l'actualització",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        selectedClient = null
                        nom = ""
                        email = ""
                        contrasenya = ""
                        telefon = ""
                        sexe = ""
                        dataNaixament = ""
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
                    viewModel.deleteProfile { success ->
                        if (success) {
                            sharedPreferences.edit().clear().apply()
                            Toast.makeText(
                                context,
                                "Client eliminat correctament",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(context, "Error en l'eliminació", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    selectedClient = null
                    nom = ""
                    email = ""
                    contrasenya = ""
                    telefon = ""
                    sexe = ""
                    dataNaixament = ""
                },
                modifier = Modifier.weight(1f),
                enabled = selectedClient != null
            ) {
                Text("Baixa")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileCrudScreenPreview() {
    val previewViewModel = ProfileViewModel()
    ProfileScreen(navigate = {}, viewModel = previewViewModel)
}