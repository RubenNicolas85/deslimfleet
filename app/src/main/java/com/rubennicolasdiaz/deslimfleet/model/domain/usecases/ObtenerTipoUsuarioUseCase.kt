package com.rubennicolasdiaz.deslimfleet.model.domain.usecases

import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.FirebaseRepository

/**
 * Caso de uso para obtener el tipo de usuario a partir de su correo electrónico.
 *
 * @property repository Repositorio de Firebase utilizado para obtener la información del usuario.
 */
class ObtenerTipoUsuarioUseCase(private val repository: FirebaseRepository) {

    /**
     * Ejecuta la acción de obtener el tipo de usuario a partir de su email.
     *
     * @param email Correo electrónico del usuario.
     * @return Tipo de usuario como un String o `null` si no se encuentra.
     */
    suspend operator fun invoke(email: String): String? {
        return repository.obtenerTipoUsuario(email)
    }
}