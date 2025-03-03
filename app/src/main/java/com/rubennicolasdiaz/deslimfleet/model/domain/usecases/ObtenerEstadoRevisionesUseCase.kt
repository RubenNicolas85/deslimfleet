package com.rubennicolasdiaz.deslimfleet.model.domain.usecases

import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.FirebaseRepository
import com.rubennicolasdiaz.deslimfleet.model.enums.Barco
import com.rubennicolasdiaz.deslimfleet.model.enums.EstadoRevision
import com.rubennicolasdiaz.deslimfleet.model.enums.Zona

/**
 * Caso de uso para obtener el estado de revisión de una zona específica de un barco.
 *
 * @property repository Repositorio de Firebase utilizado para obtener el estado de revisión.
 */
class ObtenerEstadoRevisionesUseCase(private val repository: FirebaseRepository) {

    /**
     * Ejecuta la acción de obtener el estado de revisión de una zona específica de un barco.
     *
     * @param barco Barco al que pertenece la revisión.
     * @param zona Zona específica del barco.
     * @return Estado actual de la revisión.
     */
    suspend operator fun invoke(barco: Barco, zona: Zona): EstadoRevision {
        return repository.obtenerEstadoRevision(barco, zona)
    }
}