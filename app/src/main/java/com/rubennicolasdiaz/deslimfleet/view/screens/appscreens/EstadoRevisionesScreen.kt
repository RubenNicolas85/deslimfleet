package com.rubennicolasdiaz.deslimfleet.view.screens.appscreens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.rubennicolasdiaz.deslimfleet.utilities.ObtenerFechaHoy
import com.rubennicolasdiaz.deslimfleet.model.enums.EstadoRevision
import com.rubennicolasdiaz.deslimfleet.view.theme.Azul
import com.rubennicolasdiaz.deslimfleet.view.theme.ColorCargando
import com.rubennicolasdiaz.deslimfleet.view.theme.EstadoBarcos
import com.rubennicolasdiaz.deslimfleet.view.theme.EstadoRevisiones
import com.rubennicolasdiaz.deslimfleet.view.theme.Revisado
import com.rubennicolasdiaz.deslimfleet.view.theme.SinRevisar
import com.rubennicolasdiaz.deslimfleet.viewmodel.EstadoRevisionesViewModel

/**
 * Pantalla que muestra el estado de las revisiones de los barcos.
 *
 * @param estadoRevisionesViewModel ViewModel que maneja la lógica de las revisiones.
 */
@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun EstadoRevisionesScreen(estadoRevisionesViewModel: EstadoRevisionesViewModel) {

    BackHandler(enabled = true) {
        // No hace nada, bloquea la navegación hacia atrás
    }

    val context = LocalContext.current
    val activity = context as? Activity
    val fecha = ObtenerFechaHoy.getTodayDate()

    val estadoRevisiones by estadoRevisionesViewModel.estadoRevisiones.collectAsState()

    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    LaunchedEffect(Unit) {
        estadoRevisionesViewModel.cargarEstados()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EstadoRevisiones)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Estado Revisiones - $fecha",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Azul,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        estadoRevisionesViewModel.barcosAgrupados.forEach { (barco, zonas) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = EstadoBarcos
                )
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = barco.nombre,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Azul,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(color = Azul, thickness = 1.dp)

                    zonas.forEach { (_, zona) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${zona.nombre}:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Azul,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = estadoRevisiones[barco to zona]?.nombre ?: "Cargando...",
                                color = when (estadoRevisiones[barco to zona]) {
                                    EstadoRevision.REVISADO -> Revisado
                                    EstadoRevision.SIN_REVISAR -> SinRevisar
                                    else -> ColorCargando
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp),
            onClick = { estadoRevisionesViewModel.navigateSeleccionBarco() }
        ) {
            Text(text = "Iniciar Revisión", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
    }
}





