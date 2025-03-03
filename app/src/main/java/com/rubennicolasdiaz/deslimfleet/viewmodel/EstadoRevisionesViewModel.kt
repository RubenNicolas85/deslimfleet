package com.rubennicolasdiaz.deslimfleet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.ObtenerEstadoRevisionesUseCase
import com.rubennicolasdiaz.deslimfleet.view.NavigationRoutes
import com.rubennicolasdiaz.deslimfleet.model.enums.Barco
import com.rubennicolasdiaz.deslimfleet.model.enums.EstadoRevision
import com.rubennicolasdiaz.deslimfleet.model.enums.Zona
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la obtención y visualización del estado de revisiones de las zonas de los barcos.
 *
 * @param navHostController Controlador de navegación para gestionar la navegación entre pantallas.
 * @param obtenerEstadoRevisionesUseCase Caso de uso para obtener el estado de revisión de una zona específica.
 */
class EstadoRevisionesViewModel(
    private val navHostController: NavHostController,
    private val obtenerEstadoRevisionesUseCase: ObtenerEstadoRevisionesUseCase
) : ViewModel() {

    /** Estado actual de las revisiones, mapeando barcos y zonas a su estado de revisión. */
    private val _estadoRevisiones =
        MutableStateFlow<Map<Pair<Barco, Zona>, EstadoRevision?>>(emptyMap())
    val estadoRevisiones: StateFlow<Map<Pair<Barco, Zona>, EstadoRevision?>> = _estadoRevisiones

    /** Lista de combinaciones de barcos y zonas a inspeccionar. */
    private val _barcoYZona = listOf(
        Barco.CIUDAD_BARCELONA to Zona.ACOMODACION,
        Barco.CIUDAD_BARCELONA to Zona.AUTOSERVICIO,
        Barco.CIUDAD_BARCELONA to Zona.BAR,
        Barco.CIUDAD_BARCELONA to Zona.COCINA,
        Barco.CIUDAD_GRANADA to Zona.ACOMODACION,
        Barco.CIUDAD_GRANADA to Zona.AUTOSERVICIO,
        Barco.CIUDAD_GRANADA to Zona.BAR,
        Barco.CIUDAD_GRANADA to Zona.COCINA,
        Barco.CIUDAD_PALMA to Zona.ACOMODACION,
        Barco.CIUDAD_PALMA to Zona.AUTOSERVICIO,
        Barco.CIUDAD_PALMA to Zona.BAR,
        Barco.CIUDAD_PALMA to Zona.COCINA,
        Barco.CIUDAD_SOLLER to Zona.ACOMODACION,
        Barco.CIUDAD_SOLLER to Zona.AUTOSERVICIO,
        Barco.CIUDAD_SOLLER to Zona.BAR,
        Barco.CIUDAD_SOLLER to Zona.COCINA,
    )

    /** Agrupación de barcos con sus respectivas zonas. */
    val barcosAgrupados: Map<Barco, List<Pair<Barco, Zona>>> = _barcoYZona.groupBy { it.first }

    /**
     * Carga los estados de revisión de las zonas de los barcos.
     */
    fun cargarEstados() {
        viewModelScope.launch {
            val estados = _barcoYZona.associateWith { (barco, zona) ->
                obtenerEstadoRevisionesUseCase.invoke(barco, zona)
            }
            _estadoRevisiones.value = estados
        }
    }

    /**
     * Navega a la pantalla de selección de barco.
     */
    fun navigateSeleccionBarco() {
        navHostController.navigate(NavigationRoutes.ShipSelection.route)
    }
}
