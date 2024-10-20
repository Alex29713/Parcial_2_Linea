package com.example.basesdedatos.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.basesdedatos.Model.RelacionAutorLibros
import com.example.basesdedatos.Model.Autores

@Dao
interface AutorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(autor: Autores)

    @Query("SELECT * FROM autores")
    suspend fun getAllAutores(): List<Autores>

    @Query("DELETE FROM autores WHERE autor_id = :autorId")
    suspend fun deleteById(autorId: Int): Int

    @Query("UPDATE autores SET nombre = :nombre, apellido = :apellido, nacionalidad = :nacionalidad WHERE autor_id = :autorId")
    suspend fun updateById(autorId: Int, nombre: String, apellido: String,nacionalidad: String )

    @Transaction
    @Query("SELECT * FROM autores WHERE autor_id = :autorId")
    suspend fun getAutoresWithLibros(autorId: Int): List<RelacionAutorLibros>
}