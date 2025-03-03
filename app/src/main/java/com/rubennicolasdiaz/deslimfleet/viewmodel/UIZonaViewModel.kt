package com.rubennicolasdiaz.deslimfleet.viewmodel

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.CheckBoxInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.FotoInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.ObservacionesInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.GuardarCheckListUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la UI para la inspección de zonas en la aplicación.
 *
 * Maneja el estado de los checklists, observaciones, fotos y la lógica para habilitar o deshabilitar el botón de finalización.
 *
 * @param guardarChecklistUseCase Caso de uso para guardar el checklist en la base de datos.
 */
class UIZonaViewModel(private val guardarChecklistUseCase: GuardarCheckListUseCase) : ViewModel() {

    private var _totalCheckBoxesExpected = 0

    private val _expandedSections = MutableLiveData<Map<String, Boolean>>(emptyMap())
    private val expandedSections: LiveData<Map<String, Boolean>> = _expandedSections

    private val _fotosMap = MutableLiveData<Map<String, FotoInfo>>(emptyMap())
    val fotosMap: LiveData<Map<String, FotoInfo>> = _fotosMap

    private val _observacionesMap = MutableLiveData<Map<String, ObservacionesInfo>>(emptyMap())
    val observacionesMap: LiveData<Map<String, ObservacionesInfo>> = _observacionesMap

    private val _checkBoxStatesYes = MutableLiveData<Map<String, CheckBoxInfo>>(emptyMap())
    val checkBoxStatesYes: LiveData<Map<String, CheckBoxInfo>> = _checkBoxStatesYes

    private val _checkBoxStatesNo = MutableLiveData<Map<String, CheckBoxInfo>>(emptyMap())
    val checkBoxStatesNo: LiveData<Map<String, CheckBoxInfo>> = _checkBoxStatesNo

    private val _checkBoxStatesPlaga = MutableLiveData<Map<String, CheckBoxInfo>>(emptyMap())
    val checkBoxStatesPlaga: LiveData<Map<String, CheckBoxInfo>> = _checkBoxStatesPlaga

    private val _checkBoxStatesReparacion = MutableLiveData<Map<String, CheckBoxInfo>>(emptyMap())
    val checkBoxStatesReparacion: LiveData<Map<String, CheckBoxInfo>> = _checkBoxStatesReparacion

    private val _isButtonEnabled = MutableLiveData(false)
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    private val _showDialogSalir = MutableLiveData(false)
    val showDialogSalir: LiveData<Boolean> = _showDialogSalir

    private val _showDialogConfirmar = MutableLiveData(false)
    val showDialogConfirmar: LiveData<Boolean> = _showDialogConfirmar

    private val _imageBase64 = MutableLiveData<String?>(null)

    private val _showDialogSuccess = MutableLiveData(false)
    val showDialogSuccess: LiveData<Boolean> = _showDialogSuccess

    private val _showDialogFailure = MutableLiveData(false)
    val showDialogFailure: LiveData<Boolean> = _showDialogFailure

    /**
     * Establece la imagen en formato Base64.
     *
     * @param imageBase64 Imagen en formato Base64 o `null` si no hay imagen.
     */
    fun setImageBase64(imageBase64: String?) {
        _imageBase64.value = imageBase64
    }

    /**
     * Verifica si una sección específica está expandida.
     *
     * @param section Nombre de la sección.
     * @return `true` si la sección está expandida, `false` en caso contrario.
     */
    fun isSectionExpanded(section: String): Boolean {
        return expandedSections.value?.get(section) ?: false
    }

    /**
     * Restablece el contador de checkboxes esperados.
     */
    fun resetTotalCheckBoxes() {
        _totalCheckBoxesExpected = 0
    }

    /**
     * Activa o desactiva el botón de finalización basado en la cantidad de checkboxes seleccionados.
     */
    private fun updateFinishButtonState() {
        val checkBoxesYes = _checkBoxStatesYes.value ?: emptyMap()
        val checkBoxesNo = _checkBoxStatesNo.value ?: emptyMap()


        val allUniqueKeys = checkBoxesYes.keys union checkBoxesNo.keys


        val checkedElements = allUniqueKeys.count { key ->
            (checkBoxesYes[key]?.checked == true) || (checkBoxesNo[key]?.checked == true)
        }

        _isButtonEnabled.value = checkedElements == _totalCheckBoxesExpected
    }

