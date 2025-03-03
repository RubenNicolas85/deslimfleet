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
 * Pantalla de revisión para la zona de Cocina en la inspección de barcos.
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
fun CocinaScreen(
    navHostController: NavHostController,
    uIZonaViewModel: UIZonaViewModel,
    barcoNombre: String
) {
    val checklistSections = mapOf(
        "SUELOS" to listOf(
            "Limpios y desinfectados",
            "Sin restos de grasa",
            "Zonas de difícil acceso limpios/Bajo Muebles/Bajo Maquinaria/Bajo neveras/Tras Arcones"
        ),
        "TECHOS" to listOf(
            "Limpieza Lamas Techo",
            "Rejillas ventilación(Limpieza)",
            "Luminarias(Carcasas Limpias y en buen estado)"
        ),
        "CUARTO ASEO / \nSISTEMA THOMIL" to listOf(
            "Limpieza y orden",
            "Suelos/Techos y mamparos limpios",
            "Sistema Thomil operativo y productos de limpieza ordenados y sin contacto con el suelo"
        ),
        "NEVERAS/ARCONES/CONGELADORES" to listOf(
            "Limpieza exterior e interior",
            "Temperatura correcta",
            "Correcto almacenaje de bandejas/etiquetas/papel film",
            "Estado de bisagras y tiradores",
            "Limpieza bajo y tras neveras"
        ),
        "MESA CALIENTE 1" to listOf(
            "Limpieza exterior arcón",
            "Limpieza bajo y tras arcón",
            "Limpieza y orden interior(Ausencia de hielo)",
            "Estado de puertas/Bisagras/Tiradores"
        ),
        "MESA CALIENTE 2" to listOf(
            "Limpieza de encimera",
            "Limpieza interior de los calentadores",
            "Funcionamiento/Temperatura",
            "Limpieza Bajo Calentadores"
        ),
        "ESTANTERÍAS Y \nMUEBLES INOX" to listOf(
            "Limpieza de las estanterías/Puertas/Baldas/Parte Superior",
            "Orden Interior",
            "Productos perfectamente cerrados y en fecha(No caducados)"
        ),
        "MÁQUINA \nCORTAFIAMBRE" to listOf(
            "Limpieza/Desmontaje de piezas(Disco de corte, etc)",
            "Funcionamiento",
            "Guante de corte(Estado/Limpieza)",
            "Encimera auxiliar/Superficie de trabajo Limpia"
        ),
        "FREGADERO DE \nLIMPIEZA VERDURAS" to listOf(
            "Limpieza del fregadero(Sin restos de comida",
            "Limpieza de la superficie de trabajo verduras",
            "Limpieza de los estantes inferiores y superiores/sin restos",
            "Sistema Thomil de lavado de verduras funcional/Tiras comprobación",
            "Rejillas desagüe colocadas y limpias",
            "Grifos limpios, en buen estado y con filtro limpio",
            "Caudal de agua y temperaturas tanto fría como caliente correcta"
        ),
        "FREGADEROS \nEN GENERAL" to listOf(
            "Limpieza del fregadero",
            "Sin restos de comida",
            "Rejillas desagüe colocadas y limpias",
            "Grifos limpios, en buen estado y con filtro limpio",
            "Caudal de agua y temperaturas tanto fría como caliente correcta"
        ),
        "LAVAMANOS" to listOf(
            "Limpieza",
            "Portarrollos de papel",
            "Jabonera",
            "Caudal de agua y temperaturas tanto fría como caliente correcta"
        ),
        "MÁQUINA LAVAVAJILLAS" to listOf(
            "Limpieza exterior",
            "Limpieza interior/sin restos de comida/sin agua sucia",
            "Limpieza tras y bajo máquina",
            "Funcionamiento/capota/temperaturas/brazos de lavado"
        ),
        "HORNO" to listOf(
            "Limpieza exterior/superficies/puertas",
            "Limpieza interior/superficies/rejillas/ausencia de partículas sólidas y de grasa",
            "Limpieza bajo horno",
            "Limpieza de superficies y mesas auxiliares(Cajas de cartón y restos de pan"
        ),
        "FOGONES" to listOf(
            "Limpieza fogones",
            "Limpieza de mesas auxiliares",
            "Funcionamiento de los fogones",
            "Limpieza bajo módulo de fogones"
        ),
        "PLANCHA" to listOf(
            "Limpieza de la plancha",
            "Limpieza bajo plancha",
            "Funcionamiento de la plancha"
        ),
        "SARTÉN BASCULANTE" to listOf(
            "Limpieza superficie exterior",
            "Limpieza cuba interior",
            "Funcionamiento sartén basculante",
            "Limpieza bajo sartén basculante"
        ),
        "MARMITA /\n OLLA A PRESIÓN" to listOf(
            "Limpieza exterior de la olla",
            "Limpieza interior de la olla",
            "Funcionamiento de la olla",
            "Limpieza bajo olla"
        ),
        "CAMPANA DE \nEXTRACCIÓN" to listOf(
            "Limpieza de la campana de extracción(Rejillas y superficies)",
            "Funcionamiento de la campana"
        ),
        "CANALETAS/\nIMBORNALES SUELO \nEN TORNO A COCINA" to listOf(
            "Limpieza de las canaletas de recogida y desagües" +
                    "(Canaleta/Rejilla de cubrición/desagües)"
        ),
        "CUBOS DE BASURA" to listOf(
            "Limpieza del cubo",
            "Funcionamiento del pedal y estado general del cubo",
            "Segregación de residuos",
            "Ausencia de restos de comida fuera de horario de cocina"
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
        TitleSection(Zona.COCINA.nombre)
        MultipleChecklist(uIZonaViewModel, checklistSections)
        FinishButton(uIZonaViewModel)
        DialogConfirm(
            currentUser,
            uIZonaViewModel,
            navHostController,
            barcoNombre,
            Zona.COCINA.nombre
        )
        DialogSalir(uIZonaViewModel, navHostController)
    }
}