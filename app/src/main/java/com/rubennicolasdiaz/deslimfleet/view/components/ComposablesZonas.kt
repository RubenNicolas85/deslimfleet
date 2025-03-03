package com.rubennicolasdiaz.deslimfleet.view.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.rubennicolasdiaz.deslimfleet.R
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.CheckBoxInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.FotoInfo
import com.rubennicolasdiaz.deslimfleet.model.domain.datamodels.ObservacionesInfo
import com.rubennicolasdiaz.deslimfleet.view.NavigationRoutes
import com.rubennicolasdiaz.deslimfleet.view.theme.Green
import com.rubennicolasdiaz.deslimfleet.view.theme.LightGreen
import com.rubennicolasdiaz.deslimfleet.view.theme.Purple40
import com.rubennicolasdiaz.deslimfleet.view.theme.Yellow
import com.rubennicolasdiaz.deslimfleet.viewmodel.UIZonaViewModel
import java.io.ByteArrayOutputStream

/**
 * Componente de título para secciones de la aplicación.
 *
 * @param nombre Nombre o título a mostrar.
 */
@Composable
fun TitleSection(nombre: String) {
    Text(
        text = nombre,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .background(Yellow)
            .wrapContentSize(Alignment.Center)
    )
}

/**
 * Muestra una lista de checklist organizados en secciones con opciones desplegables.
 *
 * @param uIZonaViewModel ViewModel que maneja el estado de la UI.
 * @param checklistSections Mapa con secciones y elementos dentro de cada una.
 */
