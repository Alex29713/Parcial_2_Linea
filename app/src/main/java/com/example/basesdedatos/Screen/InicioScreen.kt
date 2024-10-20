package com.example.basesdedatos.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.basesdedatos.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Inicio( navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize() // Hace que el fondo ocupe todo el tamaño disponible
    ) {

        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = "Imagen de fondo",
            modifier = Modifier.fillMaxSize(), // Asegurar que la imagen ocupe toda la pantalla
            contentScale = ContentScale.Crop // Ajusta la imagen al tamaño de la pantalla
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                "Bienvenido",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Button(onClick = {
                navController.navigate("Autores")
            }, modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text("Autores")
            }

            Button(onClick = {
                navController.navigate("Libros")
            }, modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text("Libros")
            }

            Button(onClick = {
                navController.navigate("Miembros")
            }, modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text("Miembros")
            }

            Button(onClick = {
                navController.navigate("Prestamos")
            }, modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text("Prestamos")
            }

        }


    }

}