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
 * Pantalla de revisión para la zona del Bar en la inspección de barcos.
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
fun BarScreen(
    navHostController: NavHostController,
    uIZonaViewModel: UIZonaViewModel,
    barcoNombre: String
) {
    val checklistSections = mapOf(
        "SUELOS" to listOf(
            "Limpios y desinfectados", "Sin restos de grasa",
            "Zonas de dificil acceso, bajo muebles, bajo maquinaria, bajo neveras, tras arcones"
        ),
        "TECHOS" to listOf(
            "Limpieza lamas techo (gradas/óxido)",
            "Rejillas ventilación(limpieza))",
            "Luminarias(carcasas limpias y en buen estado)"
        ),
        "NEVERAS/\nCONGELADORES" to listOf(
            "Limpieza exterior e interior",
            "Temperatura correcta",
            "Correcto almacenaje bandejas/Etiquetas/Papel Film",
            "Estado de bisagras y tiradores",
            "Limpieza bajo y tras neveras"
        ),
        "ESTANTERÍAS Y\n MUEBLES INOX" to listOf(
            "Limpieza de las estanterías/Puertas/Baldas/Parte Superior",
            "Orden Interior",
            "Productos perfectamente cerrados y en fecha(No caducados)"
        ),
        "MÁQUINA CAFÉ" to listOf(
            "Limpieza bajo y tras",
            "Máquina de café en buen estado"
        ),
        "HORNO" to listOf(
            "Limpieza exterior, superficies, puertas",
            "Limpieza interior, superficies, rejillas, ausencia partículos sólidas y de grasa",
            "Limpieza Bajo Horno",
            "Limpieza de superficies y mesas auxiliares (Cajas cartón y restos de pan)"
        ),
        "CUBOS DE BASURA" to listOf(
            "Limpieza del cubo",
            "Funcionamiento del pedal y estado general del cubo",
            "Segregación de residuos"
        )
    )

    val currentUser = Firebase.auth.currentUser?.email ?: ""

    val context = LocalContext.current
    val activity = context as? Activity

    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    DisposableEffect(Unit) {
        onDispose {
            uIZonaViewModel.resetTotalCheckBoxes()
            uIZonaViewModel.resetStates()
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
        TitleSection(Zona.BAR.nombre)
        MultipleChecklist(uIZonaViewModel, checklistSections)
        FinishButton(uIZonaViewModel)
        DialogConfirm(
            currentUser,
            uIZonaViewModel,
            navHostController,
            barcoNombre,
            Zona.BAR.nombre
        )
        DialogSalir(uIZonaViewModel, navHostController)
    }
}