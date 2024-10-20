package com.example.basesdedatos.Model

import androidx.room.Embedded
import androidx.room.Relation

data class RelacionAutorLibros(
    @Embedded val autor: Autores,
    @Relation(
        parentColumn = "autor_id",
        entityColumn = "autor_id"
    )
    val libros: List<Libros>
)

