package com.rubennicolasdiaz.deslimfleet.model.domain.datamodels

/**
 * Modelo de datos que representa un checkbox.
 *
 * @property title Título o etiqueta del checkbox.
 * @property checked Indica si el checkbox está marcado o no (por defecto, `false`).
 * @property onCheckedChange Función de callback que se ejecuta cuando el estado del checkbox cambia.
 */
data class CheckBoxInfo(
    val title: String,
    var checked: Boolean = false,
    var onCheckedChange: (Boolean) -> Unit
)
