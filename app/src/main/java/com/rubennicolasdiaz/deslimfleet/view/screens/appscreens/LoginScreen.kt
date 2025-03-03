package com.rubennicolasdiaz.deslimfleet.view.screens.appscreens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rubennicolasdiaz.deslimfleet.view.theme.White
import com.rubennicolasdiaz.deslimfleet.R
import com.rubennicolasdiaz.deslimfleet.view.theme.Pink80
import com.rubennicolasdiaz.deslimfleet.view.theme.PurpleGrey80
import com.rubennicolasdiaz.deslimfleet.viewmodel.LoginViewModel

/**
 * Pantalla de inicio de sesión de la aplicación.
 *
 * Permite a los usuarios ingresar su correo electrónico y contraseña para autenticarse.
 *
 * @param loginViewModel ViewModel que maneja la lógica de autenticación.
 */
@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun LoginScreen(loginViewModel: LoginViewModel) {

    BackHandler(enabled = true) {
        // No hace nada, bloquea la navegación hacia atrás
    }

    val context = LocalContext.current
    val activity = context as? Activity

    val scrollState = rememberScrollState()

    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    val email: String by loginViewModel.email.observeAsState("")
    val password: String by loginViewModel.password.observeAsState("")
    val loginEnabled: Boolean by loginViewModel.loginEnabled.observeAsState(false)
    val errorMessage: String? by loginViewModel.errorMessage.observeAsState(null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(painter = painterResource(id = R.drawable.trasmed_logo), contentDescription = "Logo")
        Spacer(modifier = Modifier.weight(0.5f))


        Row(modifier = Modifier.padding(10.dp)) {
            Text("Email", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        }
        Row(modifier = Modifier.padding(20.dp)) {
            TextField(
                label = { Text("Introduce tu email") },
                value = email,
                onValueChange = { loginViewModel.onValueChanged(it, password) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = PurpleGrey80,
                    focusedContainerColor = Pink80,
                )
            )
        }

        Row(modifier = Modifier.padding(10.dp)) {

            Text("Password", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 40.sp)
        }

        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
            TextField(
                label = { Text("Introduce tu contraseña") },
                value = password,
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = { loginViewModel.onValueChanged(email, it) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = PurpleGrey80,
                    focusedContainerColor = Pink80,
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(20.dp),
            onClick = { loginViewModel.onLoginSelected() },
            enabled = loginEnabled
        ) {
            Text(text = "Login", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }

    errorMessage?.let { message ->

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            loginViewModel.clearError()
    }
}