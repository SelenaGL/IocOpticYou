package com.example.opticyou

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.opticyou.R.dimen
import com.example.opticyou.R.string
import com.example.opticyou.data.DataSourceAdmin
import com.example.opticyou.data.DataSourceUser
import com.example.opticyou.ui.LoginScreen
import com.example.opticyou.ui.MenuAdminScreen
import com.example.opticyou.ui.MenuUserScreen
import androidx.activity.compose.BackHandler
import com.example.opticyou.ui.CenterScreen
import com.example.opticyou.ui.ClientScreen


enum class Screens(val title: String) {
    Login(title = "Inici sessió"),
    UserMenu(title = "Menú Usuari"),
    AdminMenu(title = "Menú Administrador"),
    Centres(title = "Gestió de Centres"),
    Clients (title = "Gestió de Clients"),
    List(title = "List"),
    Add(title = "Add")
}

/**
 * Composable que mostra la barra superior de l'aplicació amb el logotip i el botó de retrocés.
 */
@Composable
fun OpticYouDemoAppBar(
    currentScreen: Screens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = Modifier,
        title = {
            Image(
                painter = painterResource(id = R.drawable.logo1def),
                contentDescription = "Logo",
                modifier = Modifier.size(180.dp)
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(string.back_button)
                    )
                }
            }
        },
    )
}

/**
 * Composable principal de l'aplicació, que gestiona la navegació entre pantalles.
 */

@Preview
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OpticYouDemoApp(

    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screens.valueOf(
        backStackEntry?.destination?.route ?: Screens.Login.name
    )

    // Botó de retrocés en els menús
    BackHandler(enabled = true) {
        if (currentScreen == Screens.AdminMenu || currentScreen == Screens.UserMenu) {
            // Si anem enrere a la pantalla de login, es fa logout complet (elimina token i torna a login)
            val sharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)
            val tokenPrevi = sharedPreferences.getString("session_token", "null")
            println("Token sessió: $tokenPrevi")
            sharedPreferences.edit().remove("session_token").commit()
            // Tornem a llegir el token després de l'eliminació
            val tokenDespres = sharedPreferences.getString("session_token", "null")
            println("Token després de clicar enrere: $tokenDespres")
            navController.navigate(Screens.Login.name) {
                popUpTo(0)
            }
        } else {
            // Per a altres pantalles, només navega enrere sense eliminar el token de la sessió
            navController.navigateUp()
        }
    }
    Scaffold(
        topBar = {
            OpticYouDemoAppBar(
                currentScreen = currentScreen,
                canNavigateBack = true,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.Login.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screens.Login.name) {
                LoginScreen(
                    navigate = { loginResponse ->
                        if (loginResponse.success) {
                            println("Rol: ${loginResponse.rol}")
                            when (loginResponse.rol.trim().lowercase()) {
                                "admin" -> {
                                    println("Navegant a AdminMenu")
                                    navController.navigate(Screens.AdminMenu.name) {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                                "client" -> {
                                    println("Navegant a UserMenu")
                                    navController.navigate(Screens.UserMenu.name) {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                                else -> {
                                    println("No s'ha pogut determinar el rol")
                                    // Per defecte, si no és admin ni user, podríem navegar al menú d'usuari
                                    navController.navigate(Screens.UserMenu.name) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        } else {
                            // Si el login falla, no es navega. La pantalla de login mostrarà el missatge d'error.
                            println("Error en el login")
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(dimen.padding_medium))
                )
            }

            composable(route = Screens.UserMenu.name) {
                val context = LocalContext.current
                MenuUserScreen(
                    onOptionClicked = { navController.navigate(it) },
                    onLogoutClicked = { _, _ ->
                        navController.navigate(Screens.Login.name) {// We clear the stack of screens
                            popUpTo(Screens.UserMenu.name) { inclusive = true }
                            launchSingleTop = true
                        }


                    },
                    options = DataSourceUser.options,
                    modifier = Modifier.fillMaxHeight()
                )
            }

            composable(route = Screens.AdminMenu.name) {
                val context = LocalContext.current
                MenuAdminScreen(
                    onOptionClicked = { navController.navigate(it) },
                    onLogoutClicked = { _, _ ->
                        navController.navigate(Screens.Login.name) {// We clear the stack of screens
                            popUpTo(Screens.AdminMenu.name) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    options = DataSourceAdmin.options,
                    modifier = Modifier.fillMaxHeight()
                )
            }

            composable(route = Screens.Centres.name) {
                CenterScreen(modifier = Modifier.fillMaxHeight())
            }

            composable(route = Screens.Clients.name) {
                ClientScreen(modifier = Modifier.fillMaxHeight())
            }


//
//            composable(route = Screens.Add.name) {
//                AddScreen(
//                    modifier = Modifier.fillMaxHeight()
//                )
//            }

        }
    }
}

/*
    Shows error message if parameter is true
 */

@Composable
fun ShowIOResult(error: Boolean) {
    if (error) {
        Text(stringResource(string.data_could_not_be_processed), color = Color.Red)
    }
}

