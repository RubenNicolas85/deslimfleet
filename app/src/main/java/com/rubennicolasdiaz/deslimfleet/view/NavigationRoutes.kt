package com.rubennicolasdiaz.deslimfleet.view

/**
 * Clase sellada que define las rutas de navegación de la aplicación.
 *
 * Cada objeto representa una pantalla con su respectiva ruta, incluyendo
 * aquellas que requieren parámetros dinámicos.
 *
 * @property route Ruta de la pantalla en el sistema de navegación.
 */
sealed class NavigationRoutes(val route: String) {

    /** Pantalla de bienvenida (Splash). */
    data object Splash : NavigationRoutes("splash")

    /** Pantalla de inicio de sesión. */
    data object Login : NavigationRoutes("login")

    /** Pantalla de generación de informes. */
    data object Informes : NavigationRoutes("informes")

    /** Pantalla de estado de revisiones. */
    data object EstadoRevisiones : NavigationRoutes("estadoRevisiones")

    /** Pantalla de selección de barco. */
    data object ShipSelection : NavigationRoutes("shipSelection")

    /**
     * Pantalla de selección de zona con parámetro de barco.
     *
     * @param barcoNombre Nombre del barco seleccionado.
     */
    data object ZoneSelection : NavigationRoutes("zoneSelection/{barcoNombre}") {
        fun createRoute(barcoNombre: String) = "zoneSelection/$barcoNombre"
    }

    /**
     * Pantalla de inspección de Acomodación.
     *
     * @param barcoNombre Nombre del barco en inspección.
     */
    data object Acomodacion : NavigationRoutes("acomodacion/{barcoNombre}") {
        fun createRoute(barcoNombre: String) = "acomodacion/$barcoNombre"
    }

    /**
     * Pantalla de inspección de Autoservicio.
     *
     * @param barcoNombre Nombre del barco en inspección.
     */
    data object Autoservicio : NavigationRoutes("autoservicio/{barcoNombre}") {
        fun createRoute(barcoNombre: String) = "autoservicio/$barcoNombre"
    }

    /**
     * Pantalla de inspección del Bar.
     *
     * @param barcoNombre Nombre del barco en inspección.
     */
    data object Bar : NavigationRoutes("bar/{barcoNombre}") {
        fun createRoute(barcoNombre: String) = "bar/$barcoNombre"
    }

    /**
     * Pantalla de inspección de Cocina.
     *
     * @param barcoNombre Nombre del barco en inspección.
     */
    data object Cocina : NavigationRoutes("cocina/{barcoNombre}") {
        fun createRoute(barcoNombre: String) = "cocina/$barcoNombre"
    }
}