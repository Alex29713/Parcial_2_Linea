package com.example.basesdedatos.Repository

import com.example.basesdedatos.DAO.AutorDao
import com.example.basesdedatos.Model.RelacionAutorLibros
import com.example.basesdedatos.Model.Autores

class AutorRepository(private val autorDao: AutorDao){

    suspend fun insert(autor: Autores){
        autorDao.insert(autor)
    }

    suspend fun getAllAutores():List<Autores>{
        return autorDao.getAllAutores()
    }

    suspend fun deleteById(autorId: Int): Int{
        return autorDao.deleteById(autorId)
    }

    suspend fun updateById(autorId: Int, nombre: String, apellido: String,nacionalidad: String){
        autorDao.updateById(autorId, nombre, apellido,nacionalidad)
    }

    suspend fun getAutorWithLibros(autorId: Int):List<RelacionAutorLibros>{
        return autorDao.getAutoresWithLibros(autorId)
    }

}