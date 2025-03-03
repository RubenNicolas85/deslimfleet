package com.rubennicolasdiaz.deslimfleet.model.enums

/**
 * Enumeración que representa los diferentes barcos disponibles en la aplicación.
 *
 * @property nombre Nombre completo del barco.
 */
enum class Barco(val nombre: String) {

    /** Barco Ciudad de Barcelona. */
    CIUDAD_BARCELONA("Ciudad de Barcelona"),

    /** Barco Ciudad de Granada. */
    CIUDAD_GRANADA("Ciudad de Granada"),

    /** Barco Ciudad de Palma. */
    CIUDAD_PALMA("Ciudad de Palma"),

    /** Barco Ciudad de Sóller. */
    CIUDAD_SOLLER("Ciudad de Sóller");
}