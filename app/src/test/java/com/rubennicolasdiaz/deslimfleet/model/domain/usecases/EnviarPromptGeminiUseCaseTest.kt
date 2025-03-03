package com.rubennicolasdiaz.deslimfleet.model.domain.usecases

import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.UIStateGemini
import com.rubennicolasdiaz.deslimfleet.model.domain.repositories.interfaces.GeminiRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class EnviarPromptGeminiUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: EnviarPromptGeminiUseCase
    private val repository: GeminiRepository = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Simulamos el StateFlow de UIStateGemini
        val uiStateFlow: MutableStateFlow<UIStateGemini> = MutableStateFlow(UIStateGemini.Loading)
        every { repository.uiState } returns uiStateFlow

        useCase = EnviarPromptGeminiUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState refleja el estado del repositorio`() = runTest {
        val expectedState = UIStateGemini.Success("Respuesta generada")
        val stateFlow: MutableStateFlow<UIStateGemini> = MutableStateFlow(expectedState)

        every { repository.uiState } returns stateFlow

        // 🔹 Reasignamos `useCase` después de mockear `repository.uiState`
        val useCase = EnviarPromptGeminiUseCase(repository)

        assertEquals(expectedState, useCase.uiState.value)
    }

    @Test
    fun `invoke llama a sendPrompt en el repositorio`() = runTest {
        val prompt = "Genera un informe sobre el barco"

        coEvery { repository.sendPrompt(prompt) } just Runs

        useCase.invoke(prompt)

        coVerify(exactly = 1) { repository.sendPrompt(prompt) }
    }
}
