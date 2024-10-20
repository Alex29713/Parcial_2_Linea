package com.example.basesdedatos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.basesdedatos.DAO.LibroDao
import com.example.basesdedatos.DAO.AutorDao
import com.example.basesdedatos.DAO.MiembroDao
import com.example.basesdedatos.DAO.PrestamoDao
import com.example.basesdedatos.Database.UserDatabase
import com.example.basesdedatos.Repository.LibroRepository
import com.example.basesdedatos.Repository.AutorRepository
import com.example.basesdedatos.Repository.MiembroRepository
import com.example.basesdedatos.Repository.PrestamoRepository
import com.example.basesdedatos.Screen.Navigation

class MainActivity : ComponentActivity() {
    private lateinit var autorDao: AutorDao
    private lateinit var libroDao: LibroDao
    private lateinit var prestamoDao: PrestamoDao
    private lateinit var miembroDao: MiembroDao

    private lateinit var autorRepository: AutorRepository
    private lateinit var libroRepository: LibroRepository
    private lateinit var prestamoRepository: PrestamoRepository
    private lateinit var miembroRepository: MiembroRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = UserDatabase.getDatabase(applicationContext)
        autorDao = db.autorDao()
        libroDao = db.libroDao()
        prestamoDao = db.prestamoDao()
        miembroDao = db.miembroDao()

        autorRepository = AutorRepository(autorDao)
        libroRepository = LibroRepository(libroDao)
        prestamoRepository = PrestamoRepository(prestamoDao)
        miembroRepository = MiembroRepository(miembroDao)

        enableEdgeToEdge()
        setContent {
            Navigation(autorRepository, libroRepository, prestamoRepository, miembroRepository)
        }
    }
}

