package com.rubennicolasdiaz.deslimfleet.model.domain.usecases

import android.content.Context
import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.PDFGenerateRepository

/**
 * Caso de uso para generar un archivo PDF basado en una inspección.
 *
 * @property repository Repositorio responsable de la generación de PDFs.
 */
class GenerarPdfUseCase(private val repository: PDFGenerateRepository) {

    /**
     * Ejecuta la acción de generación de un archivo PDF.
     *
     * @param context Contexto de la aplicación.
     * @param inspeccion Datos de la inspección a incluir en el PDF.
     * @param barco Nombre del barco asociado al informe.
     * @param zona Zona inspeccionada.
     * @param fecha Fecha de la inspección.
     * @param respuestaGemini Evaluación de la inspección generada por la IA Gemini.
     */
    suspend operator fun invoke(
        context: Context,
        inspeccion: Map<String, Any>?,
        barco: String,
        zona: String,
        fecha: String,
        respuestaGemini: String
    ) {
        repository.crearPdf(context, inspeccion, barco, zona, fecha, respuestaGemini)
    }
}
