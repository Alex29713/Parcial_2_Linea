package com.example.basesdedatos.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.basesdedatos.Model.Prestamos
import com.example.basesdedatos.Model.RelacionLibrosPrestamos

@Dao
interface PrestamoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prestamo: Prestamos)

    @Query("SELECT * FROM prestamos")
    suspend fun getAllPrestamos(): List<Prestamos>

    @Query("DELETE FROM prestamos WHERE prestamo_id = :prestamoId")
    suspend fun deleteByIdPrestamo(prestamoId: Int): Int

    @Query("UPDATE prestamos SET libro_id = :libroId, miembro_id = :miembroId, fecha_prestamo = :fechaPrestamo, fecha_devolucion = :fechaDevolucion WHERE prestamo_id = :prestamoId")
    suspend fun updateByIdPrestamo(prestamoId: Int, libroId: Int, miembroId: Int, fechaPrestamo: String, fechaDevolucion: String)

    @Transaction
    @Query("SELECT * FROM libros INNER JOIN prestamos ON libros.libro_id = prestamos.libro_id WHERE libros.libro_id = :libroId")
    suspend fun getPrestamosByLibrosId(libroId: Int): List<RelacionLibrosPrestamos>

    @Transaction
    @Query("SELECT * FROM prestamos WHERE miembro_id = :miembroId")
    suspend fun getPrestamosByMiembroId(miembroId: Int): List<Prestamos>

}