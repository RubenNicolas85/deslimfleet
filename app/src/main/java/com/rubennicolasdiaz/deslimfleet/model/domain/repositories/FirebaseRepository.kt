package com.rubennicolasdiaz.deslimfleet.model.domain.repositories

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.CheckBoxInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.FotoInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.ObservacionesInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.FirebaseRepository
import com.rubennicolasdiaz.deslimfleet.model.enums.Barco
import com.rubennicolasdiaz.deslimfleet.model.enums.EstadoRevision
import com.rubennicolasdiaz.deslimfleet.model.enums.Zona
import com.rubennicolasdiaz.deslimfleet.utilities.ObtenerFechaHoy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Implementación de [FirebaseRepository] para interactuar con Firestore.
 */
class FirebaseRepository : FirebaseRepository {

    private val database = Firebase.firestore

    /**
     * Guarda una lista de verificación en Firebase.
     *
     * @param currentUser Usuario actual que realiza la inspección.
     * @param barcoNombre Nombre del barco.
     * @param zonaNombre Nombre de la zona inspeccionada.
     * @param checkBoxYes Map de checkboxes marcados como "Sí".
     * @param checkBoxNo Map de checkboxes marcados como "No".
     * @param checkBoxPlaga Map de checkboxes marcados como "Plaga".
     * @param checkBoxReparacion Map de checkboxes marcados como "Reparación".
     * @param observaciones Map de observaciones realizadas.
     * @param fotos Map de fotos capturadas codificadas en Base64.
     * @param onSuccess Callback en caso de éxito.
     * @param onFailure Callback en caso de fallo con la excepción correspondiente.
     */
    override suspend fun guardarChecklist(
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
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        val fechaFormateada = dateFormat.format(Date())
        val horaFormateada = timeFormat.format(Date())

        val checklistData = mutableMapOf<String, Any>(
            "Barco" to barcoNombre,
            "Zona" to zonaNombre,
            "Usuario" to currentUser,
            "Fecha" to fechaFormateada,
            "Hora" to horaFormateada,
            "CheckboxYes" to checkBoxYes.mapValues { it.value.checked },
            "CheckboxNo" to checkBoxNo.mapValues { it.value.checked },
            "CheckboxPlaga" to checkBoxPlaga.mapValues { it.value.checked },
            "CheckboxReparacion" to checkBoxReparacion.mapValues { it.value.checked },
            "Observaciones" to observaciones.mapValues { it.value.text },
            "Fotos" to fotos.mapValues { it.value.imageBase64 ?: "" }
        )

        try {
            val existingDocs = database.collection("Inspecciones")
                .whereEqualTo("Barco", barcoNombre)
                .whereEqualTo("Zona", zonaNombre)
                .whereEqualTo("Fecha", fechaFormateada)
                .get()
                .await()

            if (existingDocs.isEmpty) {
                database.collection("Inspecciones").add(checklistData).await()
                withContext(Dispatchers.Main) { onSuccess() }
            } else {
                withContext(Dispatchers.Main) { onFailure(Exception("Inspección duplicada detectada.")) }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) { onFailure(e) }
        }
    }

    /**
     * Obtiene el tipo de usuario basado en su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return Tipo de usuario o `null` si no se encuentra.
     */
    override suspend fun obtenerTipoUsuario(email: String): String? {
        return try {
            val snapshot = database.collection("Usuarios").whereEqualTo("Email", email).get().await()
            snapshot.documents.firstOrNull()?.getString("Tipo_Usuario")
        } catch (e: Exception) {
            Log.e("FirebaseRepository", "Error obteniendo tipo de usuario", e)
            null
        }
    }

    /**
     * Obtiene todas las inspecciones registradas en Firestore.
     *
     * @return Lista de inspecciones en formato Map.
     */
    override suspend fun obtenerTodasInspecciones(): List<Map<String, Any>> {
        return try {
            database.collection("Inspecciones").get().await().documents.mapNotNull { it.data }
        } catch (e: Exception) {
            Log.e("FirebaseRepository", "Error obteniendo inspecciones", e)
            emptyList()
        }
    }

    /**
     * Obtiene el estado de revisión de una zona específica de un barco.
     *
     * @param barco Barco al que pertenece la revisión.
     * @param zona Zona específica del barco.
     * @return Estado de revisión basado en si ya existe un registro para la fecha actual.
     */
    override suspend fun obtenerEstadoRevision(barco: Barco, zona: Zona): EstadoRevision {
        return try {
            val inspecciones = obtenerTodasInspecciones()
            val fechaHoy = ObtenerFechaHoy.getTodayDate()

            val inspeccionHoy = inspecciones.firstOrNull {
                it["Barco"] == barco.nombre && it["Zona"] == zona.nombre && it["Fecha"] == fechaHoy
            }

            inspeccionHoy?.let { EstadoRevision.REVISADO } ?: EstadoRevision.SIN_REVISAR
        } catch (e: Exception) {
            Log.e("FirebaseRepository", "Error verificando estado de revisión", e)
            EstadoRevision.SIN_REVISAR
        }
    }
}
