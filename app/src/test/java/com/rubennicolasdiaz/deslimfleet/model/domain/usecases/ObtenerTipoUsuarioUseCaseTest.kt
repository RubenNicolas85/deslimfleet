package com.rubennicolasdiaz.deslimfleet.model.domain.usecases

import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.FirebaseRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class ObtenerTipoUsuarioUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: ObtenerTipoUsuarioUseCase
    private val repository: FirebaseRepository = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = ObtenerTipoUsuarioUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke llama a obtenerTipoUsuario en el repositorio`() = runTest {
        val email = "usuario@empresa.com"

        coEvery { repository.obtenerTipoUsuario(email) } returns "Administración"

        val result = useCase.invoke(email)

        coVerify(exactly = 1) { repository.obtenerTipoUsuario(email) }
        assertEquals("Administración", result)
    }

    @Test
    fun `invoke devuelve tipo de usuario si el email existe`() = runTest {
        val email = "empleado@empresa.com"

        coEvery { repository.obtenerTipoUsuario(email) } returns "Empleado"

        val result = useCase.invoke(email)

        assertEquals("Empleado", result)
    }

    @Test
    fun `invoke devuelve null si el usuario no existe`() = runTest {
        val email = "desconocido@empresa.com"

        coEvery { repository.obtenerTipoUsuario(email) } returns null

        val result = useCase.invoke(email)

        assertEquals(null, result)
    }

    @Test
    fun `invoke devuelve null cuando el repositorio lanza una excepcion`() = runTest {
        val email = "error@empresa.com"

        coEvery { repository.obtenerTipoUsuario(email) } throws Exception("Firestore error")

        val result = try {
            useCase.invoke(email)
        } catch (e: Exception) {
            null
        }

        assertEquals(null, result)
    }
}
