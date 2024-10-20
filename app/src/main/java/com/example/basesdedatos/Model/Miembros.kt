package com.example.basesdedatos.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "miembros")
data class Miembros (
    @PrimaryKey(autoGenerate = true)
    val miembro_id: Int = 0,
    val nombre: String = "",
    val apellido: String = "",
    val fecha_inscripcion: String = ""
)