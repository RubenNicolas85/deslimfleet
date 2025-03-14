package com.rubennicolasdiaz.deslimfleet.view.screens.zones

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.rubennicolasdiaz.deslimfleet.model.enums.Zona
import com.rubennicolasdiaz.deslimfleet.view.components.DialogConfirm
import com.rubennicolasdiaz.deslimfleet.view.components.DialogSalir
import com.rubennicolasdiaz.deslimfleet.view.components.FinishButton
import com.rubennicolasdiaz.deslimfleet.view.components.HandleBackPress
import com.rubennicolasdiaz.deslimfleet.view.components.MultipleChecklist
import com.rubennicolasdiaz.deslimfleet.view.components.TitleSection
import com.rubennicolasdiaz.deslimfleet.view.components.scrollStateHandler
import com.rubennicolasdiaz.deslimfleet.view.theme.White
import com.rubennicolasdiaz.deslimfleet.viewmodel.UIZonaViewModel

/**
 * Pantalla de revisión para la zona de Autoservicio en la inspección de barcos.
 *
 * Presenta una lista de verificación organizada en secciones donde los usuarios pueden marcar
 * elementos como revisados, agregar observaciones y capturar fotos.
 *
 * @param navHostController Controlador de navegación para gestionar la transición entre pantallas.
 * @param uIZonaViewModel ViewModel que maneja la lógica de la UI y los estados de los checklists.
 * @param barcoNombre Nombre del barco en el que se está realizando la inspección.
 */
@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun AutoservicioScreen(
    navHostController: NavHostController,
    uIZonaViewModel: UIZonaViewModel,
    barcoNombre: String
) {
    val checklistSections = mapOf(
        "SUELOS" to listOf("Zona Expositores Comida"),
        "EXPOSITORES COMIDA" to listOf(
            "Limpieza tras retirar bandejas comida",
            "Limpieza Zonas Bajas"
        ),
        "CONTENEDORES" to listOf("Limpieza interior", "Retirada Basura", "Tapa Cerrada")
    )

    val currentUser = Firebase.auth.currentUser?.email ?: ""

    val context = LocalContext.current
    val activity = context as? Activity

    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    DisposableEffect(Unit) {
        onDispose {
            uIZonaViewModel.resetTotalCheckBoxes()
            uIZonaViewModel.resetStates() // Resetea los estados de los checkboxes y demás elementos
        }
    }

    HandleBackPress(uIZonaViewModel)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(16.dp)
            .verticalScroll(scrollStateHandler()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleSection(Zona.AUTOSERVICIO.nombre)
        MultipleChecklist(uIZonaViewModel, checklistSections)
        FinishButton(uIZonaViewModel)
        DialogConfirm(
            currentUser,
            uIZonaViewModel,
            navHostController,
            barcoNombre,
            Zona.AUTOSERVICIO.nombre
        )
        DialogSalir(uIZonaViewModel, navHostController)
    }
}