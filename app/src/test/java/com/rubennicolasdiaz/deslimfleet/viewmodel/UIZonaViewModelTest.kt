package com.rubennicolasdiaz.deslimfleet.viewmodel

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.CheckBoxInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.FotoInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.ObservacionesInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.usecases.GuardarCheckListUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class UIZonaViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: UIZonaViewModel
    private val guardarCheckListUseCase: GuardarCheckListUseCase = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UIZonaViewModel(guardarCheckListUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `toggleCheckBoxYes actualiza estado y habilita boton si es necesario`() {
        val key = "check_1"
        viewModel.toggleCheckBoxYes(key, true)

        assertEquals(true, viewModel.checkBoxStatesYes.value?.get(key)?.checked)
        assertEquals(false, viewModel.checkBoxStatesNo.value?.get(key)?.checked)
    }

    @Test
    fun `toggleCheckBoxNo actualiza estado y habilita boton si es necesario`() {
        val key = "check_2"
        viewModel.toggleCheckBoxNo(key, true)

        assertEquals(true, viewModel.checkBoxStatesNo.value?.get(key)?.checked)
        assertEquals(false, viewModel.checkBoxStatesYes.value?.get(key)?.checked)
    }

    @Test
    fun `setObservaciones actualiza observaciones correctamente`() {
        val key = "obs_1"
        val text = "Nueva observación"

        viewModel.setObservaciones(key, text)
        assertEquals(text, viewModel.observacionesMap.value?.get(key)?.text)
    }

    @Test
    fun `setShowDialogSuccess actualiza showDialogSuccess`() {
        viewModel.setShowDialogSuccess(true)
        assertEquals(true, viewModel.showDialogSuccess.value)

        viewModel.setShowDialogSuccess(false)
        assertEquals(false, viewModel.showDialogSuccess.value)
    }

    @Test
    fun `setShowDialogFailure actualiza showDialogFailure`() {
        viewModel.setShowDialogFailure(true)
        assertEquals(true, viewModel.showDialogFailure.value)

        viewModel.setShowDialogFailure(false)
        assertEquals(false, viewModel.showDialogFailure.value)
    }

    @Test
    fun `setShowDialogConfirmar actualiza showDialogConfirmar`() = runTest {
        viewModel.setShowDialogConfirmar(true)
        advanceUntilIdle()
        assertEquals(true, viewModel.showDialogConfirmar.value)
    }

    @Test
    fun `setShowDialogSalir actualiza showDialogSalir`() {
        viewModel.setShowDialogSalir(true)
        assertEquals(true, viewModel.showDialogSalir.value)

        viewModel.setShowDialogSalir(false)
        assertEquals(false, viewModel.showDialogSalir.value)
    }

    @Test
    fun `guardarChecklist ejecuta correctamente el use case`() = runTest {
        val currentUser = "usuario"
        val barco = "Barco A"
        val zona = "Zona A"
        val checkBoxYes = mapOf("check1" to CheckBoxInfo("check1", true) {})
        val checkBoxNo = mapOf<String, CheckBoxInfo>()
        val checkBoxPlaga = mapOf<String, CheckBoxInfo>()
        val checkBoxReparacion = mapOf<String, CheckBoxInfo>()
        val observaciones = mapOf("obs1" to ObservacionesInfo("obs1", "Texto") {})
        val fotos = mapOf("foto1" to FotoInfo("foto1", "base64") {})

        val onSuccess = mockk<() -> Unit>(relaxed = true)
        val onFailure = mockk<(Exception) -> Unit>(relaxed = true)

        viewModel.guardarChecklist(
            currentUser, barco, zona, checkBoxYes, checkBoxNo,
            checkBoxPlaga, checkBoxReparacion, observaciones, fotos,
            onSuccess, onFailure
        )

        advanceUntilIdle()

        coVerify {
            guardarCheckListUseCase.invoke(
                currentUser, barco, zona, checkBoxYes, checkBoxNo,
                checkBoxPlaga, checkBoxReparacion, observaciones, fotos, any(), any()
            )
        }
    }

    @Test
    fun `rotateBitmapIfNeeded rota la imagen correctamente`() {
        val bitmap: Bitmap = mockk(relaxed = true)
        val rotatedBitmap: Bitmap = mockk(relaxed = true)
        val matrix: Matrix = mockk(relaxed = true)

        // Mockear la llamada a postRotate() para evitar el error
        every { matrix.postRotate(any()) } returns true

        mockkStatic(Bitmap::class)
        every { Bitmap.createBitmap(eq(bitmap), any(), any(), any(), any(), eq(matrix), any()) } returns rotatedBitmap

        val result = viewModel.rotateBitmapIfNeeded(bitmap)

        assertEquals(rotatedBitmap, result)
    }
}
