package com.example.opticyou.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.opticyou.R
import com.example.opticyou.data.LoginResponse
import com.example.opticyou.ui.theme.OpticYouTheme

/**
 * Composable que permet als usuaris iniciar sessió a l'aplicació.
 *
 * @param navigate Funció de navegació quan el login és correcte.
 * @param viewModel ViewModel de la pantalla de login.
 * @param modifier
 */
@Composable
fun LoginScreen(
    navigate: (LoginResponse) -> Unit,
    viewModel: LoginViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("token", Context.MODE_PRIVATE)
    }
    //var server by remember {mutableStateOf("")}
    //var port by remember {mutableStateOf("")}
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Títol
        Text(text = "Inici de sessió", fontSize = 25.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // Camps d'entrada
        OutlinedTextField(
            label = { Text("Usuari") },
            value = username,
            onValueChange = { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("usernameField") //tag pel test d'instrumentació
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            label = { Text("Contrasenya") },
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("passwordField") // tag pel test d'instrumentació
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Botó d'inici de sessió
        Button(
            onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    viewModel.doLogin(username, password) { response ->
                        if (response.success) {
                            // Guardem el token després del login correcte
                            sharedPreferences.edit().putString("session_token", response.token).apply()
                            val tokenGuardat = sharedPreferences.getString("session_token", null)
                            println("Token guardat: $tokenGuardat")
                            navigate(response)
                        } else {
                            showError = true
                        }
                    }
                } else {
                    showError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("loginButton")
        ) {
            Text("Iniciar Sessió")
        }

        if (viewModel.uiState.collectAsState().value.loginTried) {
            if (!viewModel.uiState.collectAsState().value.goodResult) {
                Text(
                    text = stringResource(R.string.connection_error_try_again),
                    color = Color.Red,
                    modifier = Modifier.testTag("loginErrorText")
                )
            }
        }
    }
}


@Preview
@Composable
fun LoginPreview() {
    OpticYouTheme {
        LoginScreen(navigate = { loginResponse ->
            println("LoginResponse: $loginResponse")
        })
    }
}
