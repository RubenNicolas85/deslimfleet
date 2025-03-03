package com.rubennicolasdiaz.deslimfleet.model.domain.datamodels

/**
 * Jerarquía sellada que describe el estado de la generación de texto en la UI.
 */
sealed interface UIStateGemini {

    /**
     * Estado inicial cuando la pantalla se muestra por primera vez.
     */
    object Initial : UIStateGemini

    /**
     * Estado de carga mientras se genera el texto.
     */
    object Loading : UIStateGemini

    /**
     * Estado de éxito cuando el texto ha sido generado correctamente.
     *
     * @property outputText El texto generado con éxito.
     */
    data class Success(val outputText: String) : UIStateGemini

    /**
     * Estado de error cuando ocurre un problema en la generación del texto.
     *
     * @property errorMessage Mensaje descriptivo del error.
     */
    data class Error(val errorMessage: String) : UIStateGemini
}
