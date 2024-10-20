package com.example.basesdedatos.Repository

import com.example.basesdedatos.DAO.LibroDao
import com.example.basesdedatos.Model.Libros

class LibroRepository(private val libroDao: LibroDao){

    suspend fun insert(libro: Libros){
        libroDao.insert(libro)
    }

    suspend fun getAllLibros():List<Libros>{
        return libroDao.getAllLibros()
    }

    suspend fun deleteByIdLibro(libroId: Int): Int{
        return libroDao.deleteByIdLibro(libroId)
    }

    suspend fun updateById(libroId: Int , titulo: String, genero: String, autorId: Int){
        libroDao.updateByIdLibro(libroId, titulo, genero, autorId)
    }

    suspend fun getLibrosByAutorId(autorId: Int): List<Libros> {
        return libroDao.getLibrosByAutorId(autorId)
    }

}
