package com.rubennicolasdiaz.deslimfleet.model.domain.usecases

import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.CheckBoxInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.FotoInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.ObservacionesInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.FirebaseRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*

@ExperimentalCoroutinesApi
class GuardarCheckListUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: GuardarCheckListUseCase
    private val repository: FirebaseRepository = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = GuardarCheckListUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke llama a guardarChecklist en el repositorio`() = runTest {
        val currentUser = "usuario123"
        val barcoNombre = "Barco A"
        val zonaNombre = "Zona B"
        val checkBoxYes = mapOf("check1" to CheckBoxInfo("check1", true) {})
        val checkBoxNo = mapOf<String, CheckBoxInfo>()
        val checkBoxPlaga = mapOf<String, CheckBoxInfo>()
        val checkBoxReparacion = mapOf<String, CheckBoxInfo>()
        val observaciones = mapOf("obs1" to ObservacionesInfo("obs1", "Comentario") {})
        val fotos = mapOf("foto1" to FotoInfo("foto1", "imagenBase64") {})

        val onSuccess = mockk<() -> Unit>(relaxed = true)
        val onFailure = mockk<(Exception) -> Unit>(relaxed = true)

        coEvery {
            repository.guardarChecklist(
                currentUser, barcoNombre, zonaNombre, checkBoxYes, checkBoxNo,
                checkBoxPlaga, checkBoxReparacion, observaciones, fotos, onSuccess, onFailure
            )
        } just Runs

        useCase.invoke(currentUser, barcoNombre, zonaNombre, checkBoxYes, checkBoxNo,
            checkBoxPlaga, checkBoxReparacion, observaciones, fotos, onSuccess, onFailure)

        coVerify(exactly = 1) {
            repository.guardarChecklist(
                currentUser, barcoNombre, zonaNombre, checkBoxYes, checkBoxNo,
                checkBoxPlaga, checkBoxReparacion, observaciones, fotos, onSuccess, onFailure
            )
        }
    }

    @Test
    fun `invoke llama onSuccess cuando la operacion es exitosa`() = runTest {
        val currentUser = "usuario123"
        val barcoNombre = "Barco A"
        val zonaNombre = "Zona B"
        val checkBoxYes = mapOf("check1" to CheckBoxInfo("check1", true) {})
        val checkBoxNo = mapOf<String, CheckBoxInfo>()
        val checkBoxPlaga = mapOf<String, CheckBoxInfo>()
        val checkBoxReparacion = mapOf<String, CheckBoxInfo>()
        val observaciones = mapOf("obs1" to ObservacionesInfo("obs1", "Comentario") {})
        val fotos = mapOf("foto1" to FotoInfo("foto1", "imagenBase64") {})

        val onSuccess = mockk<() -> Unit>(relaxed = true)
        val onFailure = mockk<(Exception) -> Unit>(relaxed = true)

        coEvery {
            repository.guardarChecklist(any(), any(), any(), any(), any(), any(), any(), any(), any(), captureLambda(), any())
        } answers {
            lambda<() -> Unit>().invoke() // Simula el éxito llamando a onSuccess()
        }

        useCase.invoke(currentUser, barcoNombre, zonaNombre, checkBoxYes, checkBoxNo,
            checkBoxPlaga, checkBoxReparacion, observaciones, fotos, onSuccess, onFailure)

        verify { onSuccess.invoke() }
    }

    @Test
    fun `invoke llama onFailure cuando la operacion falla`() = runTest {
        val currentUser = "usuario123"
        val barcoNombre = "Barco A"
        val zonaNombre = "Zona B"
        val checkBoxYes = mapOf("check1" to CheckBoxInfo("check1", true) {})
        val checkBoxNo = mapOf<String, CheckBoxInfo>()
        val checkBoxPlaga = mapOf<String, CheckBoxInfo>()
        val checkBoxReparacion = mapOf<String, CheckBoxInfo>()
        val observaciones = mapOf("obs1" to ObservacionesInfo("obs1", "Comentario") {})
        val fotos = mapOf("foto1" to FotoInfo("foto1", "imagenBase64") {})

        val onSuccess = mockk<() -> Unit>(relaxed = true)
        val onFailure = mockk<(Exception) -> Unit>(relaxed = true)
        val exception = Exception("Error al guardar checklist")

        coEvery {
            repository.guardarChecklist(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), captureLambda())
        } answers {
            lambda<(Exception) -> Unit>().invoke(exception) // Simula fallo llamando a onFailure()
        }

        useCase.invoke(currentUser, barcoNombre, zonaNombre, checkBoxYes, checkBoxNo,
            checkBoxPlaga, checkBoxReparacion, observaciones, fotos, onSuccess, onFailure)

        verify { onFailure.invoke(exception) }
    }
}
