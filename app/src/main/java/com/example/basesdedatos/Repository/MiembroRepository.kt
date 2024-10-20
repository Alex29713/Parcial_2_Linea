package com.example.basesdedatos.Repository

import com.example.basesdedatos.DAO.MiembroDao
import com.example.basesdedatos.Model.Libros
import com.example.basesdedatos.Model.Miembros

class MiembroRepository (private val miembroDao: MiembroDao){

    suspend fun insert(miembro: Miembros){
        miembroDao.insert(miembro)
    }

    suspend fun getAllMiembros():List<Miembros>{
        return miembroDao.getAllMiembros()
    }

    suspend fun deleteByIdMiembro(miembroId: Int): Int{
        return miembroDao.deleteByIdMiembro(miembroId)
    }

    suspend fun updateById(miembroId: Int , nombre: String, apellido: String, fechaInscripcion: String){
        miembroDao.updateByIdMiembro(miembroId, nombre, apellido, fechaInscripcion)
    }

//    suspend fun getPrestamosByMiembroId(libroId: Int): List<Miembros> {
//        return miembroDao.getPrestamosByMiembroId(libroId)
//    }
}