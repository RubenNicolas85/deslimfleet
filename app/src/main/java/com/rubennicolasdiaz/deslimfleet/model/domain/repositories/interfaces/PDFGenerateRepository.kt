package com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces

import android.content.Context

/**
 * Interfaz que define las operaciones para la generación de archivos PDF.
 */
interface PDFGenerateRepository {
    /**
     * Crea un archivo PDF con la información proporcionada.
     *
     * @param context Contexto de la aplicación.
     * @param inspeccion Mapa que contiene los datos de la inspección (puede ser `null`).
     * @param barco Nombre del barco asociado al PDF.
     * @param zona Zona correspondiente al barco.
     * @param fecha Fecha de la inspección.
     * @param respuestaGemini Respuesta generada por Gemini para incluir en el PDF.
     */
    suspend fun crearPdf(
        context: Context,
        inspeccion: Map<String, Any>?,
        barco: String,
        zona: String,
        fecha: String,
        respuestaGemini: String
    )
}
