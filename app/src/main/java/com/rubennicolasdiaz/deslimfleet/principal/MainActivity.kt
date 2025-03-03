package com.rubennicolasdiaz.deslimfleet.principal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.rubennicolasdiaz.deslimfleet.view.NavigationWrapper
import com.rubennicolasdiaz.deslimfleet.view.theme.DeslimFleetTheme

/**
 * Actividad principal de la aplicación.
 *
 * Esta actividad configura el tema de la aplicación y gestiona la navegación.
 */
class MainActivity : ComponentActivity() {

    /**
     * Método llamado cuando la actividad es creada.
     *
     * @param savedInstanceState Estado de la actividad guardado anteriormente.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navHostController = rememberNavController()

            DeslimFleetTheme {
                NavigationWrapper(navHostController)
            }
        }
    }
}
