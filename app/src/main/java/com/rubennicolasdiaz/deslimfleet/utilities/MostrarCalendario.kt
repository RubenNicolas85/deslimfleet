package com.rubennicolasdiaz.deslimfleet.utilities

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

/**
 * Utilidad para mostrar un selector de fecha (DatePicker) en la aplicación.
 */
object MostrarCalendario {

    /**
     * Muestra un selector de fecha y retorna la fecha seleccionada en formato `dd/MM/yyyy`.
     *
     * @param context Contexto de la aplicación.
     * @param onDateSelected Callback que recibe la fecha seleccionada como una cadena de texto.
     */
    fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        android.app.DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                GregorianCalendar(selectedYear, selectedMonth, selectedDay).time
            )
            onDateSelected(formattedDate)
        }, year, month, day).show()
    }
}