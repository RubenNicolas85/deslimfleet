package com.rubennicolasdiaz.deslimfleet.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.ObtenerTipoUsuarioUseCase
import com.rubennicolasdiaz.deslimfleet.view.NavigationRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel para gestionar el inicio de sesión en la aplicación.
 *
 * Maneja la autenticación con Firebase, la validación de credenciales y la navegación tras el inicio de sesión.
 *
 * @param navHostController Controlador de navegación para manejar la transición de pantallas.
 * @param obtenerTipoUsuarioUseCase Caso de uso para obtener el tipo de usuario autenticado.
 */
class LoginViewModel(
    private val navHostController: NavHostController,
    private val obtenerTipoUsuarioUseCase: ObtenerTipoUsuarioUseCase,
) : ViewModel() {

    private val auth = Firebase.auth

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnabled = MutableLiveData<Boolean>()
    val loginEnabled: LiveData<Boolean> = _loginEnabled

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    /**
     * Actualiza los valores del email y la contraseña e intenta habilitar el botón de login.
     *
     * @param email Dirección de correo electrónico ingresada por el usuario.
     * @param password Contraseña ingresada por el usuario.
     */
    fun onValueChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnabled.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword(password: String): Boolean = password.length > 5

    /**
     * Inicia sesión con Firebase Authentication.
     */
    fun onLoginSelected() {
        auth.signInWithEmailAndPassword(email.value.toString(), password.value.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModelScope.launch(Dispatchers.IO) {
                        navigateToInformesOrEstados(auth, obtenerTipoUsuarioUseCase, navHostController)
                    }
                } else {
                    _errorMessage.postValue("ERROR DE LOGIN: El email del usuario, la contraseña o ambos son incorrectos.")
                }
            }
    }

    /**
     * Limpia el mensaje de error.
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Redirige al usuario a la pantalla de informes o estado de revisiones según su tipo de usuario.
     *
     * @param auth Instancia de Firebase Authentication.
     * @param obtenerTipoUsuarioUseCase Caso de uso para obtener el tipo de usuario autenticado.
     * @param navHostController Controlador de navegación para manejar la transición de pantallas.
     */
    private suspend fun navigateToInformesOrEstados(
        auth: FirebaseAuth,
        obtenerTipoUsuarioUseCase: ObtenerTipoUsuarioUseCase,
        navHostController: NavHostController
    ) {
        try {
            val emailUsuario = auth.currentUser?.email ?: ""

            if (emailUsuario.isNotEmpty()) {
                val tipoUsuario = obtenerTipoUsuarioUseCase.invoke(emailUsuario).toString()

                val destination = if (tipoUsuario == "Administración") {
                    NavigationRoutes.Informes.route
                } else {
                    NavigationRoutes.EstadoRevisiones.route
                }

                withContext(Dispatchers.Main) {
                    navHostController.navigate(destination)
                }
            } else {
                withContext(Dispatchers.Main) {
                    navHostController.navigate(NavigationRoutes.EstadoRevisiones.route)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _errorMessage.postValue("Error al iniciar sesión. Inténtelo de nuevo.")
            }
        }
    }
}
