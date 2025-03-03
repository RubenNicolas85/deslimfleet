package com.rubennicolasdiaz.deslimfleet.utilities

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Utilidad para obtener la fecha actual en formato `dd/MM/yyyy`.
 */
object ObtenerFechaHoy {

    /**
     * Obtiene la fecha actual en formato `dd/MM/yyyy`.
     *
     * @return Cadena de texto con la fecha actual.
     */
    fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(calendar.time)
    }
}
