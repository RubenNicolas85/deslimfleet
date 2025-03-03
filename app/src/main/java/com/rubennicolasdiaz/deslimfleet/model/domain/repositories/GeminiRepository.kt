package com.rubennicolasdiaz.deslimfleet.model.domain.repositories

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.rubennicolasdiaz.deslimfleet.BuildConfig
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.UIStateGemini
import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.GeminiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Implementación de [GeminiRepository] para interactuar con el modelo generativo de IA.
 */
class GeminiRepository : GeminiRepository {

    /**
     * Estado interno de la UI para reflejar el estado de generación de contenido.
     */
    private val _uiState = MutableStateFlow<UIStateGemini>(UIStateGemini.Initial)
    override val uiState: StateFlow<UIStateGemini> = _uiState.asStateFlow()

    /**
     * Modelo generativo de IA configurado con la clave de API.
     */
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    /**
     * Envía un prompt al modelo generativo y actualiza el estado de la UI con la respuesta.
     *
     * @param prompt Texto de entrada que se enviará al modelo generativo.
     */
    override suspend fun sendPrompt(prompt: String) {
        _uiState.value = UIStateGemini.Loading

        try {
            val response = generativeModel.generateContent(
                content { text(prompt) }
            )
            response.text?.let { outputContent ->
                _uiState.value = UIStateGemini.Success(outputContent)
            } ?: run {
                _uiState.value = UIStateGemini.Error("Respuesta vacía")
            }
        } catch (e: Exception) {
            _uiState.value = UIStateGemini.Error(e.localizedMessage ?: "Error desconocido")
        }
    }
}