@Composable
fun MultipleChecklist(
    uIZonaViewModel: UIZonaViewModel,
    checklistSections: Map<String, List<String>>
) {

    LaunchedEffect(Unit) {
        checklistSections.forEach { (section, items) ->
            uIZonaViewModel.setInitialCheckBoxStates(items, section)
            uIZonaViewModel.setInitialObservaciones(items, section)
            uIZonaViewModel.setInitialFotos(items, section)
        }
    }

    val checkBoxStatesYes by uIZonaViewModel.checkBoxStatesYes.observeAsState(emptyMap())
    val checkBoxStatesNo by uIZonaViewModel.checkBoxStatesNo.observeAsState(emptyMap())
    val checkBoxStatesPlaga by uIZonaViewModel.checkBoxStatesPlaga.observeAsState(emptyMap())
    val checkBoxStatesReparacion by uIZonaViewModel.checkBoxStatesReparacion.observeAsState(emptyMap())
    val observaciones by uIZonaViewModel.observacionesMap.observeAsState(emptyMap())
    val fotos by uIZonaViewModel.fotosMap.observeAsState(emptyMap())

    val expandedSections = remember { mutableStateMapOf<String, Boolean>() }

    checklistSections.forEach { (section, items) ->
        val isExpanded = expandedSections[section] ?: false

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Green)
                    .padding(8.dp)
                    .animateContentSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()

                        .clickable {
                            expandedSections[section] = uIZonaViewModel.isSectionExpanded(section)
                        }
                        .padding(vertical = 8.dp),

                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = section,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,

                        )

                    ExpandIcon(isExpanded) {
                        expandedSections[section] = !isExpanded
                    }
                }

                if (isExpanded) {
                    items.forEach { item ->
                        val uniqueKey = "$section - $item"

                        val checkBoxInfoYes = checkBoxStatesYes[uniqueKey] ?: CheckBoxInfo(
                            uniqueKey,
                            false
                        ) { isChecked ->
                            uIZonaViewModel.toggleCheckBoxYes(uniqueKey, isChecked)
                        }
                        val checkBoxInfoNo = checkBoxStatesNo[uniqueKey] ?: CheckBoxInfo(
                            uniqueKey,
                            false
                        ) { isChecked ->
                            uIZonaViewModel.toggleCheckBoxNo(uniqueKey, isChecked)
                        }
                        val checkBoxInfoPlaga = checkBoxStatesPlaga[uniqueKey] ?: CheckBoxInfo(
                            uniqueKey,
                            false
                        ) { isChecked ->
                            uIZonaViewModel.toggleCheckBoxPlaga(uniqueKey, isChecked)
                        }
                        val checkBoxInfoReparacion =
                            checkBoxStatesReparacion[uniqueKey] ?: CheckBoxInfo(
                                uniqueKey,
                                false
                            ) { isChecked ->
                                uIZonaViewModel.toggleCheckBoxReparacion(uniqueKey, isChecked)
                            }
                        val observacionesInfo = observaciones[uniqueKey] ?: ObservacionesInfo(
                            uniqueKey,
                            ""
                        ) { newText ->
                            uIZonaViewModel.setObservaciones(uniqueKey, newText)
                        }
                        val fotoInfo = fotos[uniqueKey] ?: FotoInfo(uniqueKey, null) { newImage ->
                            uIZonaViewModel.setFoto(uniqueKey, newImage)
                        }

                        ChecklistRow(
                            checkBoxInfoYes,
                            checkBoxInfoNo,
                            checkBoxInfoPlaga,
                            checkBoxInfoReparacion,
                            observacionesInfo,
                            fotoInfo,
                            uIZonaViewModel,
                            itemName = item
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

/**
 * Fila de checklist que representa un ítem de inspección con opciones de selección y observaciones.
 *
 * @param checkBoxInfoYes Información del checkbox para la opción "Sí".
 * @param checkBoxInfoNo Información del checkbox para la opción "No".
 * @param checkBoxInfoPlaga Información del checkbox para la opción "Plaga".
 * @param checkBoxInfoReparacion Información del checkbox para la opción "Reparación".
 * @param observacionesInfo Información de las observaciones asociadas al ítem de inspección.
 * @param fotoInfo Información de la foto capturada asociada al ítem de inspección.
 * @param uIZonaViewModel ViewModel que gestiona la lógica y el estado de la inspección.
 * @param itemName Nombre del ítem de inspección mostrado en la fila.
 */
@Composable
fun ChecklistRow(
    checkBoxInfoYes: CheckBoxInfo,
    checkBoxInfoNo: CheckBoxInfo,
    checkBoxInfoPlaga: CheckBoxInfo,
    checkBoxInfoReparacion: CheckBoxInfo,
    observacionesInfo: ObservacionesInfo,
    fotoInfo: FotoInfo,
    uIZonaViewModel: UIZonaViewModel,
    itemName: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(LightGreen)
                .padding(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = itemName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.Center) {
                OptionCheckBoxYes(checkBoxInfoYes, "Sí")
                OptionCheckBoxNo(checkBoxInfoNo, "No")
                OptionCheckBoxPlaga(checkBoxInfoPlaga, "Plaga")
                OptionCheckBoxReparacion(checkBoxInfoReparacion, "Reparación")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                ObservacionesTextField(observacionesInfo)
            }
            Spacer(modifier = Modifier.height(8.dp))
            FotoCapturaField(fotoInfo, uIZonaViewModel)
        }
    }
}

/**
 * Opción de checkbox para indicar una respuesta afirmativa en la inspección.
 *
 * @param checkBoxInfo Información del estado del checkbox y su función de actualización.
 * @param label Etiqueta descriptiva para el checkbox.
 */
@Composable
fun OptionCheckBoxYes(
    checkBoxInfo: CheckBoxInfo, label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 1.dp)) {
        Checkbox(
            checked = checkBoxInfo.checked,
            onCheckedChange = { checkBoxInfo.onCheckedChange(it) }
        )
        Text(label)
    }
}

/**
 * Opción de checkbox para indicar una respuesta negativa en la inspección.
 *
 * @param checkBoxInfo Información del estado del checkbox y su función de actualización.
 * @param label Etiqueta descriptiva para el checkbox.
 */
@Composable
fun OptionCheckBoxNo(
    checkBoxInfo: CheckBoxInfo, label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 1.dp)) {
        Checkbox(
            checked = checkBoxInfo.checked,
            onCheckedChange = { checkBoxInfo.onCheckedChange(it) }
        )
        Text(label)
    }
}

/**
 * Opción de checkbox para marcar si un área presenta indicios de plaga en la inspección.
 *
 * @param checkBoxInfo Información del estado del checkbox y su función de actualización.
 * @param label Etiqueta descriptiva para el checkbox.
 */
@Composable
fun OptionCheckBoxPlaga(
    checkBoxInfo: CheckBoxInfo, label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 1.dp)) {
        Checkbox(
            checked = checkBoxInfo.checked,
            onCheckedChange = { checkBoxInfo.onCheckedChange(it) }
        )
        Text(label)
    }
}

