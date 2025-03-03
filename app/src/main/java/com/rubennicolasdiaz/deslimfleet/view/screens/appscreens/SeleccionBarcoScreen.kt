package com.rubennicolasdiaz.deslimfleet.view.screens.appscreens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rubennicolasdiaz.deslimfleet.view.theme.Azul
import com.rubennicolasdiaz.deslimfleet.view.theme.Celeste
import com.rubennicolasdiaz.deslimfleet.viewmodel.SeleccionBarcoViewModel

/**
 * Pantalla para la selección de un barco en la aplicación.
 *
 * Permite a los usuarios elegir un barco de la lista disponible.
 *
 * @param seleccionBarcoViewModel ViewModel que maneja la lógica de selección de barcos.
 */
@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun SeleccionarBarco(seleccionBarcoViewModel: SeleccionBarcoViewModel) {

    val context = LocalContext.current
    val activity = context as? Activity

    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Celeste)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Seleccionar Barco",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Azul
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            seleccionBarcoViewModel.barcos.forEach { (nombre, imagen) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .weight(1f)
                        .clickable { seleccionBarcoViewModel.seleccionarBarco(nombre) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.TopCenter,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(imagen),
                            contentDescription = nombre,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = nombre,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Azul
                        )
                    }
                }
            }
        }
    }
}


