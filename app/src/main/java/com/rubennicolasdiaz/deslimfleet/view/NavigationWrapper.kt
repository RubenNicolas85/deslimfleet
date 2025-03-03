package com.rubennicolasdiaz.deslimfleet.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rubennicolasdiaz.deslimfleet.di.DependencyProvider
import com.rubennicolasdiaz.deslimfleet.view.screens.appscreens.EstadoRevisionesScreen
import com.rubennicolasdiaz.deslimfleet.view.screens.appscreens.InformesScreen
import com.rubennicolasdiaz.deslimfleet.view.screens.appscreens.LoginScreen
import com.rubennicolasdiaz.deslimfleet.view.screens.appscreens.SeleccionarBarco
import com.rubennicolasdiaz.deslimfleet.view.screens.appscreens.SeleccionarZona
import com.rubennicolasdiaz.deslimfleet.view.screens.appscreens.SplashScreen
import com.rubennicolasdiaz.deslimfleet.view.screens.zones.AcomodacionScreen
import com.rubennicolasdiaz.deslimfleet.view.screens.zones.AutoservicioScreen
import com.rubennicolasdiaz.deslimfleet.view.screens.zones.BarScreen
import com.rubennicolasdiaz.deslimfleet.view.screens.zones.CocinaScreen
import com.rubennicolasdiaz.deslimfleet.viewmodel.EstadoRevisionesViewModel
import com.rubennicolasdiaz.deslimfleet.viewmodel.InformesViewModel
import com.rubennicolasdiaz.deslimfleet.viewmodel.LoginViewModel
import com.rubennicolasdiaz.deslimfleet.viewmodel.SeleccionBarcoViewModel
import com.rubennicolasdiaz.deslimfleet.viewmodel.SeleccionZonaViewModel
import com.rubennicolasdiaz.deslimfleet.viewmodel.UIZonaViewModel

/**
 * Configuración del sistema de navegación de la aplicación.
 *
 * Define las rutas y las pantallas asociadas utilizando Compose Navigation.
 *
 * @param navHostController Controlador de navegación para gestionar las transiciones entre pantallas.
 */
@Composable
fun NavigationWrapper(navHostController: NavHostController) {

    // Obtener casos de uso desde `DependencyProvider`
    val uIZonaViewModel = UIZonaViewModel(DependencyProvider.guardarCheckListUseCase)
    val loginViewModel = LoginViewModel(navHostController, DependencyProvider.obtenerTipoUsuarioUseCase)
    val estadoRevisionesViewModel = EstadoRevisionesViewModel(navHostController, DependencyProvider.obtenerEstadoRevisionesUseCase)
    val seleccionBarcoViewModel = SeleccionBarcoViewModel(navHostController)
    val seleccionZonaViewModel = SeleccionZonaViewModel(navHostController)
    val informesViewModel = InformesViewModel(
        DependencyProvider.obtenerTodasInspeccionesUseCase,
        DependencyProvider.enviarPromptGeminiUseCase,
        DependencyProvider.generarPdfUseCase
    )

    NavHost(navController = navHostController, startDestination = NavigationRoutes.Splash.route) {

        composable(NavigationRoutes.Splash.route) {
            SplashScreen(navHostController)
        }

        composable(NavigationRoutes.Login.route) {
            LoginScreen(loginViewModel)
        }
        composable(NavigationRoutes.Informes.route) {
            InformesScreen(informesViewModel)
        }

        composable(NavigationRoutes.EstadoRevisiones.route) {
            EstadoRevisionesScreen(estadoRevisionesViewModel)
        }

        composable(NavigationRoutes.ShipSelection.route) {
            SeleccionarBarco(seleccionBarcoViewModel)
        }

        composable(NavigationRoutes.ZoneSelection.route) { backStackEntry ->
            val barcoNombre = backStackEntry.arguments?.getString("barcoNombre") ?: ""
            SeleccionarZona(seleccionZonaViewModel, barcoNombre)
        }

        composable(NavigationRoutes.Acomodacion.route) { backStackEntry ->
            val barcoNombre = backStackEntry.arguments?.getString("barcoNombre") ?: ""
            AcomodacionScreen(
                navHostController, uIZonaViewModel, barcoNombre
            )
        }

        composable(NavigationRoutes.Autoservicio.route) { backStackEntry ->
            val barcoNombre = backStackEntry.arguments?.getString("barcoNombre") ?: ""
            AutoservicioScreen(navHostController, uIZonaViewModel, barcoNombre)
        }

        composable(NavigationRoutes.Bar.route) { backStackEntry ->
            val barcoNombre = backStackEntry.arguments?.getString("barcoNombre") ?: ""
            BarScreen(navHostController, uIZonaViewModel, barcoNombre)
        }

        composable(NavigationRoutes.Cocina.route) { backStackEntry ->
            val barcoNombre = backStackEntry.arguments?.getString("barcoNombre") ?: ""
            CocinaScreen(navHostController, uIZonaViewModel, barcoNombre)
        }
    }
}





