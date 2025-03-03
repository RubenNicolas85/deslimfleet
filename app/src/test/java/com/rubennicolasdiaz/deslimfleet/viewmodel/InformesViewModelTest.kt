package com.rubennicolasdiaz.deslimfleet.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.EnviarPromptGeminiUseCase
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.GenerarPdfUseCase
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.ObtenerTodasInspeccionesUseCase
import com.rubennicolasdiaz.deslimfleet.model.enums.Barco
import com.rubennicolasdiaz.deslimfleet.model.enums.Zona
import com.rubennicolasdiaz.deslimfleet.utilities.ObtenerFechaHoy
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class InformesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: InformesViewModel
    private val obtenerTodasInspeccionesUseCase: ObtenerTodasInspeccionesUseCase = mockk()
    private val enviarPromptGeminiUseCase: EnviarPromptGeminiUseCase = mockk(relaxed = true)
    private val generarPdfUseCase: GenerarPdfUseCase = mockk(relaxed = true)
    private val context: Context = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = InformesViewModel(
            obtenerTodasInspeccionesUseCase,
            enviarPromptGeminiUseCase,
            generarPdfUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `valores iniciales son correctos`() {
        assertEquals(Barco.CIUDAD_BARCELONA, viewModel.selectedBarco.value)
        assertEquals(Zona.ACOMODACION, viewModel.selectedZona.value)
        assertEquals(ObtenerFechaHoy.getTodayDate(), viewModel.selectedDate.value)
    }

    @Test
    fun `setSelectedBarco actualiza el barco correctamente`() {
        viewModel.setSelectedBarco(Barco.CIUDAD_PALMA)
        assertEquals(Barco.CIUDAD_PALMA, viewModel.selectedBarco.value)
    }

    @Test
    fun `setSelectedZona actualiza la zona correctamente`() {
        viewModel.setSelectedZona(Zona.COCINA)
        assertEquals(Zona.COCINA, viewModel.selectedZona.value)
    }

    @Test
    fun `setSelectedDate actualiza la fecha correctamente`() {
        val nuevaFecha = "2025-02-28"
        viewModel.setSelectedDate(nuevaFecha)
        assertEquals(nuevaFecha, viewModel.selectedDate.value)
    }

    @Test
    fun `clearError limpia el mensaje de error`() {
        val observer: Observer<String?> = mockk(relaxed = true)
        viewModel.errorMessage.observeForever(observer)

        viewModel.clearError()
        assertEquals(null, viewModel.errorMessage.value)

        viewModel.errorMessage.removeObserver(observer)
    }

    @Test
    fun `generarInforme con inspección encontrada ejecuta UseCases`() = runTest {
        // Configurar valores en la ViewModel antes de ejecutar el test
        viewModel.setSelectedBarco(Barco.CIUDAD_BARCELONA)
        viewModel.setSelectedZona(Zona.ACOMODACION)
        viewModel.setSelectedDate(ObtenerFechaHoy.getTodayDate())

        // Simular inspecciones encontradas
        val inspecciones = listOf(
            mapOf(
                "Barco" to Barco.CIUDAD_BARCELONA.nombre, // Asegurar que coincide con la ViewModel
                "Zona" to Zona.ACOMODACION.nombre, // Asegurar que coincide con la ViewModel
                "Fecha" to ObtenerFechaHoy.getTodayDate()
            )
        )

        // Mockear el comportamiento de los UseCases
        coEvery { obtenerTodasInspeccionesUseCase.invoke() } returns inspecciones
        coEvery { enviarPromptGeminiUseCase.invoke(any()) } just Runs
        coEvery { generarPdfUseCase.invoke(any(), any(), any(), any(), any(), any()) } just Runs

        // Ejecutar la función
        viewModel.generarInforme(context)

        // Asegurar que todas las coroutines terminan antes de verificar los mocks
        advanceUntilIdle()

        // Verificar que `enviarPromptGeminiUseCase.invoke()` se llamó exactamente una vez
        coVerify(exactly = 1) { enviarPromptGeminiUseCase.invoke(any()) }

        // Verificar que `generarPdfUseCase.invoke()` se llamó correctamente
        coVerify(exactly = 1) { generarPdfUseCase.invoke(any(), any(), any(), any(), any(), any()) }
    }



    @Test
    fun `generarInforme con inspección no encontrada muestra error`() = runTest {
        // Simular que no se encuentran inspecciones
        coEvery { obtenerTodasInspeccionesUseCase.invoke() } returns emptyList()

        // Crear un observer para monitorear cambios en `errorMessage`
        val observer: Observer<String?> = mockk(relaxed = true)
        viewModel.errorMessage.observeForever(observer)

        // Ejecutar la función
        viewModel.generarInforme(context)

        // Esperar a que las coroutines terminen
        advanceUntilIdle()

        // Verificar que el mensaje de error se haya actualizado correctamente
        assertEquals(
            "No se encontró inspección para el barco, la zona y la fecha seleccionados",
            viewModel.errorMessage.value
        )

        // Eliminar el observer para evitar memory leaks
        viewModel.errorMessage.removeObserver(observer)
    }

}