/**
 * Opción de checkbox para marcar si un elemento requiere reparación en la inspección.
 *
 * @param checkBoxInfo Información del estado del checkbox y su función de actualización.
 * @param label Etiqueta descriptiva para el checkbox.
 */
@Composable
fun OptionCheckBoxReparacion(
    checkBoxInfo: CheckBoxInfo, label: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checkBoxInfo.checked,
            onCheckedChange = { checkBoxInfo.onCheckedChange(it) }
        )
        Text(
            label,
            fontSize = 15.sp
        )
    }
}

/**
 * Campo de texto para ingresar observaciones en la inspección.
 *
 * @param observacionInfo Información de la observación, incluyendo el texto y la función de actualización.
 */
@Composable
fun ObservacionesTextField(observacionInfo: ObservacionesInfo) {
    TextField(
        value = observacionInfo.text,
        onValueChange = { newText ->
            observacionInfo.onTextChange(newText)
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Observaciones") },
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Color.LightGray,
            focusedIndicatorColor = Color.Green
        )
    )
}

/**
 * Componente que permite capturar una foto utilizando la cámara del dispositivo y mostrarla en la interfaz de usuario.
 *
 * @param fotoInfo Información de la foto capturada, incluyendo su representación en Base64.
 * @param uIZonaViewModel ViewModel que gestiona el estado de la imagen capturada.
 */
