package com.rubennicolasdiaz.deslimfleet.viewmodel

import androidx.navigation.NavHostController
import com.rubennicolasdiaz.deslimfleet.R
import com.rubennicolasdiaz.deslimfleet.model.enums.Zona
import com.rubennicolasdiaz.deslimfleet.view.NavigationRoutes
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SeleccionarZonaViewModelTest {

    private lateinit var viewModel: SeleccionZonaViewModel
    private val navHostController: NavHostController = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = SeleccionZonaViewModel(navHostController)
    }

    @Test
    fun `zonas contiene los valores correctos`() {
        val expectedZonas = listOf(
            Zona.ACOMODACION.nombre to R.drawable.acomodacion,
            Zona.AUTOSERVICIO.nombre to R.drawable.autoservicio,
            Zona.BAR.nombre to R.drawable.bar,
            Zona.COCINA.nombre to R.drawable.cocina
        )

        assertEquals(expectedZonas, viewModel.zonas)
    }

    @Test
    fun `seleccionarZona navega a la ruta correcta para cada zona`() {
        val barcoNombre = "Ciudad de Barcelona"

        val zonasConRutas = mapOf(
            Zona.ACOMODACION.nombre to NavigationRoutes.Acomodacion.createRoute(barcoNombre),
            Zona.AUTOSERVICIO.nombre to NavigationRoutes.Autoservicio.createRoute(barcoNombre),
            Zona.BAR.nombre to NavigationRoutes.Bar.createRoute(barcoNombre),
            Zona.COCINA.nombre to NavigationRoutes.Cocina.createRoute(barcoNombre)
        )

        zonasConRutas.forEach { (zonaNombre, expectedRoute) ->
            viewModel.seleccionarZona(barcoNombre, zonaNombre)

            verify { navHostController.navigate(expectedRoute) }
        }
    }

    @Test
    fun `seleccionarZona con zona desconocida navega a acomodacion por defecto`() {
        val barcoNombre = "Ciudad de Barcelona"
        val zonaDesconocida = "Zona Desconocida"
        val expectedRoute = NavigationRoutes.Acomodacion.createRoute(barcoNombre)

        viewModel.seleccionarZona(barcoNombre, zonaDesconocida)

        verify { navHostController.navigate(expectedRoute) }
    }
}
