package com.example.basesdedatos.Repository

import com.example.basesdedatos.DAO.PrestamoDao
import com.example.basesdedatos.Model.Prestamos
import com.example.basesdedatos.Model.RelacionLibrosPrestamos

class PrestamoRepository(private val prestamoDao: PrestamoDao) {

    suspend fun insert(prestamo: Prestamos) {
        prestamoDao.insert(prestamo)
    }

    suspend fun getAllPrestamos(): List<Prestamos> {
        return prestamoDao.getAllPrestamos()
    }

    suspend fun deleteByIdPrestamo(prestamoId: Int): Int {
        return prestamoDao.deleteByIdPrestamo(prestamoId)
    }

    suspend fun updateByIdPrestamo(prestamoId: Int, libroId: Int, miembroId: Int, fechaPrestamo: String, fechaDevolucion: String){
        return prestamoDao.updateByIdPrestamo(prestamoId, libroId, miembroId, fechaPrestamo, fechaDevolucion)
    }

    suspend fun getPrestamosByLibrosId(libroId: Int): List<RelacionLibrosPrestamos> {
        return prestamoDao.getPrestamosByLibrosId(libroId)
    }

    suspend fun getPrestamosByMiembroId(miembroId: Int): List<Prestamos> {
        return prestamoDao.getPrestamosByMiembroId(miembroId)
    }
}