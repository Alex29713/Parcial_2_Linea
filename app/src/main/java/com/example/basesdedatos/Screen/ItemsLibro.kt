package com.example.basesdedatos.Screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.basesdedatos.Model.Autores
import com.example.basesdedatos.Model.Libros
import com.example.basesdedatos.R
import com.example.basesdedatos.Repository.AutorRepository
import com.example.basesdedatos.Repository.LibroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrosApp(libroRepository: LibroRepository, autorRepository: AutorRepository, navController: NavController) {

    var titulo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var autor_id by remember { mutableStateOf("") }

    var selectedLibroId by remember { mutableStateOf<Int?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var expandedLibro by remember { mutableStateOf(false) }

    var libros by remember { mutableStateOf(listOf<Libros>()) }
    var autores by remember { mutableStateOf(listOf<Autores>()) }
    var selectedAutor by remember { mutableStateOf<Autores?>(null) }


    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showListDialog by remember { mutableStateOf(false) } // Controla el diálogo de lista
    var showList by remember { mutableStateOf(false) } // Controla el diálogo de lista
    var showListDialog2 by remember { mutableStateOf(false) } // Controla el diálogo de lista
    var showListLibrosById by remember { mutableStateOf(false) } // Controla el diálogo de lista

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

        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campos de texto redondeados
            item {
                Text(
                    text = "Libros",
                    fontWeight = FontWeight.Bold,
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            item {
                TextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Build, // Ícono de persona
                            contentDescription = "Icono de Titulo",
                            tint = Color.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF0F0F0),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }

            item {
                TextField(
                    value = genero,
                    onValueChange = { genero = it },
                    label = { Text("Genero") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder, // Ícono de persona
                            contentDescription = "Icono de Genero",
                            tint = Color.Gray
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF0F0F0), // Fondo gris claro
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color.Black, // Color de la etiqueta cuando está enfocada
                        unfocusedLabelColor = Color.Black, // Color de la etiqueta cuando no está enfocada
                        cursorColor = Color.Black // Color del cursor
                    ),
                    textStyle = TextStyle(color = Color.Black) // Esto asegura que el texto sea negro
                )
            }

            item {
                LaunchedEffect(Unit) {
                    autores = withContext(Dispatchers.IO) {
                        autorRepository.getAllAutores() // Método para obtener todas las personas
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth(), // Se asegura de que el Box ocupe todo el ancho
                    contentAlignment = Alignment.Center // Centra el contenido en el medio del Box
                ) {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable { expanded = !expanded },
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF5C73EC)
                        ),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Face, // Ícono de persona
                                contentDescription = "Icono de Autor",
                                tint = Color.Black
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (selectedAutor != null) {
                                    "Autor ID: ${selectedAutor!!.autor_id} seleccionado"
                                } else {
                                    "Seleccionar Autor"
                                }
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.heightIn(max = 200.dp)
                                .align(Alignment.CenterHorizontally)
                                .background(Color(0xFF5C73EC))
                        ) {
                            autores.forEach { autor ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.Person,  // Cambia este icono si deseas otro
                                                contentDescription = "Icono de autor",
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    text = "Nombre: ${autor.nombre} ${autor.apellido}",  // Muestra el nombre y apellido del autor
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Nacionalidad: ${autor.nacionalidad}",  // Muestra la nacionalidad
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        selectedAutor = autor
                                        expanded = false
                                    }, modifier = Modifier.padding(8.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }

            item {
                // Botón para registrar usuario
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Botón Registrar
                    Button(
                        onClick = {
                            // Verificación de campos vacíos
                            if (titulo.isNotEmpty() && genero.isNotEmpty() && selectedAutor != null) {
                                val personaId =
                                    selectedAutor!!.autor_id // Accede a la propiedad id de selectedUser solo si no es nulo
                                val libro = Libros(
                                    titulo = titulo,
                                    genero = genero,
                                    autor_id = personaId
                                )
                                scope.launch {
                                    withContext(Dispatchers.IO) {
                                        libroRepository.insert(libro)
                                    }
                                }
                                titulo = ""
                                genero = ""
                                autor_id = ""
                                selectedAutor = null
                                Toast.makeText(context, "Libro Registrado", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Rellene todos los campos ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier
                            .weight(1f) // Asigna el mismo peso a este botón
                            .padding(8.dp), // Espacio alrededor
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xE86200EE), // Fondo morado oscuro
                            contentColor = Color.White          // Texto en blanco
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle, // Icono de Material Design
                            contentDescription = "Crear",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el icono y el texto
                        Text("Crear")
                    }

                    // Botón Listar
                    Button(
                        onClick = {
                            scope.launch {
                                libros = withContext(Dispatchers.IO) {
                                    libroRepository.getAllLibros()
                                }
                                showList = true // Mostrar la ventana emergente
                            }
                        },
                        modifier = Modifier
                            .weight(1f) // Asigna el mismo peso a este botón
                            .padding(8.dp), // Espacio alrededor
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50), // Fondo verde
                            contentColor = Color.White          // Texto en blanco
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.List, // Icono de Material Design
                            contentDescription = "Listar",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Listar")
                    }

                    // Botón Editar/Eliminar
                    Button(
                        onClick = {
                            scope.launch {
                                libros = withContext(Dispatchers.IO) {
                                    libroRepository.getAllLibros()
                                }
                                showListDialog = true // Mostrar la ventana emergente
                            }
                        },
                        modifier = Modifier
                            .weight(1f) // Asigna el mismo peso a este botón
                            .padding(8.dp), // Espacio alrededor
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF44336), // Fondo Rojo
                            contentColor = Color.White          // Texto en blanco
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit, // Icono de Material Design
                            contentDescription = "Edit/Delete",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = "Edit/Delete")
                    }
                }
            }


            item {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("Inicio") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50), // Fondo rojo
                        contentColor = Color.White          // Texto en blanco
                    )
                ) {
                    Text(text = "Ir a Inicio")
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                if (showListDialog2) {
                    AlertDialog(
                        onDismissRequest = { showListDialog2 = false },
                        title = { Text(text = "Libros Registrados") },
                        text = {
                            LazyColumn(
                                modifier = Modifier
                                    .heightIn(max = 300.dp)
                            ) {
                                item {
                                    libros.forEach { libro ->
                                        Card(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .fillMaxWidth()
                                                .clickable { expanded = !expanded },
                                            colors = CardDefaults.cardColors(),
                                            elevation = CardDefaults.cardElevation(8.dp),
                                            onClick = {
                                                selectedLibroId = libro.libro_id
                                                titulo = libro.titulo
                                                genero = libro.genero
                                                autor_id = libro.autor_id.toString()
                                                selectedAutor =
                                                    autores.find { it.autor_id == libro.autor_id }
                                                showListDialog =
                                                    true // Mostrar la ventana emergente
                                            }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(16.dp),
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Column(
                                                    modifier = Modifier.padding(16.dp)
                                                ) {
                                                    Row {
                                                        Text(
                                                            text = "Título: ",
                                                            fontWeight = FontWeight.Bold,
                                                        )
                                                        Text(text = libro.titulo)
                                                    }

                                                    Row {
                                                        Text(
                                                            text = "Genero: ",
                                                            fontWeight = FontWeight.Bold,
                                                        )
                                                        Text(text = libro.genero)
                                                    }


                                                    Row {
                                                        Text(
                                                            text = "Autor: ",
                                                            fontWeight = FontWeight.Bold,
                                                        )
                                                        Text(text = "${autores.find { it.autor_id == libro.autor_id }?.nombre}")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                // Limpiar campos cuando se confirme el cierre del diálogo
                                titulo = ""
                                genero = ""
                                autor_id = ""
                                showListDialog2 = false
                                selectedAutor = null
                                null.also { selectedLibroId = it }
                            }) {
                                Text("Cerrar")
                            }
                        }
                    )
                }
            }

            item {
                if (showListLibrosById) {
                    AlertDialog(
                        onDismissRequest = { showListLibrosById = false },
                        title = { Text(text = "Libros Registrados Por Autor") },
                        text = {
                            LazyColumn(
                                modifier = Modifier
                                    .heightIn(max = 300.dp)
                            ) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(), // Se asegura de que el Box ocupe todo el ancho
                                        contentAlignment = Alignment.Center // Centra el contenido en el medio del Box
                                    ) {
                                        Card(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .fillMaxWidth()
                                                .clickable { expanded = !expanded },
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0xFF5C73EC),
                                            ),
                                            elevation = CardDefaults.cardElevation(8.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(16.dp),
                                                horizontalArrangement = Arrangement.Center,
                                            ) {
                                                Text(
                                                    text = if (selectedAutor != null) {
                                                        "Usuario ID: ${selectedAutor!!.autor_id!!} seleccionado"
                                                    } else {
                                                        "Seleccionar Autor"
                                                    }
                                                )
                                            }
                                            DropdownMenu(
                                                expanded = expanded,
                                                onDismissRequest = { expanded = false },
                                                modifier = Modifier.heightIn(max = 200.dp)
                                                .background(Color(0xFF5C73EC))
                                            ) {
                                                autores.forEach { autor ->
                                                    DropdownMenuItem(
                                                        text = {
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Icon(
                                                                    Icons.Default.Person,  // Cambia este icono si deseas otro
                                                                    contentDescription = "Icono de autor",
                                                                    modifier = Modifier.size(12.dp)
                                                                )
                                                                Spacer(modifier = Modifier.width(8.dp))
                                                                Column {
                                                                    Text(
                                                                        text = "${autor.nombre} ${autor.apellido}",  // Muestra el nombre completo del autor
                                                                        fontWeight = FontWeight.Bold
                                                                    )
                                                                    Spacer(
                                                                        modifier = Modifier.height(
                                                                            4.dp
                                                                        )
                                                                    )
                                                                    Text(
                                                                        text = "Nacionalidad: ${autor.nacionalidad}",  // Muestra la nacionalidad del autor
                                                                        style = MaterialTheme.typography.bodySmall
                                                                    )
                                                                }
                                                            }
                                                        },
                                                        onClick = {
                                                            selectedAutor = autor
                                                            expanded = false

                                                            scope.launch {
                                                                withContext(Dispatchers.IO) {
                                                                    libros =
                                                                        libroRepository.getLibrosByAutorId(
                                                                            selectedAutor!!.autor_id
                                                                        ) // Método para obtener coches por ID de usuario
                                                                }
                                                            }
                                                        }, modifier = Modifier.padding(8.dp)
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                }
                                            }
                                        }
                                    }
                                }

                                item {
                                    libros.forEach { libro ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                            colors = CardDefaults.cardColors(),
                                            elevation = CardDefaults.cardElevation(8.dp),
                                            onClick = {
                                                selectedLibroId = libro.libro_id
                                                titulo = libro.titulo
                                                genero = libro.genero
                                                autor_id = libro.autor_id.toString()
                                                selectedAutor =
                                                    autores.find { it.autor_id == libro.autor_id }
                                                showListDialog =
                                                    true // Mostrar la ventana emergente
                                            }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(16.dp),
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = "Titulo: ${libro.titulo}, Genero: ${libro.genero} Autor: ${autores.find { it.autor_id == libro.autor_id }?.nombre}"
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                // Limpiar campos cuando se confirme el cierre del diálogo
                                titulo = ""
                                genero = ""
                                autor_id = ""
                                showListLibrosById = false
                                selectedAutor = null
                            }) {
                                Text("Cerrar")
                            }
                        }
                    )
                }
            }


            item {
                // Ventana emergente con lista de usuarios, editar y eliminar
                if (showListDialog) {
                    AlertDialog(
                        onDismissRequest = { showListDialog = false },
                        title = { Text(text = "Libros Registrados") },
                        text = {
                            LazyColumn {
                                // Dropdown para seleccionar usuario
                                item {

                                    Box(
                                        modifier = Modifier.fillMaxWidth(), // Se asegura de que el Box ocupe todo el ancho
                                        contentAlignment = Alignment.Center // Centra el contenido en el medio del Box
                                    ) {

                                        Card(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .fillMaxWidth()
                                                .clickable { expandedLibro = !expandedLibro },
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0xFF237CC7),
                                            ),
                                            elevation = CardDefaults.cardElevation(8.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(16.dp),
                                                horizontalArrangement = Arrangement.Center,
                                            ) {
                                                Text(
                                                    text = if (selectedLibroId != null) {
                                                        "Libro ID: ${selectedLibroId} seleccionado"
                                                    } else {
                                                        "Seleccionar Libro"
                                                    }
                                                )
                                            }
                                            DropdownMenu(
                                                expanded = expandedLibro,
                                                onDismissRequest = { expandedLibro = false },
                                                modifier = Modifier.heightIn(max = 200.dp)
                                                    .align(Alignment.CenterHorizontally)
                                                    .background(Color(0xFF237CC7))
                                            ) {
                                                libros.forEach { libro ->
                                                    DropdownMenuItem(
                                                        text = {
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Icon(
                                                                    Icons.Default.PlayArrow,  // Cambia este icono si deseas otro
                                                                    contentDescription = "Icono de libro",
                                                                    modifier = Modifier.size(12.dp)
                                                                )
                                                                Spacer(modifier = Modifier.width(8.dp))
                                                                Column {
                                                                    Text(
                                                                        text = "Título: ${libro.titulo}",  // Muestra el título del libro
                                                                        fontWeight = FontWeight.Bold
                                                                    )
                                                                    Spacer(
                                                                        modifier = Modifier.height(
                                                                            4.dp
                                                                        )
                                                                    )
                                                                    Text(
                                                                        text = "Género: ${libro.genero}",  // Muestra el género del libro
                                                                        style = MaterialTheme.typography.bodySmall
                                                                    )
                                                                    Spacer(
                                                                        modifier = Modifier.height(
                                                                            4.dp
                                                                        )
                                                                    )
                                                                    Text(
                                                                        text = "Autor: ${autores.find { it.autor_id == libro.autor_id }?.nombre ?: "Desconocido"}",  // Muestra el nombre del autor, si existe
                                                                        style = MaterialTheme.typography.bodySmall
                                                                    )
                                                                }
                                                            }
                                                        },
                                                        onClick = {
                                                            selectedLibroId = libro.libro_id
                                                            titulo = libro.titulo
                                                            genero = libro.genero
                                                            autor_id = libro.autor_id.toString()
                                                            selectedAutor =
                                                                autores.find { it.autor_id == libro.autor_id }
                                                            expandedLibro = false
                                                        }, modifier = Modifier.padding(8.dp)
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                }
                                            }
                                        }
                                    }
                                }



                                item {
                                    TextField(
                                        value = titulo,
                                        onValueChange = { titulo = it },
                                        label = { Text("Título") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = TextFieldDefaults.textFieldColors(
                                            containerColor = Color(0xFFF0F0F0), // Fondo gris claro
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            focusedLabelColor = Color.Black, // Color de la etiqueta cuando está enfocada
                                            unfocusedLabelColor = Color.Black, // Color de la etiqueta cuando no está enfocada
                                            cursorColor = Color.Black // Color del cursor
                                        ),
                                        textStyle = TextStyle(color = Color.Black) // Esto asegura que el texto sea negro
                                    )
                                }

                                item {
                                    TextField(
                                        value = genero,
                                        onValueChange = { genero = it },
                                        label = { Text("Genero") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = TextFieldDefaults.textFieldColors(
                                            containerColor = Color(0xFFF0F0F0), // Fondo gris claro
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            focusedLabelColor = Color.Black, // Color de la etiqueta cuando está enfocada
                                            unfocusedLabelColor = Color.Black, // Color de la etiqueta cuando no está enfocada
                                            cursorColor = Color.Black // Color del cursor
                                        ),
                                        textStyle = TextStyle(color = Color.Black) // Esto asegura que el texto sea negro
                                    )
                                }

                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(), // Se asegura de que el Box ocupe todo el ancho
                                        contentAlignment = Alignment.Center // Centra el contenido en el medio del Box
                                    ) {
                                        Card(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .fillMaxWidth()
                                                .clickable { expanded = !expanded },
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0xFF5C73EC),
                                            ),
                                            elevation = CardDefaults.cardElevation(8.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(16.dp),
                                                horizontalArrangement = Arrangement.Center,
                                            ) {
                                                Text(
                                                    text = if (selectedAutor != null) {
                                                        "Autor ID: ${autor_id} seleccionado"
                                                    } else {
                                                        "Seleccionar Autor"
                                                    }
                                                )
                                            }
                                                DropdownMenu(
                                                    expanded = expanded,
                                                    onDismissRequest = { expanded = false },
                                                    modifier = Modifier.heightIn(max = 200.dp)
                                                        .align(Alignment.CenterHorizontally)
                                                        .background(Color(0xFF5C73EC))
                                                ) {
                                                    autores.forEach { autor ->
                                                        DropdownMenuItem(
                                                            text = {
                                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                                    Icon(
                                                                        Icons.Default.Person,  // Cambia este icono si deseas otro
                                                                        contentDescription = "Icono de autor",
                                                                        modifier = Modifier.size(12.dp)
                                                                    )
                                                                    Spacer(
                                                                        modifier = Modifier.width(
                                                                            8.dp
                                                                        )
                                                                    )
                                                                    Column {
                                                                        Text(
                                                                            text = "Nombre: ${autor.nombre} ${autor.apellido}",  // Muestra el nombre completo del autor
                                                                            fontWeight = FontWeight.Bold
                                                                        )
                                                                        Spacer(
                                                                            modifier = Modifier.height(
                                                                                4.dp
                                                                            )
                                                                        )
                                                                        Text(
                                                                            text = "Nacionalidad: ${autor.nacionalidad}",  // Muestra la nacionalidad del autor
                                                                            style = MaterialTheme.typography.bodySmall
                                                                        )
                                                                    }
                                                                }
                                                            },
                                                            onClick = {
                                                                selectedAutor = autor
                                                                expanded = false

                                                                scope.launch {
                                                                    withContext(Dispatchers.IO) {
                                                                        autores =
                                                                            autorRepository.getAllAutores() // Método para obtener coches por ID de usuario
                                                                    }
                                                                }
                                                                autor_id = autor.autor_id.toString()
                                                            }, modifier = Modifier.padding(8.dp)
                                                        )
                                                        Spacer(modifier = Modifier.height(4.dp))
                                                    }
                                                }
                                            }
                                        }

                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                item {
                                    // Botones de Editar y Eliminar
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        TextButton(onClick = {
                                            if (selectedLibroId == null) {
                                                // Mostrar mensaje de error si no se ha seleccionado un usuario
                                                Toast.makeText(
                                                    context,
                                                    "Por favor, seleccione un libro primero",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                if (titulo.isNotEmpty() && genero.isNotEmpty() && autor_id.isNotEmpty()) {
                                                    showEditDialog = true
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Rellene todos los campos",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }) {
                                            Text("Actualizar")
                                        }

                                        TextButton(onClick = {
                                            if (selectedLibroId == null) {
                                                // Mostrar mensaje de error si no se ha seleccionado un usuario
                                                Toast.makeText(
                                                    context,
                                                    "Por favor, seleccione un libro primero",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                showDeleteDialog = true
                                            }
                                        }) {
                                            Text("Eliminar")
                                        }
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                // Limpiar campos cuando se confirme el cierre del diálogo
                                titulo = ""
                                genero = ""
                                autor_id = ""
                                showListDialog = false
                            }) {
                                Text("Cerrar")
                            }
                        }
                    )
                }

                // Diálogos de confirmación para eliminar y editar (igual que antes)
                if (showEditDialog) {
                    AlertDialog(
                        onDismissRequest = { showEditDialog = false },
                        title = { Text(text = "Confirmar Edición") },
                        text = { Text(text = "¿Está seguro de que desea actualizar este libro?") },
                        confirmButton = {
                            TextButton(onClick = {
                                selectedLibroId?.let { libroId ->
                                    scope.launch {
                                        try {
                                            withContext(Dispatchers.IO) {
                                                libroRepository.updateById(
                                                    libroId,
                                                    titulo,
                                                    genero,
                                                    autor_id.toInt()
                                                )
                                            }
                                            // Refrescar la lista de usuarios después de la actualización
                                            libros = withContext(Dispatchers.IO) {
                                                libroRepository.getAllLibros()
                                            }

                                            // Mostrar mensaje de éxito y limpiar los campos
                                            Toast.makeText(
                                                context,
                                                "Libro Actualizado",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            // Limpiar los campos
                                            titulo = ""
                                            genero = ""
                                            autor_id = ""
                                            showEditDialog = false
                                            showListDialog = false
                                            selectedLibroId =
                                                null // Cerrar también el menú de lista
                                            selectedAutor = null // Cerrar también el menú de lista

                                        } catch (e: Exception) {
                                            // En caso de error, mostrar mensaje de error
                                            Toast.makeText(
                                                context,
                                                "Error al actualizar el libro",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }) {
                                Text("Actualizar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showEditDialog = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }

                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text(text = "Confirmar Eliminación") },
                        text = { Text(text = "¿Está seguro de que desea eliminar este libro?") },
                        confirmButton = {
                            TextButton(onClick =
                            {
                                selectedLibroId?.let { libroId ->
                                    scope.launch {
                                        try {
                                            withContext(Dispatchers.IO) {
                                                libroRepository.deleteByIdLibro(libroId)
                                            }

                                            // Mostrar mensaje de éxito y limpiar los campos
                                            Toast.makeText(
                                                context,
                                                "Libro Eliminado",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            titulo = ""
                                            genero = ""
                                            autor_id = ""
                                            showDeleteDialog = false
                                            showListDialog =
                                                false // Cerrar también el menú de lista
                                            selectedLibroId = null

                                        } catch (e: Exception) {
                                            // En caso de error, mostrar mensaje de error
                                            Toast.makeText(
                                                context,
                                                "Error al eliminar el libro",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                            ) {
                                Text("Eliminar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteDialog = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }


                if (showList) {
                    AlertDialog(
                        onDismissRequest = { showList = false },
                        title = { Text(text = "Listar", fontSize = 20.sp) },
                        text = {
                            LazyColumn(
                                modifier = Modifier
                                    .heightIn(max = 300.dp)
                            ) {
                                item {
                                    Button(
                                        onClick = {
                                            scope.launch {
                                                libros = withContext(Dispatchers.IO) {
                                                    libroRepository.getAllLibros()
                                                }
                                                showListDialog2 =
                                                    true // Mostrar la ventana emergente
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth() // Asigna el mismo peso a este botón
                                            .padding(8.dp), // Espacio alrededor
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF4CAF50), // Fondo verde
                                            contentColor = Color.White          // Texto en blanco
                                        )
                                    ) {
                                        Text(text = "Listar")
                                    }
                                }

                                item {
                                    Spacer(Modifier.height(8.dp))

                                    Button(
                                        onClick = {
                                            scope.launch {
                                                libros = withContext(Dispatchers.IO) {
                                                    libroRepository.getAllLibros()
                                                }
                                                showListLibrosById =
                                                    true // Mostrar la ventana emergente
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()// Asigna el mismo peso a este botón
                                            .padding(8.dp), // Espacio alrededor
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF91C34A), // Fondo verde
                                            contentColor = Color.White          // Texto en blanco
                                        )
                                    ) {
                                        Text(text = "Listar por Autor")
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                // Limpiar campos cuando se confirme el cierre del diálogo
                                titulo = ""
                                genero = ""
                                autor_id = ""
                                showList = false
                                selectedAutor = null
                                null.also { selectedLibroId = it }
                            }) {
                                Text("Cerrar")
                            }
                        }
                    )
                }
            }
        }
    }
}
