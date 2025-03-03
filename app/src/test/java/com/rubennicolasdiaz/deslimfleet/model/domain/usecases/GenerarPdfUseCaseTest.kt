package com.rubennicolasdiaz.deslimfleet.model.domain.usecases

import android.content.Context
import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.PDFGenerateRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*

@ExperimentalCoroutinesApi
class GenerarPdfUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GenerarPdfUseCase
    private val repository: PDFGenerateRepository = mockk()
    private val context: Context = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = GenerarPdfUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke llama a crearPdf en el repositorio`() = runTest {
        val inspeccion = mapOf("Clave" to "Valor")
        val barco = "Barco A"
        val zona = "Zona B"
        val fecha = "2025-03-01"
        val respuestaGemini = "Resumen generado por IA"

        coEvery { repository.crearPdf(context, inspeccion, barco, zona, fecha, respuestaGemini) } just Runs

        useCase.invoke(context, inspeccion, barco, zona, fecha, respuestaGemini)

        coVerify(exactly = 1) { repository.crearPdf(context, inspeccion, barco, zona, fecha, respuestaGemini) }
    }
}
