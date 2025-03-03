package com.rubennicolasdiaz.deslimfleet.model.enums

import androidx.compose.ui.graphics.Color
import com.rubennicolasdiaz.deslimfleet.view.theme.Revisado
import com.rubennicolasdiaz.deslimfleet.view.theme.SinRevisar

/**
 * Enumeración que representa el estado de revisión de una inspección.
 *
 * @property nombre Descripción del estado de la revisión.
 * @property color Color asociado al estado en la interfaz de usuario.
 */
enum class EstadoRevision(val nombre: String, val color: Color) {

    /** Estado cuando la inspección ha sido completada. */
    REVISADO("REVISADO", Revisado),

    /** Estado cuando la inspección aún no ha sido realizada. */
    SIN_REVISAR("PENDIENTE DE REVISAR", SinRevisar);
}
