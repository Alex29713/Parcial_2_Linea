package com.example.basesdedatos.Model

import androidx.room.Embedded
import androidx.room.Relation

data class RelacionMiembrosPrestamos(
    @Embedded val miembro: Miembros,
    @Relation(
        parentColumn = "miembro_id",
        entityColumn = "miembro_id"
    )
    val prestamos: List<Prestamos>
)

