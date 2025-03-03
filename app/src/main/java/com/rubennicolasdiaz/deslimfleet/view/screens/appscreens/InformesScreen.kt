package com.rubennicolasdiaz.deslimfleet.view.screens.appscreens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rubennicolasdiaz.deslimfleet.model.enums.Barco
import com.rubennicolasdiaz.deslimfleet.model.enums.Zona
import com.rubennicolasdiaz.deslimfleet.utilities.MostrarCalendario
import com.rubennicolasdiaz.deslimfleet.view.theme.GradienteClaro
import com.rubennicolasdiaz.deslimfleet.view.theme.GradienteOscuro
import com.rubennicolasdiaz.deslimfleet.view.theme.Magenta
import com.rubennicolasdiaz.deslimfleet.view.theme.Naranja
import com.rubennicolasdiaz.deslimfleet.view.theme.Verde
import com.rubennicolasdiaz.deslimfleet.viewmodel.InformesViewModel

/**
 * Pantalla para la generación de informes de inspección.
 *
 * Permite seleccionar un barco, una zona y una fecha para generar un informe basado en las inspecciones realizadas.
 *
 * @param informesViewModel ViewModel que maneja la lógica de los informes.
 */
@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun InformesScreen(informesViewModel: InformesViewModel) {

    val context = LocalContext.current
    val activity = context as? Activity
    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    val selectedBarco by informesViewModel.selectedBarco.collectAsState()
    val selectedZona by informesViewModel.selectedZona.collectAsState()
    val selectedDate by informesViewModel.selectedDate.collectAsState()
    val isLoading by informesViewModel.isLoading.observeAsState(false)
    val errorMessage: String? by informesViewModel.errorMessage.observeAsState(null)
    val barcoDropdownExpanded by informesViewModel.barcoDropdownExpanded.collectAsState()
    val zonaDropdownExpanded by informesViewModel.zonaDropdownExpanded.collectAsState()

    BackHandler(enabled = true) {}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(GradienteOscuro, GradienteClaro)))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Selección de Informes",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        DropdownSelector(
            title = "Seleccionar Barco",
            options = Barco.entries,
            selectedOption = selectedBarco,
            expanded = barcoDropdownExpanded,
            onExpandedChange = { informesViewModel.setBarcoDropdownExpanded(it) },
            onOptionSelected = { informesViewModel.setSelectedBarco(it) },
            buttonColor = Naranja
        )

        DropdownSelector(
            title = "Seleccionar Zona",
            options = Zona.entries,
            selectedOption = selectedZona,
            expanded = zonaDropdownExpanded,
            onExpandedChange = { informesViewModel.setZonaDropdownExpanded(it) },
            onOptionSelected = { informesViewModel.setSelectedZona(it) },
            buttonColor = Verde
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                MostrarCalendario.showDatePicker(context) {
                    informesViewModel.setSelectedDate(it)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Magenta),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .height(55.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Seleccionar Fecha",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "Fecha seleccionada: $selectedDate",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        LoadingScreen(isLoading)

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { informesViewModel.generarInforme(context) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .height(70.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A))
        ) {
            Text(
                text = "Generar Informe",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
    }

    errorMessage?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        informesViewModel.clearError()
    }
}

/**
 * Componente de selección desplegable reutilizable en Jetpack Compose.
 *
 * @param T Tipo genérico de los elementos de la lista de opciones.
 * @param title Título del selector desplegable.
 * @param options Lista de opciones disponibles para la selección.
 * @param selectedOption Opción actualmente seleccionada.
 * @param expanded Indica si el menú desplegable está expandido o no.
 * @param onExpandedChange Función que actualiza el estado de expansión del menú.
 * @param onOptionSelected Función que maneja la selección de una opción.
 * @param buttonColor Color del botón de selección.
 */
@Composable
fun <T> DropdownSelector(
    title: String,
    options: List<T>,
    selectedOption: T,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onOptionSelected: (T) -> Unit,
    buttonColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Button(
            onClick = { onExpandedChange(true) },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(top = 5.dp)
        ) {
            Text(selectedOption.toString(), color = Color.White, fontSize = 18.sp)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.toString()) },
                    onClick = {
                        onOptionSelected(option)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

/**
 * Componente de pantalla de carga que muestra un indicador de progreso cuando está activo.
 *
 * @param isLoading Indica si la pantalla de carga debe mostrarse.
 */
@Composable
fun LoadingScreen(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Naranja,
                strokeWidth = 4.dp
            )
        }
    }
}