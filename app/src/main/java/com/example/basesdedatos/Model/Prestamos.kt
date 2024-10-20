package com.example.basesdedatos.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prestamos")
data class Prestamos(
   @PrimaryKey(autoGenerate = true)
    val prestamo_id: Int = 0,
    val libro_id: Int,
    val miembro_id: Int,
    val fecha_prestamo: String,
    val fecha_devolucion: String
)
