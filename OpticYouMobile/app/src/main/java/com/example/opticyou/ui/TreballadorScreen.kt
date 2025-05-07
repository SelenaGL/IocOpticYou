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
import com.example.opticyou.data.Treballador
import com.example.opticyou.data.LoginResponse
import androidx.compose.ui.tooling.preview.Preview

/**
 * Pantalla composable per gestionar el CRUD de treballadors.
 *
 * @param modifier Modifier opcional.
 * @param navigate Funció de navegació (per exemple, al login).
 * @param viewModel ViewModel que gestiona els treballadors.
 */
@Composable
fun TreballadorScreen(
    modifier: Modifier = Modifier,
    navigate: (LoginResponse) -> Unit = {},
    viewModel: TreballadorViewModel = viewModel()
) {
    val context = LocalContext.current

    // Recuperem el token des de SharedPreferences
    val sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)
    val storedToken = sharedPreferences.getString("session_token", "") ?: ""
    viewModel.setAuthToken(storedToken)

    // Variables per al formulari
    var selected by remember { mutableStateOf<Treballador?>(null) }
    var nom by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contrasenya by remember { mutableStateOf("") }
    var especialitat by remember { mutableStateOf("") }
    var estat by remember { mutableStateOf("") }
    var inici by remember { mutableStateOf("") }
    var dies by remember { mutableStateOf("") }
    var fi by remember { mutableStateOf("") }
    var clinicaId by remember { mutableStateOf("") }

    // Observem la llista de clients des del ViewModel
    val treballadors by viewModel.treballadors.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Gestió de Treballadors", style = MaterialTheme.typography.headlineSmall)
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
            value = especialitat,
            onValueChange = { especialitat = it },
            label = { Text("Especialitat") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = estat,
            onValueChange = { estat = it },
            label = { Text("Estat") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inici,
            onValueChange = { inici = it },
            label = { Text("Inici Jornada") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))


        OutlinedTextField(

            value = fi,
            onValueChange = { fi = it },
            label = { Text("Fi Jornada") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = clinicaId,
            onValueChange = { clinicaId = it },
            label = { Text("ID Clínica") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botó "Alta"
            Button(
                onClick = {
                    viewModel.addTreballador(
                        nom,
                        email,
                        contrasenya,
                        especialitat,
                        estat,
                        inici,
                        dies,
                        fi,
                        clinicaId.toLongOrNull() ?: 0L
                    ) { success ->
                        if (success) {
                            Toast.makeText(
                                context,
                                "Treballador creat correctament",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Error en la creació del treballador",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    // Reinicia els camps
                    selected = null
                    nom = ""
                    email = ""
                    contrasenya = ""
                    especialitat = ""
                    estat = ""
                    inici = ""
                    dies = ""
                    fi = ""
                    clinicaId = ""
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Alta")
            }
            // Botó "Modificació"
            Button(
                onClick = {
                    if (selected != null) {
                        val updatedTreballador = selected!!.copy(
                            nom = nom,
                            email = email,
                            contrasenya = contrasenya,
                            especialitat = especialitat,
                            estat = estat,
                            iniciJornada = inici,
                            diesJornada = dies,
                            fiJornada = fi,
                            clinicaId = clinicaId.toLongOrNull() ?: selected!!.clinicaId

                        )
                        viewModel.updateTreballador(updatedTreballador) { success ->
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Treballador actualitzat correctament",
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
                        selected = null
                        nom = ""
                        email = ""
                        contrasenya = ""
                        especialitat = ""
                        estat = ""
                        inici = ""
                        dies = ""
                        fi = ""
                        clinicaId = ""
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = selected != null
            ) {
                Text("Modificació")
            }
        }
        Row {
            // Botó "Baixa"
            Button(
                onClick = {
                    if (selected != null) {
                        viewModel.deleteTreballador(selected!!) { success ->
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Treballador eliminat correctament",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(context, "Error en l'eliminació", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        selected = null
                        nom = ""
                        email = ""
                        contrasenya = ""
                        especialitat = ""
                        estat = ""
                        inici = ""
                        dies = ""
                        fi = ""
                        clinicaId = ""
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = selected != null
            ) {
                Text("Baixa")
            }
            // Botó "Consulta"
            Button(
                onClick = {
                    viewModel.loadTreballadors() { success ->
                        if (success) {
                            Toast.makeText(
                                context,
                                "Treballadors carregats correctament",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Error al carregar els treballadors",
                                Toast.LENGTH_SHORT
                            ).show()
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
            items(treballadors) { treballador ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Omple el formulari amb les dades del client seleccionat
                            selected = treballador
                            nom = treballador.nom
                            email = treballador.email
                            contrasenya = "****"
                            especialitat = treballador.especialitat
                            estat = treballador.estat
                            inici = treballador.iniciJornada
                            dies = treballador.diesJornada
                            fi = treballador.fiJornada
                            clinicaId = treballador.clinicaId.toString()
                        }
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = treballador.nom, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TreballadorCrudScreenPreview() {
    val previewViewModel = TreballadorViewModel()
    TreballadorScreen(navigate = {}, viewModel = previewViewModel)
}