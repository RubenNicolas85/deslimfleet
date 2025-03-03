package com.rubennicolasdiaz.deslimfleet.viewmodel

import androidx.navigation.NavHostController
import com.rubennicolasdiaz.deslimfleet.R
import com.rubennicolasdiaz.deslimfleet.model.enums.Barco
import com.rubennicolasdiaz.deslimfleet.view.NavigationRoutes
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SeleccionBarcoViewModelTest {

    private lateinit var viewModel: SeleccionBarcoViewModel
    private val navHostController: NavHostController = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = SeleccionBarcoViewModel(navHostController)
    }

    @Test
    fun `barcos contiene los valores correctos`() {
        val expectedBarcos = listOf(
            Barco.CIUDAD_BARCELONA.nombre to R.drawable.ciudad_barcelona,
            Barco.CIUDAD_GRANADA.nombre to R.drawable.ciudad_granada,
            Barco.CIUDAD_PALMA.nombre to R.drawable.ciudad_palma,
            Barco.CIUDAD_SOLLER.nombre to R.drawable.ciudad_soller
        )

        assertEquals(expectedBarcos, viewModel.barcos)
    }

    @Test
    fun `seleccionarBarco navega a la ruta correcta`() {
        val nombreBarco = Barco.CIUDAD_BARCELONA.nombre
        val expectedRoute = NavigationRoutes.ZoneSelection.createRoute(nombreBarco)

        viewModel.seleccionarBarco(nombreBarco)

        verify { navHostController.navigate(expectedRoute) }
    }
}
