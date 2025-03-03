package com.rubennicolasdiaz.deslimfleet.model.enums

/**
 * Enumeración que representa las diferentes zonas de un barco.
 *
 * @property nombre Nombre de la zona.
 */
enum class Zona(val nombre: String) {

    /** Zona de acomodación. */
    ACOMODACION("Acomodación"),

    /** Zona de autoservicio. */
    AUTOSERVICIO("Autoservicio"),

    /** Zona de bar. */
    BAR("Bar"),

    /** Zona de cocina. */
    COCINA("Cocina");
}
