package com.rubennicolasdiaz.deslimfleet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.rubennicolasdiaz.deslimfleet.R
import com.rubennicolasdiaz.deslimfleet.model.enums.Barco
import com.rubennicolasdiaz.deslimfleet.view.NavigationRoutes

/**
 * ViewModel para la selección de barcos en la aplicación.
 *
 * Gestiona la lista de barcos disponibles y la navegación hacia la selección de zonas.
 *
 * @param navHostController Controlador de navegación para manejar las transiciones entre pantallas.
 */
class SeleccionBarcoViewModel(private val navHostController: NavHostController) : ViewModel() {

    /** Lista de barcos disponibles con sus respectivas imágenes. */
    val barcos = listOf(
        Barco.CIUDAD_BARCELONA.nombre to R.drawable.ciudad_barcelona,
        Barco.CIUDAD_GRANADA.nombre to R.drawable.ciudad_granada,
        Barco.CIUDAD_PALMA.nombre to R.drawable.ciudad_palma,
        Barco.CIUDAD_SOLLER.nombre to R.drawable.ciudad_soller
    )

    /**
     * Navega a la pantalla de selección de zona para el barco seleccionado.
     *
     * @param nombreBarco Nombre del barco seleccionado.
     */
    fun seleccionarBarco(nombreBarco: String) {
        navHostController.navigate(NavigationRoutes.ZoneSelection.createRoute(nombreBarco))
    }
}