@Composable
fun FotoCapturaField(fotoInfo: FotoInfo, uIZonaViewModel: UIZonaViewModel) {

    val context = LocalContext.current
    var currentImageBase64 by remember { mutableStateOf(fotoInfo.imageBase64) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val photo = result.data?.extras?.get("data") as? Bitmap
            photo?.let {
                val rotatedBitmap = uIZonaViewModel.rotateBitmapIfNeeded(it)
                val outputStream = ByteArrayOutputStream()
                rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                val byteArray = outputStream.toByteArray()
                val base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)


                fotoInfo.imageBase64 = base64
                fotoInfo.onImageChange(base64)
                uIZonaViewModel.setImageBase64(base64)
                currentImageBase64 = base64
            }
        }
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraLauncher.launch(cameraIntent)
        }
    }

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) -> {
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraLauncher.launch(cameraIntent)
                    }

                    else -> {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = "Capturar foto",
                    tint = Purple40
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        currentImageBase64?.let { base64Image ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .padding(8.dp)
                ) {
                    val decodedImage = remember(base64Image) {
                        val byteArray = Base64.decode(base64Image, Base64.DEFAULT)
                        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    }
                    Image(
                        alignment = Alignment.Center,
                        bitmap = decodedImage.asImageBitmap(),
                        contentDescription = "Miniatura de la imagen capturada",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                    )
                    IconButton(
                        onClick = {

                            currentImageBase64 = null
                            fotoInfo.imageBase64 = null
                            fotoInfo.onImageChange(null)
                            uIZonaViewModel.setImageBase64(null)
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(24.dp)
                            .background(Color.Red, shape = CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete_photo),
                            contentDescription = "Eliminar imagen",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Muestra un cuadro de diálogo de confirmación para salir de la revisión de zona.
 *
 * @param uIZonaViewModel ViewModel que maneja el estado de la UI en la zona de inspección.
 * @param navHostController Controlador de navegación para redirigir la pantalla si el usuario confirma la salida.
 */
@Composable
fun DialogSalir(uIZonaViewModel: UIZonaViewModel, navHostController: NavHostController) {
    val showDialog by uIZonaViewModel.showDialogSalir.observeAsState(false)

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { uIZonaViewModel.setShowDialogSalir(false) },
            title = {
                Text(
                    text = "Salir de revisión de zona",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = "¿Está seguro de que desea salir? \nSe perderá el progreso de la revisión",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Justify
                )
            },
            confirmButton = {
                Button(onClick = {
                    navHostController.navigate(NavigationRoutes.EstadoRevisiones.route)
                    uIZonaViewModel.setShowDialogSalir(false)
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = { uIZonaViewModel.setShowDialogSalir(false) }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * Muestra un cuadro de diálogo de confirmación antes de guardar la revisión en la base de datos.
 *
 * @param currentUser Usuario actual que realiza la inspección.
 * @param uIZonaViewModel ViewModel que maneja el estado de la UI y la lógica de la inspección.
 * @param navHostController Controlador de navegación para manejar la redirección después de la confirmación.
 * @param barcoNombre Nombre del barco asociado a la revisión.
 * @param zonaNombre Nombre de la zona inspeccionada.
 */
@Composable
fun DialogConfirm(
    currentUser: String,
    uIZonaViewModel: UIZonaViewModel,
    navHostController: NavHostController,
    barcoNombre: String,
    zonaNombre: String
) {
    val showDialog by uIZonaViewModel.showDialogConfirmar.observeAsState(false)
    val context = LocalContext.current

    val showDialogSuccess by uIZonaViewModel.showDialogSuccess.observeAsState(false)
    val showDialogFailure by uIZonaViewModel.showDialogFailure.observeAsState(false)

    LaunchedEffect(showDialogSuccess) {
        if (showDialogSuccess) {
            Toast.makeText(
                context,
                "Éxito al guardar la revisión en la base de datos.",
                Toast.LENGTH_LONG
            ).show()
            navHostController.navigate(NavigationRoutes.EstadoRevisiones.route)
            uIZonaViewModel.setShowDialogSuccess(false)
        }
    }

    LaunchedEffect(showDialogFailure) {
        if (showDialogFailure) {
            Toast.makeText(
                context,
                "Error al guardar. Ya se había registrado inspección en la misma zona hoy.",
                Toast.LENGTH_LONG
            ).show()
            navHostController.navigate(NavigationRoutes.EstadoRevisiones.route)
            uIZonaViewModel.setShowDialogFailure(false)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { uIZonaViewModel.setShowDialogConfirmar(false) },
            title = {
                Text(
                    text = "Finalizar Revisión",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text =
                    "Se va a guardar la revisión en la base de datos. ¿Está seguro de que desea continuar?" +
                            " Si necesita modificar algún campo, pulse cancelar. Si está todo correcto, pulse aceptar.",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Justify
                )
            },
            confirmButton = {
                Button(onClick = {
                    uIZonaViewModel.guardarChecklist(
                        currentUser,
                        barcoNombre,
                        zonaNombre,
                        uIZonaViewModel.checkBoxStatesYes.value ?: emptyMap(),
                        uIZonaViewModel.checkBoxStatesNo.value ?: emptyMap(),
                        uIZonaViewModel.checkBoxStatesPlaga.value ?: emptyMap(),
                        uIZonaViewModel.checkBoxStatesReparacion.value ?: emptyMap(),
                        uIZonaViewModel.observacionesMap.value ?: emptyMap(),
                        uIZonaViewModel.fotosMap.value ?: emptyMap(),
                        onSuccess = {},
                        onFailure = {}
                    )
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = { uIZonaViewModel.setShowDialogConfirmar(false) }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * Botón de finalización de la revisión que permite confirmar la inspección.
 *
 * @param uIZonaViewModel ViewModel que maneja la lógica de la UI para la revisión de la zona.
 */
@Composable
fun FinishButton(uIZonaViewModel: UIZonaViewModel) {
    val isEnabled by uIZonaViewModel.isButtonEnabled.observeAsState(false)

    Button(
        onClick = {
            uIZonaViewModel.onConfirmarButtonClicked()
        },
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .height(80.dp)
    ) {
        Text(
            text = "Finalizar Revisión",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Maneja el evento de retroceso en la aplicación y muestra un cuadro de diálogo de confirmación para salir.
 *
 * @param uIZonaViewModel ViewModel que gestiona el estado de la interfaz de usuario en la zona de inspección.
 */
@Composable
fun HandleBackPress(uIZonaViewModel: UIZonaViewModel) {
    BackHandler {
        uIZonaViewModel.setShowDialogSalir(true)
    }
}

/**
 * Manejador de estado de desplazamiento para su uso en listas o contenido desplazable en Jetpack Compose.
 *
 * @return Estado de desplazamiento recordado que se puede usar en componentes desplazables.
 */
@Composable
fun scrollStateHandler(): ScrollState {
    return rememberScrollState()
}

/**
 * Muestra un icono de expansión para listas desplegables.
 *
 * @param isExpanded Indica si la sección está expandida.
 * @param onClick Acción a ejecutar al hacer clic en el icono.
 */
@Composable
fun ExpandIcon(isExpanded: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = if (isExpanded) "Contraer" else "Expandir"
        )
    }
}