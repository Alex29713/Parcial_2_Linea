package com.example.basesdedatos.Screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.basesdedatos.Model.Miembros
import com.example.basesdedatos.Repository.LibroRepository
import com.example.basesdedatos.Repository.AutorRepository
import com.example.basesdedatos.Repository.MiembroRepository
import com.example.basesdedatos.Repository.PrestamoRepository

@Composable
fun Navigation(autorRepository: AutorRepository, libroRepository: LibroRepository, prestamoRepository: PrestamoRepository, miembroRepository: MiembroRepository){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Inicio"){
        composable("Inicio"){
            Inicio(navController)
        }

        composable("Libros"){
            LibrosApp(libroRepository = libroRepository, autorRepository = autorRepository, navController)
        }

        composable("Autores") {
            AutorApp(autorRepository = autorRepository, navController)
        }

        composable("Miembros") {
            MiembroApp(miembroRepository = miembroRepository, navController)
        }
        composable("Prestamos") {
            PrestamosApp(libroRepository = libroRepository, miembroRepository = miembroRepository, prestamoRepository = prestamoRepository, navController)
        }

    }

}