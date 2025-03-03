package com.rubennicolasdiaz.deslimfleet.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.ObtenerTipoUsuarioUseCase
import com.rubennicolasdiaz.deslimfleet.view.NavigationRoutes
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: LoginViewModel
    private val navHostController: NavHostController = mockk(relaxed = true)
    private val obtenerTipoUsuarioUseCase: ObtenerTipoUsuarioUseCase = mockk()
    private val auth: FirebaseAuth = mockk(relaxed = true)
    private val firebaseUser: FirebaseUser = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(navHostController, obtenerTipoUsuarioUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `email y password invalidos deshabilitan el boton de login`() {
        viewModel.onValueChanged("correo_invalido", "123")
        assertEquals(false, viewModel.loginEnabled.value)
    }

    @Test
    fun `email y password validos habilitan el boton de login`() {
        viewModel.onValueChanged("usuario@ejemplo.com", "password123")
        assertEquals(true, viewModel.loginEnabled.value)
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
    fun `onLoginSelected navega a Informes cuando el usuario es Administracion`() = runTest {
        every { auth.currentUser } returns firebaseUser
        every { firebaseUser.email } returns "admin@empresa.com"
        coEvery { obtenerTipoUsuarioUseCase.invoke("admin@empresa.com") } returns "Administración"

        viewModel.onLoginSelected()
        advanceUntilIdle()

        verify { navHostController.navigate(NavigationRoutes.Informes.route) }
    }

    @Test
    fun `onLoginSelected navega a EstadoRevisiones cuando el usuario no es Administracion`() = runTest {
        every { auth.currentUser } returns firebaseUser
        every { firebaseUser.email } returns "usuario@empresa.com"
        coEvery { obtenerTipoUsuarioUseCase.invoke("usuario@empresa.com") } returns "Usuario"

        viewModel.onLoginSelected()
        advanceUntilIdle()

        verify { navHostController.navigate(NavigationRoutes.EstadoRevisiones.route) }
    }

    @Test
    fun `onLoginSelected muestra error cuando login falla`() = runTest {
        // Simular un Task<AuthResult> fallido
        val task: com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> = mockk {
            every { isSuccessful } returns false
        }

        // Simular la llamada de Firebase
        every { auth.signInWithEmailAndPassword(any(), any()) } returns task

        // Crear un observer para monitorear cambios en `errorMessage`
        val observer: Observer<String?> = mockk(relaxed = true)
        viewModel.errorMessage.observeForever(observer)

        // Ejecutar la función
        viewModel.onLoginSelected()

        // Esperar a que las coroutines terminen
        advanceUntilIdle()

        // Verificar que el mensaje de error se haya actualizado correctamente
        assertEquals(
            "ERROR DE LOGIN: El email del usuario, la contraseña o ambos son incorrectos.",
            viewModel.errorMessage.value
        )

        // Eliminar el observer para evitar memory leaks
        viewModel.errorMessage.removeObserver(observer)
    }
}
