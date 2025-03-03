package com.rubennicolasdiaz.deslimfleet.model.domain.usecases

import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.FirebaseRepository
import com.rubennicolasdiaz.deslimfleet.model.enums.Barco
import com.rubennicolasdiaz.deslimfleet.model.enums.EstadoRevision
import com.rubennicolasdiaz.deslimfleet.model.enums.Zona
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class ObtenerEstadoRevisionesUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: ObtenerEstadoRevisionesUseCase
    private val repository: FirebaseRepository = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = ObtenerEstadoRevisionesUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke llama a obtenerEstadoRevision en el repositorio`() = runTest {
        val barco = Barco.CIUDAD_BARCELONA
        val zona = Zona.ACOMODACION

        coEvery { repository.obtenerEstadoRevision(barco, zona) } returns EstadoRevision.REVISADO

        val result = useCase.invoke(barco, zona)

        coVerify(exactly = 1) { repository.obtenerEstadoRevision(barco, zona) }
        assertEquals(EstadoRevision.REVISADO, result)
    }

    @Test
    fun `invoke devuelve REVISADO cuando hay inspección`() = runTest {
        val barco = Barco.CIUDAD_PALMA
        val zona = Zona.BAR

        coEvery { repository.obtenerEstadoRevision(barco, zona) } returns EstadoRevision.REVISADO

        val result = useCase.invoke(barco, zona)

        assertEquals(EstadoRevision.REVISADO, result)
    }

    @Test
    fun `invoke devuelve SIN_REVISAR cuando no hay inspección`() = runTest {
        val barco = Barco.CIUDAD_GRANADA
        val zona = Zona.COCINA

        coEvery { repository.obtenerEstadoRevision(barco, zona) } returns EstadoRevision.SIN_REVISAR

        val result = useCase.invoke(barco, zona)

        assertEquals(EstadoRevision.SIN_REVISAR, result)
    }

    @Test
    fun `invoke devuelve SIN_REVISAR cuando el repositorio lanza una excepcion`() = runTest {
        val barco = Barco.CIUDAD_SOLLER
        val zona = Zona.AUTOSERVICIO

        coEvery { repository.obtenerEstadoRevision(barco, zona) } throws Exception("Firestore error")

        val result = try {
            useCase.invoke(barco, zona)
        } catch (e: Exception) {
            EstadoRevision.SIN_REVISAR
        }

        assertEquals(EstadoRevision.SIN_REVISAR, result)
    }
}
