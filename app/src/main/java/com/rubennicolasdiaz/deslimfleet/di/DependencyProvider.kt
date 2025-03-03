package com.rubennicolasdiaz.deslimfleet.di

import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.FirebaseRepository
import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.PDFGenerateRepository
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.EnviarPromptGeminiUseCase
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.GenerarPdfUseCase
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.GuardarCheckListUseCase
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.ObtenerEstadoRevisionesUseCase
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.ObtenerTipoUsuarioUseCase
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.ObtenerTodasInspeccionesUseCase
import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.GeminiRepository

/**
 * Proveedor de dependencias para la aplicación.
 *
 * Este objeto se encarga de instanciar y proporcionar las dependencias necesarias,
 * como los repositorios y los casos de uso.
 */
object DependencyProvider {

    // Repositorios
    /** Repositorio para interactuar con Firebase. */
    private val firebaseRepository = FirebaseRepository()

    /** Repositorio para interactuar con Gemini. */
    private val geminiRepository = GeminiRepository()

    /** Repositorio para generar archivos PDF. */
    private val pdfGenerateRepository = PDFGenerateRepository()

    // Casos de Uso
    /** Caso de uso para enviar un prompt a Gemini. */
    val enviarPromptGeminiUseCase = EnviarPromptGeminiUseCase(geminiRepository)

    /** Caso de uso para generar un archivo PDF. */
    val generarPdfUseCase = GenerarPdfUseCase(pdfGenerateRepository)

    /** Caso de uso para guardar una lista de verificación en Firebase. */
    val guardarCheckListUseCase = GuardarCheckListUseCase(firebaseRepository)

    /** Caso de uso para obtener el estado de las revisiones desde Firebase. */
    val obtenerEstadoRevisionesUseCase = ObtenerEstadoRevisionesUseCase(firebaseRepository)

    /** Caso de uso para obtener el tipo de usuario desde Firebase. */
    val obtenerTipoUsuarioUseCase = ObtenerTipoUsuarioUseCase(firebaseRepository)

    /** Caso de uso para obtener todas las inspecciones desde Firebase. */
    val obtenerTodasInspeccionesUseCase = ObtenerTodasInspeccionesUseCase(firebaseRepository)
}
