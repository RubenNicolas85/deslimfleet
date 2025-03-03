package com.rubennicolasdiaz.deslimfleet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.rubennicolasdiaz.deslimfleet.R
import com.rubennicolasdiaz.deslimfleet.model.enums.Zona
import com.rubennicolasdiaz.deslimfleet.view.NavigationRoutes

/**
 * ViewModel para la selección de zonas dentro de un barco.
 *
 * Gestiona la lista de zonas disponibles y la navegación hacia la pantalla correspondiente
 * de inspección según la zona seleccionada.
 *
 * @param navHostController Controlador de navegación para manejar las transiciones entre pantallas.
 */
class SeleccionZonaViewModel(private val navHostController: NavHostController) : ViewModel() {

    /** Lista de zonas disponibles con sus respectivas imágenes. */
    val zonas = listOf(
        Zona.ACOMODACION.nombre to R.drawable.acomodacion,
        Zona.AUTOSERVICIO.nombre to R.drawable.autoservicio,
        Zona.BAR.nombre to R.drawable.bar,
        Zona.COCINA.nombre to R.drawable.cocina
    )

    /**
     * Navega a la pantalla correspondiente a la zona seleccionada dentro del barco.
     *
     * @param barcoNombre Nombre del barco seleccionado.
     * @param zonaNombre Nombre de la zona seleccionada.
     */
    fun seleccionarZona(barcoNombre: String, zonaNombre: String) {
        val ruta = when (zonaNombre) {
            Zona.ACOMODACION.nombre -> NavigationRoutes.Acomodacion.createRoute(barcoNombre)
            Zona.AUTOSERVICIO.nombre -> NavigationRoutes.Autoservicio.createRoute(barcoNombre)
            Zona.BAR.nombre -> NavigationRoutes.Bar.createRoute(barcoNombre)
            Zona.COCINA.nombre -> NavigationRoutes.Cocina.createRoute(barcoNombre)
            else -> NavigationRoutes.Acomodacion.createRoute(barcoNombre)
        }
        navHostController.navigate(ruta)
    }
}