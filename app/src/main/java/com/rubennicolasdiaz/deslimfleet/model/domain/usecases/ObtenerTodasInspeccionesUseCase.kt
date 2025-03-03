package com.rubennicolasdiaz.deslimfleet.model.domain.usecases

import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.FirebaseRepository

/**
 * Caso de uso para obtener todas las inspecciones almacenadas en Firebase.
 *
 * @property repository Repositorio de Firebase utilizado para recuperar las inspecciones.
 */
class ObtenerTodasInspeccionesUseCase(private val repository: FirebaseRepository) {

    /**
     * Ejecuta la acción de obtener todas las inspecciones registradas en Firebase.
     *
     * @return Lista de inspecciones en formato Map.
     */
    suspend operator fun invoke(): List<Map<String, Any>> {
        return repository.obtenerTodasInspecciones()
    }
}
