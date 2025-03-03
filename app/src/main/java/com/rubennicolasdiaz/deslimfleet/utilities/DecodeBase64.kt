package com.rubennicolasdiaz.deslimfleet.utilities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

/**
 * Utilidad para decodificar imágenes en formato Base64 a objetos Bitmap.
 */
object DecodeBase64 {

    /**
     * Decodifica una cadena Base64 en un objeto [Bitmap].
     *
     * @param base64String Cadena codificada en Base64 representando una imagen.
     * @return [Bitmap] resultante o `null` si la decodificación falla.
     */
    fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val options = BitmapFactory.Options().apply {
                inPreferredConfig = Bitmap.Config.ARGB_8888 // Mantiene la mejor calidad
            }
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size, options)
        } catch (e: Exception) {
            e.printStackTrace()
            null // Retorna null si hay un error en la conversión
        }
    }
}
