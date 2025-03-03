package com.rubennicolasdiaz.deslimfleet.model.domain.usecases

import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.GeminiRepository
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.UIStateGemini
import kotlinx.coroutines.flow.StateFlow

/**
 * Caso de uso para enviar un prompt a Gemini.
 *
 * @property repository Repositorio de Gemini utilizado para gestionar la solicitud.
 */
class EnviarPromptGeminiUseCase(private val repository: GeminiRepository) {

    /**
     * Estado actual de la UI que refleja el estado de generación de texto.
     */
    val uiState: StateFlow<UIStateGemini> = repository.uiState

    /**
     * Ejecuta la acción de enviar un prompt a Gemini.
     *
     * @param prompt Texto de entrada que se enviará al modelo.
     */
    suspend operator fun invoke(prompt: String) {
        repository.sendPrompt(prompt)
    }
}

