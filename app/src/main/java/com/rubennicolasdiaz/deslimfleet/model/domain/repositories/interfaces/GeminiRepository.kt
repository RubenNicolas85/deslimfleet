package com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces

import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.UIStateGemini
import kotlinx.coroutines.flow.StateFlow

/**
 * Interfaz que define las operaciones para interactuar con el servicio de Gemini.
 */
interface GeminiRepository {
    /**
     * Estado actual de la UI que refleja el estado de generación de texto.
     */
    val uiState: StateFlow<UIStateGemini>

    /**
     * Envía un prompt al servicio de Gemini para generar una respuesta.
     *
     * @param prompt Texto de entrada que se enviará al modelo.
     */
    suspend fun sendPrompt(prompt: String)
}
