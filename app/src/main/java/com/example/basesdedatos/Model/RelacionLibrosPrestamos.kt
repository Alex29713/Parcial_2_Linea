package com.example.basesdedatos.Model

import androidx.room.Embedded
import androidx.room.Relation

data class RelacionLibrosPrestamos(
    @Embedded val libro: Libros,
    @Relation(
        parentColumn = "libro_id",
        entityColumn = "libro_id"
    )
    val prestamos: List<Prestamos>
)

