package com.rubennicolasdiaz.deslimfleet.model.domain.datamodels

/**
 * Modelo de datos que representa una sección de observaciones.
 *
 * @property title Título o etiqueta de la sección de observaciones.
 * @property text Texto ingresado en la sección de observaciones (por defecto, cadena vacía).
 * @property onTextChange Función de callback que se ejecuta cuando el texto cambia.
 */
data class ObservacionesInfo(
    val title: String,
    var text: String = "",
    var onTextChange: (String) -> Unit
)