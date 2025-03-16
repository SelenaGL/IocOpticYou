package com.example.opticyou

import android.annotation.SuppressLint
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
import androidx.compose.material3.TopAppBar
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
//import com.example.opticyou.ui.AddScreen
import com.example.opticyou.ui.LoginScreen
import com.example.opticyou.ui.MenuAdminScreen
import com.example.opticyou.ui.MenuUserScreen

//import com.example.opticyou.ui.QueryScreen
//import com.example.opticyou.ui.UsersListScreen


enum class Screens(val title: String) {
    Login(title = "Inici sessió"),
    UserMenu(title = "Menú Usuari"),
    AdminMenu(title = "Menú Administrador"),
    Query(title = "Query"),
    List(title = "List"),
    Add(title = "Add")
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@Composable
fun OpticYouDemoAppBar(
    currentScreen: Screens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.logo1def),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

/*
    App screen. It shows AppBar and, down of it, the other screens
 */

@Preview
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OpticYouDemoApp(

    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screens.valueOf(
        backStackEntry?.destination?.route ?: Screens.Login.name
    )
    Scaffold(
        topBar = {
            OpticYouDemoAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
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
                                    println("Navegant a Screens.AdminMenu")
                                    navController.navigate(Screens.AdminMenu.name) {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                                "client" -> {
                                    println("Navegant a Screens.UserMenu")
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

//            composable(route = Screens.List.name) {
//                UsersListScreen(
//                    modifier = Modifier.fillMaxHeight()
//                )
//            }
//
//            composable(route = Screens.Query.name) {
//                QueryScreen(
//                    modifier = Modifier.fillMaxHeight()
//                )
//
//            }
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

