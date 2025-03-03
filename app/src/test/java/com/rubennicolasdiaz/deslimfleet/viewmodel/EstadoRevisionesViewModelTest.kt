package com.rubennicolasdiaz.deslimfleet.viewmodel

import androidx.navigation.NavHostController
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.ObtenerEstadoRevisionesUseCase
import com.rubennicolasdiaz.deslimfleet.model.enums.EstadoRevision
import com.rubennicolasdiaz.deslimfleet.view.NavigationRoutes
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class EstadoRevisionesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: EstadoRevisionesViewModel
    private val navHostController: NavHostController = mockk(relaxed = true)
    private val obtenerEstadoRevisionesUseCase: ObtenerEstadoRevisionesUseCase = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = EstadoRevisionesViewModel(navHostController, obtenerEstadoRevisionesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Restablecer Dispatcher después de cada test
    }

    @Test
    fun `cargarEstados actualiza estadoRevisiones correctamente`() = runTest {
        val expectedEstados = viewModel.barcosAgrupados.flatMap { (_, zonas) ->
            zonas.map { it to EstadoRevision.REVISADO }
        }.toMap()

        // Simular el UseCase para que siempre devuelva "REVISADO"
        coEvery { obtenerEstadoRevisionesUseCase.invoke(any(), any()) } returns EstadoRevision.REVISADO

        // Ejecutar la función
        viewModel.cargarEstados()

        // Avanzar el test hasta que todas las coroutines terminen
        advanceUntilIdle()

        // Verificar el estado final
        assertEquals(expectedEstados, viewModel.estadoRevisiones.value)
    }


    @Test
    fun `navigateSeleccionBarco llama correctamente a la navegación`() {
        viewModel.navigateSeleccionBarco()
        verify { navHostController.navigate(NavigationRoutes.ShipSelection.route) }
    }
}


