package com.rubennicolasdiaz.deslimfleet.view.screens.appscreens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rubennicolasdiaz.deslimfleet.R
import com.rubennicolasdiaz.deslimfleet.view.NavigationRoutes
import com.rubennicolasdiaz.deslimfleet.view.theme.Splashscreen
import kotlinx.coroutines.delay

/**
 * Pantalla de bienvenida (Splash Screen) que se muestra al iniciar la aplicación.
 *
 * Esta pantalla permanece visible durante un breve período antes de redirigir al usuario
 * a la pantalla de inicio de sesión.
 *
 * @param navHostController Controlador de navegación para gestionar la transición a la siguiente pantalla.
 */
@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun SplashScreen(navHostController: NavHostController) {


    val context = LocalContext.current
    val activity = context as? Activity

    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    LaunchedEffect(key1 = true) {
        delay(2000)
        navHostController.popBackStack()
        navHostController.navigate(NavigationRoutes.Login.route)
    }

    BackHandler(enabled = true) {
        // No hace nada, bloquea la navegación hacia atrás
    }

    Splash()
}

/**
 * Componente de la pantalla de bienvenida (Splash).
 *
 * Muestra un logo e un mensaje de bienvenida mientras se carga la aplicación.
 */
@Composable
fun Splash() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Splashscreen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.grimaldi_lines),
            contentDescription = "Logo Deslim Fleet",
            modifier = Modifier.size(350.dp)
        )
        Text(
            text = "Bienvenid@s a Deslim Fleet",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(4.dp)
        )
    }
}