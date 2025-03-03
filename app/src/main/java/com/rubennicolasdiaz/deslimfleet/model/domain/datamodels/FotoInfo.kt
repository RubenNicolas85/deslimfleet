package com.rubennicolasdiaz.deslimfleet.model.domain.datamodels

/**
 * Modelo de datos que representa una foto.
 *
 * @property title Título o etiqueta de la foto.
 * @property imageBase64 Imagen en formato Base64 (opcional, puede ser `null`).
 * @property onImageChange Función de callback que se ejecuta cuando la imagen cambia.
 */
data class FotoInfo(
    val title: String,
    var imageBase64: String? = null,
    var onImageChange: (String?) -> Unit
)
