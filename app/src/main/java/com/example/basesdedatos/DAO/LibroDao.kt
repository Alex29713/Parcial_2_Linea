package com.example.basesdedatos.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.basesdedatos.Model.Libros

@Dao
interface LibroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(libro: Libros)

    @Query("SELECT * FROM libros")
    suspend fun getAllLibros(): List<Libros>

    @Query("DELETE FROM libros WHERE libro_id = :libroId")
    suspend fun deleteByIdLibro(libroId: Int): Int

    @Query("UPDATE libros SET titulo = :titulo, genero = :genero, autor_id = :autorId WHERE libro_id = :libroId")
    suspend fun updateByIdLibro(libroId: Int, titulo: String, genero: String, autorId: Int)

    @Transaction
    @Query("SELECT * FROM libros WHERE autor_id = :autorId")
    suspend fun getLibrosByAutorId(autorId: Int): List<Libros>

}

