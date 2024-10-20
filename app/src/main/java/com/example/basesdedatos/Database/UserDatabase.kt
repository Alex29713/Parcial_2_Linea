package com.example.basesdedatos.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.basesdedatos.DAO.AutorDao
import com.example.basesdedatos.DAO.LibroDao
import com.example.basesdedatos.DAO.MiembroDao
import com.example.basesdedatos.DAO.PrestamoDao
import com.example.basesdedatos.Model.Autores
import com.example.basesdedatos.Model.Libros
import com.example.basesdedatos.Model.Miembros
import com.example.basesdedatos.Model.Prestamos


// Incluye ambas entidades (User y Coche)
@Database(entities = [Autores::class, Libros::class, Miembros::class, Prestamos::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    // Define los DAOs
    abstract fun autorDao(): AutorDao
    abstract fun libroDao(): LibroDao
    abstract fun miembroDao(): MiembroDao
    abstract fun prestamoDao(): PrestamoDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        // Retorna la instancia de la base de datos
        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}