package com.example.basesdedatos.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.basesdedatos.Model.Miembros

@Dao
interface MiembroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(miembro: Miembros)

    @Query("SELECT * FROM miembros")
    suspend fun getAllMiembros(): List<Miembros>

    @Query("DELETE FROM miembros WHERE miembro_id = :miembroId")
    suspend fun deleteByIdMiembro(miembroId: Int): Int

    @Query("UPDATE miembros SET nombre = :nombre, apellido = :apellido, fecha_inscripcion = :fechaInscripcion WHERE miembro_id = :miembroId")
    suspend fun updateByIdMiembro(miembroId: Int, nombre: String, apellido: String, fechaInscripcion: String )

//    @Transaction
//    @Query("SELECT * FROM prestamos WHERE miembro_id = :miembroId")
//    suspend fun getPrestamosByMiembroId(miembroId: Int): List<RelacionLibrosPrestamos>
}