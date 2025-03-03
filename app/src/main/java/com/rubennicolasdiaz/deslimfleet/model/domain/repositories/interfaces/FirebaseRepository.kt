package com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces

import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.CheckBoxInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.FotoInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.ObservacionesInfo
import com.rubennicolasdiaz.deslimfleet.model.enums.Barco
import com.rubennicolasdiaz.deslimfleet.model.enums.EstadoRevision
import com.rubennicolasdiaz.deslimfleet.model.enums.Zona

/**
 * Interfaz que define las operaciones para interactuar con Firebase.
 */
interface FirebaseRepository {
    /**
     * Guarda una lista de verificación en Firebase.
     *
     * @param currentUser Usuario actual que realiza la operación.
     * @param barcoNombre Nombre del barco al que pertenece la lista de verificación.
     * @param zonaNombre Nombre de la zona correspondiente.
     * @param checkBoxYes Map con los checkboxes marcados como "Sí".
     * @param checkBoxNo Map con los checkboxes marcados como "No".
     * @param checkBoxPlaga Map con los checkboxes marcados como "Plaga".
     * @param checkBoxReparacion Map con los checkboxes marcados como "Reparación".
     * @param observaciones Map con las observaciones registradas.
     * @param fotos Map con las fotos asociadas.
     * @param onSuccess Callback que se ejecuta si la operación es exitosa.
     * @param onFailure Callback que se ejecuta si la operación falla, proporcionando la excepción.
     */
    suspend fun guardarChecklist(
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
    )

    /**
     * Obtiene el tipo de usuario en función del correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return Tipo de usuario como un String o `null` si no se encuentra.
     */
    suspend fun obtenerTipoUsuario(email: String): String?

    /**
     * Obtiene todas las inspecciones almacenadas en Firebase.
     *
     * @return Lista de mapas que representan las inspecciones.
     */
    suspend fun obtenerTodasInspecciones(): List<Map<String, Any>>

    /**
     * Obtiene el estado de revisión de una zona específica de un barco.
     *
     * @param barco Barco al que pertenece la revisión.
     * @param zona Zona específica del barco.
     * @return Estado actual de la revisión.
     */
    suspend fun obtenerEstadoRevision(barco: Barco, zona: Zona): EstadoRevision
}
