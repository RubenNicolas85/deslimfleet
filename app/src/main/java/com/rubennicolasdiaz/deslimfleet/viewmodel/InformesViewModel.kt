package com.rubennicolasdiaz.deslimfleet.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.EnviarPromptGeminiUseCase
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.GenerarPdfUseCase
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.ObtenerTodasInspeccionesUseCase
import com.rubennicolasdiaz.deslimfleet.model.enums.Barco
import com.rubennicolasdiaz.deslimfleet.model.enums.Zona
import com.rubennicolasdiaz.deslimfleet.utilities.ObtenerFechaHoy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel para la generación de informes de inspección.
 *
 * Gestiona la selección de barco, zona y fecha, así como la generación de informes
 * basados en inspecciones registradas en Firebase.
 *
 * @param obtenerTodasInspeccionesUseCase Caso de uso para obtener todas las inspecciones almacenadas.
 * @param enviarPromptGeminiUseCase Caso de uso para analizar la inspección mediante inteligencia artificial.
 * @param generarPdfUseCase Caso de uso para generar el informe en formato PDF.
 */
class InformesViewModel(
    private val obtenerTodasInspeccionesUseCase: ObtenerTodasInspeccionesUseCase,
    private val enviarPromptGeminiUseCase: EnviarPromptGeminiUseCase,
    private val generarPdfUseCase: GenerarPdfUseCase
) : ViewModel() {

    private val _selectedBarco = MutableStateFlow(Barco.CIUDAD_BARCELONA)
    val selectedBarco: StateFlow<Barco> = _selectedBarco

    private val _selectedZona = MutableStateFlow(Zona.ACOMODACION)
    val selectedZona: StateFlow<Zona> = _selectedZona

    private val _selectedDate = MutableStateFlow(ObtenerFechaHoy.getTodayDate())
    val selectedDate: StateFlow<String> = _selectedDate

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _barcoDropdownExpanded = MutableStateFlow(false)
    val barcoDropdownExpanded: StateFlow<Boolean> = _barcoDropdownExpanded

    private val _zonaDropdownExpanded = MutableStateFlow(false)
    val zonaDropdownExpanded: StateFlow<Boolean> = _zonaDropdownExpanded

    /**
     * Establece el barco seleccionado.
     *
     * @param barco Barco seleccionado.
     */
    fun setSelectedBarco(barco: Barco) {
        _selectedBarco.value = barco
    }

    /**
     * Establece la zona seleccionada.
     *
     * @param zona Zona seleccionada.
     */
    fun setSelectedZona(zona: Zona) {
        _selectedZona.value = zona
    }

    /**
     * Establece la fecha seleccionada.
     *
     * @param date Fecha seleccionada en formato `dd/MM/yyyy`.
     */
    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }

    /**
     * Limpia el mensaje de error.
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Expande o colapsa el menú desplegable de barcos.
     *
     * @param expanded Estado del menú desplegable.
     */
    fun setBarcoDropdownExpanded(expanded: Boolean) {
        _barcoDropdownExpanded.value = expanded
    }

    /**
     * Expande o colapsa el menú desplegable de zonas.
     *
     * @param expanded Estado del menú desplegable.
     */
    fun setZonaDropdownExpanded(expanded: Boolean) {
        _zonaDropdownExpanded.value = expanded
    }

    /**
     * Genera un informe basado en la inspección seleccionada.
     *
     * @param context Contexto de la aplicación.
     */
    fun generarInforme(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _isLoading.value = true
            }

            val barco = _selectedBarco.value.nombre
            val zona = _selectedZona.value.nombre
            val fecha = _selectedDate.value

            val inspecciones = obtenerTodasInspeccionesUseCase.invoke()
            val inspeccionFiltrada = inspecciones.firstOrNull {
                it["Barco"] == barco && it["Zona"] == zona && it["Fecha"] == fecha
            }

            if (inspeccionFiltrada == null) {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
                _errorMessage.postValue("No se encontró inspección para el barco, la zona y la fecha seleccionados")
            } else {
                val prompt =
                    "Eres un gran experto en firebase. Te paso un documento de la colección Inspecciones que contiene información sobre " +
                            "la inspección de una zona de un barco en una fecha concreta. Analízala y destaca la información más importante. Te explico cómo funciona: " +
                            "El CheckboxYes indica que el elemento en cuestión está correcto. El CheckboxNo indica que el elemento en cuestión no está bien." +
                            "El CheckboxPlaga indica que el elemento sufre de una plaga y habría que ver las observaciones de ese elemento." +
                            "El CheckboxReparacion indica que el elemento está dañado y habría que repararlo, y habría que ver las observaciones de ese elemento." +
                            "Observaciones: ahí se indica información relevante de cada elemento." +
                            "Ignora el apartado fotos." +
                            "Devuélveme un informe valorando la información que has recibido sin poner etiquetas, sólo elabora el informe. " + inspeccionFiltrada.toString()
                async { enviarPromptGeminiUseCase.invoke(prompt) }.await()
                val respuestaGemini = enviarPromptGeminiUseCase.uiState.value.toString()
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
                async {
                    generarPdfUseCase.invoke(
                        context,
                        inspeccionFiltrada,
                        barco,
                        zona,
                        fecha,
                        respuestaGemini
                    )
                }.await()
            }
        }
    }
}
