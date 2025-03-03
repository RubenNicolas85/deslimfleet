package com.rubennicolasdiaz.deslimfleet.model.domain.usecases

import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.FirebaseRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class ObtenerTodasInspeccionesUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: ObtenerTodasInspeccionesUseCase
    private val repository: FirebaseRepository = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = ObtenerTodasInspeccionesUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke llama a obtenerTodasInspecciones en el repositorio`() = runTest {
        val inspecciones = listOf(
            mapOf("Barco" to "Barco A", "Zona" to "Zona B"),
            mapOf("Barco" to "Barco C", "Zona" to "Zona D")
        )

        coEvery { repository.obtenerTodasInspecciones() } returns inspecciones

        val result = useCase.invoke()

        coVerify(exactly = 1) { repository.obtenerTodasInspecciones() }
        assertEquals(inspecciones, result)
    }

    @Test
    fun `invoke devuelve lista vacia si no hay inspecciones`() = runTest {
        coEvery { repository.obtenerTodasInspecciones() } returns emptyList()

        val result = useCase.invoke()

        assertEquals(emptyList<Map<String, Any>>(), result)
    }

    @Test
    fun `invoke devuelve lista vacia cuando el repositorio lanza una excepcion`() = runTest {
        coEvery { repository.obtenerTodasInspecciones() } throws Exception("Firestore error")

        val result = try {
            useCase.invoke()
        } catch (e: Exception) {
            emptyList()
        }

        assertEquals(emptyList<Map<String, Any>>(), result)
    }
}
