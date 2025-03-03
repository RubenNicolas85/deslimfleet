package com.rubennicolasdiaz.deslimfleet.model.domain.usecases

import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.CheckBoxInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.FotoInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.ObservacionesInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.FirebaseRepository

/**
 * Caso de uso para guardar una lista de verificación en Firebase.
 *
 * @property repository Repositorio de Firebase utilizado para almacenar la lista de verificación.
 */
class GuardarCheckListUseCase(private val repository: FirebaseRepository) {

    /**
     * Ejecuta la acción de guardar una lista de verificación en Firebase.
     *
     * @param currentUser Usuario actual que realiza la inspección.
     * @param barcoNombre Nombre del barco inspeccionado.
     * @param zonaNombre Nombre de la zona inspeccionada.
     * @param checkBoxYes Map de checkboxes marcados como "Sí".
     * @param checkBoxNo Map de checkboxes marcados como "No".
     * @param checkBoxPlaga Map de checkboxes marcados como "Plaga".
     * @param checkBoxReparacion Map de checkboxes marcados como "Reparación".
     * @param observaciones Map de observaciones registradas.
     * @param fotos Map de fotos capturadas en la inspección.
     * @param onSuccess Callback a ejecutar en caso de éxito.
     * @param onFailure Callback a ejecutar en caso de error, recibiendo una excepción.
     */
    suspend operator fun invoke(
        currentUser: String,
        barcoNombre: String,
        zonaNombre: String,
        checkBoxYes: Map<String, CheckBoxInfo>,
        checkBoxNo: Map<String, CheckBoxInfo>,
        checkBoxPlaga: Map<String, CheckBoxInfo>,
        checkBoxReparacion: Map<String, CheckBoxInfo>,
        observaciones: Map<String, ObservacionesInfo>,
        fotos: Map<String, FotoInfo>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.guardarChecklist(
            currentUser,
            barcoNombre,
            zonaNombre,
            checkBoxYes,
            checkBoxNo,
            checkBoxPlaga,
            checkBoxReparacion,
            observaciones,
            fotos,
            onSuccess,
            onFailure
        )
    }
}