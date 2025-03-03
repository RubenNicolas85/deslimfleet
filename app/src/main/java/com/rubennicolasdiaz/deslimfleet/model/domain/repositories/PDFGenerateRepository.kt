package com.rubennicolasdiaz.deslimfleet.model.domain.repositories

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.PDFGenerateRepository
import com.rubennicolasdiaz.deslimfleet.utilities.DecodeBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * Implementación de [PDFGenerateRepository] para la generación y visualización de archivos PDF.
 */
class PDFGenerateRepository : PDFGenerateRepository {

    /**
     * Crea un archivo PDF con la información proporcionada y lo abre automáticamente.
     *
     * @param context Contexto de la aplicación.
     * @param inspeccion Mapa con los datos de la inspección (puede contener imágenes en Base64).
     * @param barco Nombre del barco asociado.
     * @param zona Zona específica del barco.
     * @param fecha Fecha de la inspección.
     * @param respuestaGemini Evaluación de la inspección generada por la IA Gemini.
     */
    override suspend fun crearPdf(
        context: Context,
        inspeccion: Map<String, Any>?,
        barco: String,
        zona: String,
        fecha: String,
        respuestaGemini: String
    ) {
        try {
            val file = File(context.cacheDir, "Informe_$barco.pdf")
            val writer = PdfWriter(withContext(Dispatchers.IO) { FileOutputStream(file) })
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc)

            // Agregar logo si está disponible en los recursos
            val drawableId =
                context.resources.getIdentifier("trasmed_logo", "drawable", context.packageName)
            if (drawableId != 0) {
                val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)
                val byteArray = bitmapToByteArray(bitmap)
                val imageData = ImageDataFactory.create(byteArray)
                val image = Image(imageData)
                    .scaleToFit(250f, 250f)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)

                document.add(image)
            }

            // Agregar título del informe
            val title = Paragraph("Informe Inspección de limpieza/desinfección")
                .setFontSize(24f)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
            document.add(title)

            // Detalles de la inspección
            document.add(Paragraph("Barco: $barco").setFontSize(14f))
            document.add(Paragraph("Zona: $zona").setFontSize(14f))
            document.add(Paragraph("Fecha: $fecha").setFontSize(14f))
            document.add(Paragraph("\n"))

            // Evaluación de Gemini
            val analisisGemini = Paragraph(
                "Evaluación de la inspección por la IA Gemini 2.0 de Google: $respuestaGemini"
            ).setFontSize(14f)
            document.add(analisisGemini)

            // Mostrar imágenes de la inspección
            inspeccion?.get("Fotos")?.let { fotos ->
                (fotos as? Map<*, *>)?.forEach { (titulo, imagenBase64) ->
                    if (imagenBase64 is String && imagenBase64.isNotEmpty()) {
                        val bitmap = DecodeBase64.decodeBase64ToBitmap(imagenBase64)
                        bitmap?.let {
                            val byteArray = bitmapToByteArray(it)
                            val imageData = ImageDataFactory.create(byteArray)
                            val image = Image(imageData)
                                .scaleToFit(400f, 400f)
                                .setHorizontalAlignment(HorizontalAlignment.CENTER)

                            document.add(Paragraph("Foto: $titulo").setFontSize(14f).setBold())
                            document.add(image)
                        }
                    }
                }
            }

            // Pie de página
            document.add(Paragraph("\n"))
            val footer = Paragraph("Documento generado automáticamente.")
                .setFontSize(10f)
                .setTextAlignment(TextAlignment.CENTER)
            document.add(footer)

            document.close()

            // Abrir el PDF generado
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "*/*")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "No hay una aplicación disponible para abrir el PDF",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    /**
     * Convierte un objeto [Bitmap] en un arreglo de bytes.
     *
     * @param bitmap Imagen en formato Bitmap.
     * @return Arreglo de bytes representando la imagen.
     */
    private fun bitmapToByteArray(bitmap: android.graphics.Bitmap): ByteArray {
        val stream = java.io.ByteArrayOutputStream()
        bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}