    /**
     * Activa el diálogo de confirmación cuando el usuario hace clic en el botón de confirmación.
     *
     * Esta función cambia el estado de `_showDialogConfirmar` a `true`, lo que indica
     * que se debe mostrar el cuadro de diálogo de confirmación antes de finalizar la revisión.
     */
    fun onConfirmarButtonClicked() {
        _showDialogConfirmar.value = true
    }

    /**
     * Controla la visibilidad del cuadro de diálogo para salir de la revisión.
     *
     * @param show `true` para mostrar el diálogo, `false` para ocultarlo.
     */
    fun setShowDialogSalir(show: Boolean) {
        _showDialogSalir.value = show
    }

    /**
     * Controla la visibilidad del cuadro de diálogo de confirmación antes de finalizar la revisión.
     *
     * Ejecuta la actualización del estado en el hilo principal de forma segura utilizando `CoroutineScope(Dispatchers.Main)`.
     *
     * @param show `true` para mostrar el diálogo, `false` para ocultarlo.
     */
    fun setShowDialogConfirmar(show: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            _showDialogConfirmar.value = show
        }
    }

    /**
     * Restablece todos los estados de los checkboxes, observaciones, fotos y el estado del botón de finalización.
     *
     * Esta función se utiliza para limpiar los datos de la inspección actual, preparando el ViewModel
     * para una nueva inspección o restableciendo el estado después de finalizar una.
     */
    fun resetStates() {
        _checkBoxStatesYes.value = emptyMap()
        _checkBoxStatesNo.value = emptyMap()
        _checkBoxStatesPlaga.value = emptyMap()
        _checkBoxStatesReparacion.value = emptyMap()
        _observacionesMap.value = emptyMap()
        _fotosMap.value = emptyMap()
        _isButtonEnabled.value = false
    }

    /**
     * Alterna el estado de un checkbox de la categoría "Sí".
     *
     * @param uniqueKey Clave única del checkbox.
     * @param isChecked Estado del checkbox.
     */
    fun toggleCheckBoxYes(uniqueKey: String, isChecked: Boolean) {
        _checkBoxStatesYes.value = _checkBoxStatesYes.value?.toMutableMap()?.apply {
            this[uniqueKey] =
                CheckBoxInfo(uniqueKey, isChecked) { toggleCheckBoxYes(uniqueKey, it) }
        }

        _checkBoxStatesNo.value = _checkBoxStatesNo.value?.toMutableMap()?.apply {
            this[uniqueKey] =
                CheckBoxInfo(uniqueKey, !isChecked) { toggleCheckBoxNo(uniqueKey, it) }
        }

        updateFinishButtonState()
    }

    /**
     * Alterna el estado del checkbox de la categoría "No" e invierte el de la categoría "Sí".
     *
     * Si el checkbox "No" se marca, el correspondiente en la categoría "Sí" se desmarca y viceversa.
     * Luego, actualiza el estado del botón de finalización.
     *
     * @param uniqueKey Clave única que identifica el checkbox dentro de la inspección.
     * @param isChecked Estado del checkbox ("No"): `true` si está marcado, `false` si está desmarcado.
     */
    fun toggleCheckBoxNo(uniqueKey: String, isChecked: Boolean) {
        _checkBoxStatesNo.value = _checkBoxStatesNo.value?.toMutableMap()?.apply {
            this[uniqueKey] = CheckBoxInfo(uniqueKey, isChecked) { toggleCheckBoxNo(uniqueKey, it) }
        }

        _checkBoxStatesYes.value = _checkBoxStatesYes.value?.toMutableMap()?.apply {
            this[uniqueKey] =
                CheckBoxInfo(uniqueKey, !isChecked) { toggleCheckBoxYes(uniqueKey, it) }
        }

        updateFinishButtonState()
    }

    /**
     * Alterna el estado del checkbox de la categoría "Plaga".
     *
     * Permite marcar o desmarcar un checkbox que indica si un elemento presenta signos de plaga.
     *
     * @param uniqueKey Clave única que identifica el checkbox dentro de la inspección.
     * @param isChecked Estado del checkbox: `true` si está marcado, `false` si está desmarcado.
     */
    fun toggleCheckBoxPlaga(uniqueKey: String, isChecked: Boolean) {
        _checkBoxStatesPlaga.value = _checkBoxStatesPlaga.value?.toMutableMap()?.apply {
            this[uniqueKey] =
                CheckBoxInfo(uniqueKey, isChecked) { toggleCheckBoxPlaga(uniqueKey, it) }
        }
    }

    /**
     * Alterna el estado del checkbox de la categoría "Reparación".
     *
     * Permite marcar o desmarcar un checkbox que indica si un elemento necesita reparación.
     *
     * @param uniqueKey Clave única que identifica el checkbox dentro de la inspección.
     * @param isChecked Estado del checkbox: `true` si está marcado, `false` si está desmarcado.
     */
    fun toggleCheckBoxReparacion(uniqueKey: String, isChecked: Boolean) {
        _checkBoxStatesReparacion.value = _checkBoxStatesReparacion.value?.toMutableMap()?.apply {
            this[uniqueKey] =
                CheckBoxInfo(uniqueKey, isChecked) { toggleCheckBoxReparacion(uniqueKey, it) }
        }
    }

    /**
     * Obtiene el estado de todos los checkboxes de la categoría "Plaga".
     *
     * Devuelve un mapa donde cada clave representa un elemento inspeccionado y el valor
     * indica si el checkbox "Plaga" está marcado (`true`) o no (`false`).
     *
     * @return Mapa de claves únicas con su estado (`true` si está marcado, `false` si no).
     */
    fun getCheckBoxStatesPlaga(): Map<String, Boolean> {
        return _checkBoxStatesPlaga.value?.mapValues { it.value.checked } ?: emptyMap()
    }

    /**
     * Obtiene el estado de todos los checkboxes de la categoría "Reparación".
     *
     * Devuelve un mapa donde cada clave representa un elemento inspeccionado y el valor
     * indica si el checkbox "Reparación" está marcado (`true`) o no (`false`).
     *
     * @return Mapa de claves únicas con su estado (`true` si está marcado, `false` si no).
     */
    fun getCheckBoxStatesReparacion(): Map<String, Boolean> {
        return _checkBoxStatesReparacion.value?.mapValues { it.value.checked } ?: emptyMap()
    }

    /**
     * Inicializa los estados de los checkboxes para una sección específica.
     *
     * Crea entradas en los mapas de checkboxes "Sí" y "No" con valores predeterminados `false`,
     * y asigna funciones de alternancia para gestionar los cambios de estado.
     *
     * @param items Lista de elementos dentro de la sección.
     * @param section Nombre de la sección a la que pertenecen los checkboxes.
     */
    fun setInitialCheckBoxStates(items: List<String>, section: String) {
        val uniqueKeys = items.map { "$section - $it" }

        // Aumenta el total de checkboxes esperados
        _totalCheckBoxesExpected += uniqueKeys.size

        // Obtiene los estados actuales o crea nuevos mapas mutables si están vacíos
        val newYesStates = _checkBoxStatesYes.value?.toMutableMap() ?: mutableMapOf()
        val newNoStates = _checkBoxStatesNo.value?.toMutableMap() ?: mutableMapOf()

        uniqueKeys.forEach { uniqueKey ->
            newYesStates[uniqueKey] = CheckBoxInfo(uniqueKey, false) { isChecked ->
                toggleCheckBoxYes(uniqueKey, isChecked)
            }
            newNoStates[uniqueKey] = CheckBoxInfo(uniqueKey, false) { isChecked ->
                toggleCheckBoxNo(uniqueKey, isChecked)
            }
        }

        // Actualiza los estados de los checkboxes en los LiveData
        _checkBoxStatesYes.value = newYesStates
        _checkBoxStatesNo.value = newNoStates

        // Verifica si el botón de finalización debe activarse
        updateFinishButtonState()
    }

    /**
     * Establece o actualiza el texto de una observación asociada a un elemento de la inspección.
     *
     * @param uniqueKey Clave única que identifica el elemento dentro de la inspección.
     * @param text Texto de la observación asociada al elemento.
     */
    fun setObservaciones(uniqueKey: String, text: String) {
        _observacionesMap.value = _observacionesMap.value?.toMutableMap()?.apply {
            this[uniqueKey] = ObservacionesInfo(uniqueKey, text) { setObservaciones(uniqueKey, it) }
        }
    }

    /**
     * Guarda el checklist en la base de datos.
     */
    fun guardarChecklist(
        currentUser: String,
        barcoNombre: String,
        zonaNombre: String,
        checkBoxStatesYes: Map<String, CheckBoxInfo>,
        checkBoxStatesNo: Map<String, CheckBoxInfo>,
        checkBoxStatesPlaga: Map<String, CheckBoxInfo>,
        checkBoxStatesReparacion: Map<String, CheckBoxInfo>,
        observacionesMap: Map<String, ObservacionesInfo>,
        fotosMap: Map<String, FotoInfo>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            guardarChecklistUseCase.invoke(
                currentUser,
                barcoNombre,
                zonaNombre,
                checkBoxStatesYes,
                checkBoxStatesNo,
                checkBoxStatesPlaga,
                checkBoxStatesReparacion,
                observacionesMap,
                fotosMap,
                {
                    viewModelScope.launch(Dispatchers.Main) {
                        setShowDialogConfirmar(false)
                        setShowDialogSuccess(true)
                        onSuccess()
                    }
                },
                { e ->
                    viewModelScope.launch(Dispatchers.Main) {
                        setShowDialogConfirmar(false)
                        setShowDialogFailure(true)
                        Log.e("Guardar CheckList", "Error al guardar:", e)
                        onFailure(e)
                    }
                }
            )
        }
    }

    /**
     * Inicializa el mapa de observaciones para una sección específica, asignando valores vacíos a cada elemento.
     *
     * Esta función solo inicializa las observaciones si el mapa de `_observacionesMap` está vacío.
     *
     * @param items Lista de elementos dentro de la sección.
     * @param section Nombre de la sección a la que pertenecen las observaciones.
     */
    fun setInitialObservaciones(items: List<String>, section: String) {
        val uniqueKeys = items.map { "$section - $it" }

        if (_observacionesMap.value.isNullOrEmpty()) {
            _observacionesMap.value = uniqueKeys.associateWith { uniqueKey ->
                ObservacionesInfo(uniqueKey, "") { newText -> setObservaciones(uniqueKey, newText) }
            }
        }
    }

    /**
     * Asigna o actualiza la imagen en formato Base64 asociada a un elemento inspeccionado.
     *
     * @param uniqueKey Clave única que identifica el elemento dentro de la inspección.
     * @param imageBase64 Imagen codificada en Base64, o `null` si no hay imagen.
     */
    fun setFoto(uniqueKey: String, imageBase64: String?) {
        _fotosMap.value = _fotosMap.value?.toMutableMap()?.apply {
            this[uniqueKey] = FotoInfo(uniqueKey, imageBase64) { setFoto(uniqueKey, it) }
        }
    }

    /**
     * Controla la visibilidad del cuadro de diálogo de éxito después de guardar la inspección.
     *
     * @param show `true` para mostrar el diálogo, `false` para ocultarlo.
     */
    fun setShowDialogSuccess(show: Boolean) {
        _showDialogSuccess.value = show
    }

    /**
     * Controla la visibilidad del cuadro de diálogo de error después de un fallo al guardar la inspección.
     *
     * @param show `true` para mostrar el diálogo, `false` para ocultarlo.
     */
    fun setShowDialogFailure(show: Boolean) {
        _showDialogFailure.value = show
    }

    /**
     * Inicializa el mapa de fotos para una sección específica, asignando valores `null` a cada elemento.
     *
     * Esta función solo inicializa las fotos si el mapa `_fotosMap` está vacío.
     *
     * @param items Lista de elementos dentro de la sección.
     * @param section Nombre de la sección a la que pertenecen las fotos.
     */
    fun setInitialFotos(items: List<String>, section: String) {
        val uniqueKeys = items.map { "$section - $it" }

        if (_fotosMap.value.isNullOrEmpty()) {
            _fotosMap.value = uniqueKeys.associateWith { uniqueKey ->
                FotoInfo(uniqueKey, null) { newImage -> setFoto(uniqueKey, newImage) }
            }
        }
    }

    /**
     * Rota una imagen en formato `Bitmap` en 90 grados en el sentido de las agujas del reloj.
     *
     * @param bitmap Imagen en formato `Bitmap` que se desea rotar.
     * @return Un nuevo `Bitmap` con la imagen rotada 90 grados.
     */
    fun rotateBitmapIfNeeded(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(90f)

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}