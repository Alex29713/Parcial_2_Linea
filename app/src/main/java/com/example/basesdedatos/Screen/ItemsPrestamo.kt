package com.example.basesdedatos.Screen

import android.R.attr
import android.app.DatePickerDialog
import android.graphics.fonts.FontStyle
import android.os.Build
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.basesdedatos.Model.Autores
import com.example.basesdedatos.Model.Miembros
import com.example.basesdedatos.Model.Libros
import com.example.basesdedatos.Model.Prestamos
import com.example.basesdedatos.Model.RelacionLibrosPrestamos
import com.example.basesdedatos.Repository.AutorRepository
import com.example.basesdedatos.Repository.LibroRepository
import com.example.basesdedatos.Repository.MiembroRepository
import com.example.basesdedatos.Repository.PrestamoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

import java.util.Calendar
import androidx.compose.ui.text.style.TextOverflow
import android.R.attr.singleLine
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MenuDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.basesdedatos.R


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrestamosApp(libroRepository: LibroRepository, miembroRepository: MiembroRepository, prestamoRepository: PrestamoRepository, navController: NavController) {

    var libro_id by remember { mutableStateOf("") }
    var miembro_id by remember { mutableStateOf("") }
    var fecha_prestamo by remember { mutableStateOf("") }
    var fecha_devolucion by remember { mutableStateOf("") }

    var selectedPrestamoId by remember { mutableStateOf<Int?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var expandedPrestamo by remember { mutableStateOf(false) }
    var expandedLibro by remember { mutableStateOf(false) }

    var prestamos by remember { mutableStateOf(listOf<Prestamos>()) }
    var prestamosbyLibro by remember { mutableStateOf(listOf<RelacionLibrosPrestamos>()) }
    var libros by remember { mutableStateOf(listOf<Libros>()) }
    var autores by remember { mutableStateOf(listOf<Autores>()) }
    var miembros by remember { mutableStateOf(listOf<Miembros>()) }

    var selectedMiembroId by remember { mutableStateOf<Miembros?>(null) }
    var selectedLibroId by remember { mutableStateOf<Libros?>(null) }


    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showListDialog by remember { mutableStateOf(false) } // Controla el diálogo de lista
    var showListDialog2 by remember { mutableStateOf(false) } // Controla el diálogo de lista
    var showList by remember { mutableStateOf(false) } // Controla el diálogo de lista
    var showListPrestamosByLibro by remember { mutableStateOf(false) } // Controla el diálogo de lista
    var showListPrestamosByMiembro by remember { mutableStateOf(false) } // Controla el diálogo de lista


    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Crear el DatePickerDialog
    var datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            if(dayOfMonth.toString().length == 1){
                fecha_prestamo = "0$dayOfMonth/${month + 1}/$year"
            }else{
                fecha_prestamo = "$dayOfMonth/${month + 1}/$year"
            }
            // Actualizamos el estado cuando se selecciona la fecha
        },
        year, month, day
    )

    var datePickerDialog2 = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            // Actualizamos el estado cuando se selecciona la fecha
            if(dayOfMonth.toString().length == 1){
                fecha_devolucion = "0$dayOfMonth/${month + 1}/$year"
            }else {
                fecha_devolucion = "$dayOfMonth/${month + 1}/$year"
            }
        },
        year, month, day
    )


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
             // Contenido de la tarjeta
             Text(
                 text = "Préstamos",
                 fontWeight = FontWeight.Bold,
                 style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                 modifier = Modifier
                     .padding(16.dp) // Aumenta el padding para mejorar el aspecto
             )
        }


        item{
            LaunchedEffect(Unit) {
                libros = withContext(Dispatchers.IO) {
                    libroRepository.getAllLibros() // Método para obtener todas las personas
                }
//                Log.d("Debug", "Préstamos por libro cargados: ${prestamosbyLibro.size}")
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
                    .clickable { expandedLibro = !expandedLibro },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(8.dp),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,  // Puedes cambiar el ícono aquí
                        contentDescription = "Icono de Libro",
                        modifier = Modifier.size(24.dp)  // Tamaño del ícono
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = if (selectedLibroId != null) {
                            "Libro : ${selectedLibroId!!.titulo} seleccionado"
                        } else {
                            "Seleccionar Libro"
                        }
                    )
                }
                    DropdownMenu(
                        expanded = expandedLibro,
                        onDismissRequest = { expandedLibro = false },
                        modifier = Modifier.heightIn(max = 200.dp).align(Alignment.CenterHorizontally)
                    ) {
                        libros.forEach { libro ->

                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.PlayArrow,  // Cambia este icono si deseas otro
                                            contentDescription = "Icono de libro",
                                            modifier = Modifier.size(16.dp)  // Puedes ajustar el tamaño del icono
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))  // Espacio entre icono y texto
                                        Column {
                                            // Muestra el título del libro
                                            Text(
                                                text = "Título: ${libro.titulo}",
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodyMedium  // Ajusta el estilo si es necesario
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))  // Espacio entre líneas

                                            // Muestra el género del libro
                                            Text(
                                                text = "Género: ${libro.genero}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                },
                                onClick = {

                                    scope.launch {
                                        libros = withContext(Dispatchers.IO) {
                                            libroRepository.getAllLibros()
                                        }
                                    }

                                    selectedLibroId = libros.find { it.libro_id == libro.libro_id }
                                    libro_id = libro.libro_id.toString()
                                    expandedLibro = false

                                }, modifier = Modifier.padding(8.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }

        item{
            LaunchedEffect(Unit) {
                miembros = withContext(Dispatchers.IO) {
                    miembroRepository.getAllMiembros() // Método para obtener todas las personas
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
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(8.dp),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,  // Puedes cambiar el ícono aquí
                        contentDescription = "Icono de Libro",
                        modifier = Modifier.size(24.dp)  // Tamaño del ícono
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = if (selectedMiembroId != null) {
                            "Miembro: ${selectedMiembroId!!.nombre} seleccionado"
                        } else {
                            "Seleccionar Miembro"
                        }
                    )
                }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .heightIn(max = 200.dp)
                            .align(Alignment.CenterHorizontally)
                            .background(Color.White)
                    ) {
                        miembros.forEach { miembro ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Person,  // Cambia este icono si deseas otro
                                            contentDescription = "Icono de miembro",
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = "Nombre: ${miembro.nombre} ${miembro.apellido}",  // Muestra el nombre y apellido del miembro
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Fecha de Inscripción: ${miembro.fecha_inscripcion}",  // Muestra la fecha de inscripción
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                },
                                onClick = {

                                    scope.launch {
                                        miembros = withContext(Dispatchers.IO) {
                                            miembroRepository.getAllMiembros()
                                        }
                                    }

                                    selectedMiembroId = miembro
                                    miembro_id = miembro.miembro_id.toString()

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween // Espacio entre los campos
                ) {

                    OutlinedTextField(
                        value = fecha_prestamo,
                        onValueChange = { fecha_prestamo = it },
                        label = { Text("Date Prestamo") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White
                        ),
                        enabled = false,  // Evitar que el usuario escriba manualmente
                        modifier = Modifier
                            .weight(1f)  // Hace que ambos campos compartan el espacio disponible
                            .clickable {
                                // Abrir el DatePickerDialog cuando se haga clic en el campo de texto
                                datePickerDialog.show()
                            },
                        trailingIcon = {
                            IconButton(onClick = { datePickerDialog.show() }) {
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = "Seleccionar fecha"
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = fecha_devolucion,
                        onValueChange = { fecha_devolucion = it },
                        label = { Text("Date Devolución") },
                        enabled = false,  // Evitar que el usuario escriba manualmente
                        modifier = Modifier
                            .weight(1f)  // Hace que ambos campos compartan el espacio disponible
                            .clickable {
                                // Abrir el DatePickerDialog cuando se haga clic en el campo de texto
                                datePickerDialog2.show()
                            },
                        trailingIcon = {
                            IconButton(onClick = { datePickerDialog2.show() }) {
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = "Seleccionar fecha"
                                )
                            }
                        }
                    )
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
                        if (selectedLibroId != null && selectedMiembroId != null && fecha_prestamo.isNotEmpty() && fecha_devolucion.isNotEmpty()) {


                                val fechaPrestamoLocalDate = LocalDate.parse(fecha_prestamo, formatter)
                                val fechaDevolucionLocalDate = LocalDate.parse(fecha_devolucion, formatter)

                                // Comparar las fechas
                                if (fechaPrestamoLocalDate.isBefore(fechaDevolucionLocalDate)) {
                                    // Fechas válidas, puedes proceder con el registro
                                    val prestamo = Prestamos(
                                        libro_id = libro_id.toInt(),
                                        miembro_id = miembro_id.toInt(),
                                        fecha_prestamo = fecha_prestamo,
                                        fecha_devolucion = fecha_devolucion
                                    )
                                    scope.launch {
                                        withContext(Dispatchers.IO) {
                                            prestamoRepository.insert(prestamo)
                                        }
                                    }
                                    Toast.makeText(
                                        context,
                                        "Prestamos Registrado",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    fecha_prestamo = ""
                                    fecha_devolucion = ""
                                    selectedLibroId = null
                                    selectedMiembroId = null

                                } else {
                                    // La fecha de devolución es anterior o igual a la fecha de préstamo
                                    Toast.makeText(
                                        context,
                                        "La fecha de devolución debe ser mayor a la fecha de préstamo",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                        else {
                            Toast.makeText(context, "Rellene todos los campos ", Toast.LENGTH_SHORT).show()
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
                            showList = true // Mostrar la ventana emergente
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
                            prestamos = withContext(Dispatchers.IO) {
                                prestamoRepository.getAllPrestamos()
                            }

                            miembros = withContext(Dispatchers.IO) {
                                miembroRepository.getAllMiembros()
                            }
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
                    title = { Text(text = "Prestamos Registrados") },
                    text = {
                        LazyColumn(
                            modifier = Modifier
                                .heightIn(max = 300.dp)
                        ) {
                            item {
                                prestamos.forEach { prestamo ->
                                    Card(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth()
                                            .clickable { expanded = !expanded },
                                        colors = CardDefaults.cardColors(),
                                        elevation = CardDefaults.cardElevation(8.dp),
                                        onClick = {

                                            selectedPrestamoId = prestamo.prestamo_id
                                            selectedLibroId = libros.find { it.libro_id == prestamo.libro_id }
                                            selectedMiembroId = miembros.find { it.miembro_id == prestamo.miembro_id }

                                            libro_id = prestamo.libro_id.toString()
                                            miembro_id = prestamo.miembro_id.toString()
                                            fecha_prestamo = prestamo.fecha_prestamo

                                            fecha_devolucion = prestamo.fecha_devolucion
                                            showListDialog = true // Mostrar la ventana emergente
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
                                                        text = "Libro: ",
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(text = "${libros.find { it.libro_id == prestamo.libro_id }?.titulo}")
                                                }

                                                Row {
                                                    Text(
                                                        text = "Miembro: ",
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text( text = "${miembros.find { it.miembro_id == prestamo.miembro_id }?.nombre}")
                                                }

                                                Row {
                                                    Text(
                                                        text = "Prestamo: ",
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(text = prestamo.fecha_prestamo)
                                                }

                                                Row {
                                                    Text(
                                                        text = "Devolución: ",
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(text = prestamo.fecha_devolucion)
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
                            libro_id = ""
                            miembro_id = ""
                            fecha_prestamo = ""
                            fecha_devolucion = ""
                            showListDialog2 = false
                            selectedMiembroId = null
                            null.also { selectedLibroId = it }
                        }) {
                            Text("Cerrar")
                        }
                    }
                )
            }

        }

        item {
            if (showListPrestamosByLibro) {
                AlertDialog(
                    onDismissRequest = { showListPrestamosByLibro = false },
                    title = { Text(text = "Prestamos Realizados Por Libro") },
                    text = {
                        LazyColumn(
                            modifier = Modifier
                                .heightIn(max = 300.dp)
                        ) {
                            item{
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
                                        containerColor = Color(0xFF009688),
                                    ),
                                    elevation = CardDefaults.cardElevation(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        Text(
                                            text = if (selectedLibroId != null) {
                                                "Libro ID: ${selectedLibroId!!.libro_id} seleccionado"
                                            } else {
                                                "Seleccionar Libro"
                                            }
                                        )
                                    }
                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false },
                                            modifier = Modifier.heightIn(max = 200.dp).align(Alignment.CenterHorizontally)
                                        ){
                                            libros.forEach { libro ->
                                                DropdownMenuItem(
                                                    text = {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(
                                                                Icons.Default.PlayArrow,  // Icono representativo para el libro
                                                                contentDescription = "Icono de libro",
                                                                modifier = Modifier.size(12.dp)
                                                            )
                                                            Spacer(modifier = Modifier.width(8.dp))
                                                            Column {
                                                                Text(
                                                                    text = "Título: ${libro.titulo}",  // Mostrar el título del libro
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                                Spacer(modifier = Modifier.height(4.dp))
                                                                Text(
                                                                    text = "Género: ${libro.genero}",  // Mostrar el género del libro
                                                                    style = MaterialTheme.typography.bodySmall
                                                                )
                                                            }
                                                        }
                                                    },
                                                    onClick = {
                                                        selectedLibroId = libro
                                                        expanded = false

                                                        // Filtrar los préstamos por el ID del libro seleccionado
                                                        scope.launch {
                                                            withContext(Dispatchers.IO) {
                                                                prestamosbyLibro = prestamoRepository.getPrestamosByLibrosId(selectedLibroId!!.libro_id)
                                                            }
                                                        }

                                                        Log.d("Debug", "prestamosbyLibro size: ${prestamosbyLibro.size}")
                                                        println( "prestamosbyLibro size: ${prestamosbyLibro.size}")

                                                    }, modifier = Modifier.padding(8.dp)
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                prestamosbyLibro.forEach { relacionLibroPrestamo ->
                                    relacionLibroPrestamo.prestamos.forEach { prestamo -> // Iterar sobre los préstamos asociados
                                        Card(
                                            modifier = Modifier
                                                .padding(8.dp),
                                            colors = CardDefaults.cardColors(),
                                            elevation = CardDefaults.cardElevation(8.dp),
                                            onClick = {
                                                selectedPrestamoId = prestamo.prestamo_id // Asigna el ID del préstamo seleccionado
                                                selectedLibroId = relacionLibroPrestamo.libro // Obtiene el libro de la relación
                                                selectedMiembroId = miembros.find { it.miembro_id == prestamo.miembro_id }

                                                // Asigna los valores para mostrar en la ventana emergente
                                                libro_id = relacionLibroPrestamo.libro.libro_id.toString()
                                                miembro_id = prestamo.miembro_id.toString()
                                                fecha_prestamo = prestamo.fecha_prestamo
                                                fecha_devolucion = prestamo.fecha_devolucion

                                                showListDialog = true // Mostrar la ventana emergente
                                            }
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(16.dp),
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = "Libro: ${relacionLibroPrestamo.libro.titulo}, " +
                                                            "Miembro: ${miembros.find { it.miembro_id == prestamo.miembro_id }?.nombre}, " +
                                                            "Fecha Préstamo: ${prestamo.fecha_prestamo}, " +
                                                            "Fecha Devolución: ${prestamo.fecha_devolucion}"
                                                )
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

                            fecha_prestamo = ""
                            fecha_devolucion = ""
                            selectedMiembroId = null
                            showListPrestamosByLibro = false
                            selectedLibroId = null
                        }) {
                            Text("Cerrar")
                        }
                    }
                )}

        }

        item {
            if (showListPrestamosByMiembro) {
                AlertDialog(
                    onDismissRequest = { showListPrestamosByMiembro = false },
                    title = { Text(text = "Prestamos Realizados A Miembros") },
                    text = {
                        LazyColumn(
                            modifier = Modifier
                                .heightIn(max = 300.dp)
                        ) {
                            item{
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
                                        containerColor = Color(0xFF009688),
                                    ),
                                    elevation = CardDefaults.cardElevation(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        Text(
                                            text = if (selectedMiembroId != null) {
                                                "Miembro ID: ${selectedMiembroId!!.miembro_id} seleccionado"
                                            } else {
                                                "Seleccionar Miembro"
                                            }
                                        )
                                    }
                                        DropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false },
                                            modifier = Modifier.heightIn(max = 200.dp).align(Alignment.CenterHorizontally)
                                        ){
                                            miembros.forEach { miembro ->

                                                DropdownMenuItem(
                                                    text = {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(
                                                                Icons.Default.Person,  // Icono representativo para el miembro
                                                                contentDescription = "Icono de miembro",
                                                                modifier = Modifier.size(12.dp)
                                                            )
                                                            Spacer(modifier = Modifier.width(8.dp))
                                                            Column {
                                                                Text(
                                                                    text = "Nombre: ${miembro.nombre} ${miembro.apellido}",  // Mostrar el nombre completo del miembro
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                                Spacer(modifier = Modifier.height(4.dp))
                                                                Text(
                                                                    text = "Fecha de Inscripción: ${miembro.fecha_inscripcion}",  // Mostrar la fecha de inscripción
                                                                    style = MaterialTheme.typography.bodySmall
                                                                )
                                                            }
                                                        }
                                                    },
                                                    onClick = {
                                                        selectedMiembroId = miembro
                                                        expanded = false

                                                        // Filtrar los préstamos por el ID del libro seleccionado
                                                        scope.launch {
                                                            withContext(Dispatchers.IO) {
                                                                prestamos = prestamoRepository.getPrestamosByMiembroId(selectedMiembroId!!.miembro_id)
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
                                prestamos.forEach { prestamo ->
                                    Card(
                                        modifier = Modifier
                                            .padding(8.dp),
                                        colors = CardDefaults.cardColors(),
                                        elevation = CardDefaults.cardElevation(8.dp),
                                        onClick = {
                                            selectedPrestamoId = prestamo.prestamo_id
                                            selectedLibroId = libros.find { it.libro_id == prestamo.libro_id }
                                            selectedMiembroId = miembros.find { it.miembro_id == prestamo.miembro_id }

                                            libro_id = prestamo.libro_id.toString()
                                            miembro_id = prestamo.miembro_id.toString()
                                            fecha_prestamo = prestamo.fecha_prestamo

                                            fecha_devolucion = prestamo.fecha_devolucion
                                            showListDialog = true // Mostrar la ventana emergente
                                        }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = "Libro: ${libros.find { it.libro_id == prestamo.libro_id }?.titulo}, Miembro: ${miembros.find { it.miembro_id == prestamo.miembro_id }?.nombre}, Fecha Préstamo: ${prestamo.fecha_prestamo}, Fecha Devolución: ${prestamo.fecha_devolucion}"
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

                            fecha_prestamo = ""
                            fecha_devolucion = ""
                            selectedMiembroId = null
                            showListPrestamosByMiembro = false
                            selectedMiembroId = null
                        }) {
                            Text("Cerrar")
                        }
                    }
                )}
        }


        item {
            // Ventana emergente con lista de usuarios, editar y eliminar
            if (showListDialog) {
                AlertDialog(
                    onDismissRequest = { showListDialog = false },
                    title = { Text(text = "Prestamos Registrados") },
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
                                            .clickable { expandedPrestamo = !expandedPrestamo },
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFF009688)
                                        ),
                                        elevation = CardDefaults.cardElevation(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            horizontalArrangement = Arrangement.Center,
                                        ) {
                                            Text(
                                                text = if (selectedPrestamoId != null) {
                                                    "Prestamo ID: ${selectedPrestamoId} seleccionado"
                                                } else {
                                                    "Seleccionar Prestamo"
                                                }
                                            )
                                        }

                                        // DropdownMenu centrado con respecto a la Card
                                        DropdownMenu(
                                            expanded = expandedPrestamo,
                                            onDismissRequest = { expandedPrestamo = false },
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally) // Alineación horizontal en el centro
                                                .heightIn(max = 200.dp)
                                                .background((Color(0xFF009688))),
                                        ) {
                                            prestamos.forEach { prestamo ->

                                                val libroTitulo =
                                                    libros.find { it.libro_id == prestamo.libro_id }?.titulo
                                                        ?: "Desconocido"
                                                val miembroNombre =
                                                    miembros.find { it.miembro_id == prestamo.miembro_id }?.nombre
                                                        ?: "Desconocido"

                                                DropdownMenuItem(
                                                    text = {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(
                                                                Icons.Default.KeyboardArrowRight,
                                                                contentDescription = "Icono de libro",
                                                                modifier = Modifier.size(12.dp)
                                                            )
                                                            Spacer(modifier = Modifier.width(8.dp))
                                                            Column {
                                                                Text(
                                                                    "Libro: $libroTitulo",
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                                Spacer(modifier = Modifier.height(4.dp))
                                                                Text(
                                                                    "Miembro: $miembroNombre",
                                                                    style = MaterialTheme.typography.bodySmall
                                                                )
                                                                Spacer(modifier = Modifier.height(4.dp))
                                                                Text(
                                                                    "Fecha Préstamo: ${prestamo.fecha_prestamo}",
                                                                    style = MaterialTheme.typography.bodySmall
                                                                )
                                                                Spacer(modifier = Modifier.height(4.dp))
                                                                Text(
                                                                    "Fecha Devolución: ${prestamo.fecha_devolucion}",
                                                                    style = MaterialTheme.typography.bodySmall
                                                                )
                                                            }
                                                        }
                                                    },
                                                    onClick = {
                                                        selectedPrestamoId = prestamo.prestamo_id
                                                        selectedLibroId =
                                                            libros.find { it.libro_id == prestamo.libro_id }
                                                        selectedMiembroId =
                                                            miembros.find { it.miembro_id == prestamo.miembro_id }
                                                        libro_id = prestamo.libro_id.toString()
                                                        miembro_id = prestamo.miembro_id.toString()
                                                        fecha_prestamo = prestamo.fecha_prestamo
                                                        fecha_devolucion = prestamo.fecha_devolucion
                                                        expandedPrestamo = false
                                                    },
                                                    modifier = Modifier.padding(8.dp)
                                                )

                                                Spacer(modifier = Modifier.height(8.dp)) // Espacio entre los elementos
                                            }
                                        }
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
                                        .clickable { expandedLibro = !expandedLibro },
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),
                                    elevation = CardDefaults.cardElevation(8.dp),
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = if (selectedLibroId != null) {
                                                "Libro : ${selectedLibroId!!.titulo} seleccionado"
                                            } else {
                                                "Seleccionar Libro"
                                            }
                                        )
                                    }
                                        DropdownMenu(
                                            expanded = expandedLibro,
                                            onDismissRequest = { expandedLibro = false },
                                            modifier = Modifier
                                                .heightIn(max = 200.dp)
                                                .align(Alignment.CenterHorizontally)
                                                .background(Color.White)
                                        ) {
                                            libros.forEach { libro ->
                                                DropdownMenuItem(
                                                    text = {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(
                                                                Icons.Default.KeyboardArrowRight,
                                                                contentDescription = "Icono de libro",
                                                                modifier = Modifier.size(12.dp)
                                                            )
                                                            Spacer(modifier = Modifier.width(8.dp))
                                                            Column {
                                                                Text(
                                                                    text = "Título: ${libro.titulo}",  // Aquí se muestra el título del libro
                                                                    fontWeight = FontWeight.Bold
                                                                )
                                                                Spacer(modifier = Modifier.height(4.dp))
                                                                Text(
                                                                    text = "Género: ${libro.genero}",  // Aquí se muestra el género del libro
                                                                    style = MaterialTheme.typography.bodySmall
                                                                )
                                                            }
                                                        }
                                                    },
                                                    onClick = {

                                                        scope.launch {
                                                            libros = withContext(Dispatchers.IO) {
                                                                libroRepository.getAllLibros()
                                                            }
                                                        }
                                                        selectedLibroId = libros.find { it.libro_id == libro.libro_id }
                                                        libro_id = libro.libro_id.toString()
                                                        expandedLibro = false

                                                    },
                                                    modifier = Modifier.padding(8.dp)
                                                )

                                                Spacer(modifier = Modifier.height(4.dp))
                                            }
                                        }
                                    }
                                }
                            }

                            item{
                                LaunchedEffect(Unit) {
                                    miembros = withContext(Dispatchers.IO) {
                                        miembroRepository.getAllMiembros() // Método para obtener todas las personas
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
                                            containerColor = Color(0xFF4CAF50)
                                        ),
                                        elevation = CardDefaults.cardElevation(8.dp),
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            horizontalArrangement = Arrangement.Center,
                                        ) {
                                            Text(
                                                text = if (selectedMiembroId != null) {
                                                    "Miembro: ${selectedMiembroId!!.nombre} seleccionado"
                                                } else {
                                                    "Seleccionar Miembro"
                                                }
                                            )
                                        }
                                            DropdownMenu(
                                                expanded = expanded,
                                                onDismissRequest = { expanded = false },
                                                modifier = Modifier.heightIn(max = 200.dp).align(Alignment.CenterHorizontally)
                                                    .background(Color(0xFF4CAF50))
                                            ) {
                                                miembros.forEach { miembro ->
                                                    DropdownMenuItem(
                                                        text = {
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Icon(
                                                                    Icons.Default.Person,  // Cambia este icono si deseas otro
                                                                    contentDescription = "Icono de miembro",
                                                                    modifier = Modifier.size(12.dp)
                                                                )
                                                                Spacer(modifier = Modifier.width(8.dp))
                                                                Column {
                                                                    Text(
                                                                        text = "Nombre: ${miembro.nombre} ${miembro.apellido}",  // Aquí se muestra el nombre y apellido del miembro
                                                                        fontWeight = FontWeight.Bold
                                                                    )
                                                                    Spacer(
                                                                        modifier = Modifier.height(
                                                                            4.dp
                                                                        )
                                                                    )
                                                                    Text(
                                                                        text = "Fecha de Inscripción: ${miembro.fecha_inscripcion}",  // Aquí se muestra la fecha de inscripción
                                                                        style = MaterialTheme.typography.bodySmall
                                                                    )
                                                                }
                                                            }
                                                        },
                                                        onClick = {

                                                            scope.launch {
                                                                miembros =
                                                                    withContext(Dispatchers.IO) {
                                                                        miembroRepository.getAllMiembros()
                                                                    }
                                                            }

                                                            selectedMiembroId = miembro
                                                            miembro_id =
                                                                miembro.miembro_id.toString()

                                                            expanded = false
                                                        },
                                                        modifier = Modifier.padding(8.dp),
                                                        colors = MenuDefaults.itemColors(
                                                            textColor = Color.Black,   // Cambiar el color del texto
                                                            disabledTextColor = Color.Gray, // Cambiar el color del texto deshabilitado
                                                            leadingIconColor = Color.Gray // Cambiar el color del ícono
                                                        )                                                  )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                }
                                            }
                                        }
                                    }
                                }

                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween // Espacio entre los campos
                                ) {

                                    OutlinedTextField(
                                        value = fecha_prestamo,
                                        onValueChange = { fecha_prestamo = it },
                                        label = {
                                            Text(
                                                "Prestamo",
                                                style = MaterialTheme.typography.bodySmall // Ajustar tamaño de la etiqueta
                                            )
                                        },
                                        enabled = false,  // Evitar que el usuario escriba manualmente
                                        modifier = Modifier
                                            .weight(1f)  // Hace que ambos campos compartan el espacio disponible
                                            .clickable {
                                                // Abrir el DatePickerDialog cuando se haga clic en el campo de texto
                                                datePickerDialog.show()
                                            },
                                        trailingIcon = {
                                            IconButton(onClick = { datePickerDialog.show() }) {
                                                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                                            }
                                        },
                                        singleLine = true,  // Mantener el texto en una sola línea
                                        maxLines = 1,       // Limitar a una línea máxima
                                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)  // Reducir el tamaño de la fuente
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    OutlinedTextField(
                                        value = fecha_devolucion,
                                        onValueChange = { fecha_devolucion = it },
                                        label = {
                                            Text(
                                                "Devolución",
                                                style = MaterialTheme.typography.bodySmall // Ajustar tamaño de la etiqueta
                                            )
                                        },
                                        enabled = false,  // Evitar que el usuario escriba manualmente
                                        modifier = Modifier
                                            .weight(1f)  // Hace que ambos campos compartan el espacio disponible
                                            .clickable {
                                                // Abrir el DatePickerDialog cuando se haga clic en el campo de texto
                                                datePickerDialog2.show()
                                            },
                                        trailingIcon = {
                                            IconButton(onClick = { datePickerDialog2.show() }) {
                                                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                                            }
                                        },
                                        singleLine = true,  // Mantener el texto en una sola línea
                                        maxLines = 1,       // Limitar a una línea máxima
                                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)  // Reducir el tamaño de la fuente
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }


                            item {
                                // Botones de Editar y Eliminar
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    TextButton(onClick = {
                                        if (selectedPrestamoId == null) {
                                            // Mostrar mensaje de error si no se ha seleccionado un usuario

                                            Toast.makeText(
                                                context,
                                                "Por favor, seleccione un prestamo primero",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            if (selectedLibroId != null && selectedMiembroId != null && fecha_prestamo.isNotEmpty() && fecha_devolucion.isNotEmpty())
                                            {
                                                showEditDialog = true
                                            }else{
                                                Toast.makeText(context, "Rellene todos los campos", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }) {
                                        Text("Actualizar")
                                    }

                                    TextButton(onClick = {
                                        if (selectedPrestamoId == null) {
                                            // Mostrar mensaje de error si no se ha seleccionado un usuario
                                            Toast.makeText(
                                                context,
                                                "Por favor, seleccione un prestamo primero",
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
                            selectedPrestamoId = null
                            selectedLibroId = null
                            selectedMiembroId = null
                            fecha_prestamo = ""
                            fecha_devolucion = ""
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
                    text = { Text(text = "¿Está seguro de que desea actualizar este prestamo?") },
                    confirmButton = {
                        TextButton(onClick = {
                            selectedPrestamoId?.let { prestamoId ->
                                scope.launch {
                                    try {
                                            // Convertir las fechas de String a LocalDate
                                            val fechaPrestamoLocalDate = LocalDate.parse(fecha_prestamo, formatter)
                                            val fechaDevolucionLocalDate = LocalDate.parse(fecha_devolucion, formatter)

                                            // Comparar las fechas
                                            if (fechaPrestamoLocalDate.isBefore(fechaDevolucionLocalDate)) {

                                                withContext(Dispatchers.IO) {
                                                    prestamoRepository.updateByIdPrestamo(
                                                        prestamoId,
                                                        libro_id.toInt(),
                                                        miembro_id.toInt(),
                                                        fecha_prestamo,
                                                        fecha_devolucion
                                                    )
                                                }
                                                // Refrescar la lista de usuarios después de la actualización
                                                prestamos = withContext(Dispatchers.IO) {
                                                    prestamoRepository.getAllPrestamos()
                                                }

                                                // Mostrar mensaje de éxito y limpiar los campos
                                                Toast.makeText(context, "Prestamo Actualizado", Toast.LENGTH_SHORT).show()

                                                // Limpiar los campos
                                                selectedMiembroId = null
                                                selectedLibroId = null
                                                fecha_prestamo = ""
                                                fecha_devolucion = ""

                                                showEditDialog = false
                                                showListDialog = false
                                                selectedPrestamoId = null
                                            } else {
                                                // La fecha de devolución es anterior o igual a la fecha de préstamo
                                                Toast.makeText(context, "La fecha de devolución debe ser mayor a la fecha de préstamo", Toast.LENGTH_SHORT).show()
                                            }

                                    } catch (e: Exception) {
                                        // En caso de error, mostrar mensaje de error
                                        Toast.makeText(context, "Error al actualizar el prestamo", Toast.LENGTH_SHORT).show()
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
                    text = { Text(text = "¿Está seguro de que desea eliminar este prestamo?") },
                    confirmButton = {
                        TextButton(onClick =
                        {
                            selectedPrestamoId?.let { prestamoId ->
                                scope.launch {
                                    try {
                                        withContext(Dispatchers.IO) {
                                            prestamoRepository.deleteByIdPrestamo(prestamoId)
                                        }

                                        // Mostrar mensaje de éxito y limpiar los campos
                                        Toast.makeText(
                                            context,
                                            "Prestamo Eliminado",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        selectedPrestamoId = null
                                        selectedLibroId = null
                                        selectedMiembroId = null
                                        fecha_prestamo = ""
                                        fecha_devolucion = ""

                                        showDeleteDialog = false
                                        showListDialog = false // Cerrar también el menú de lista
                                        selectedLibroId = null

                                    } catch (e: Exception) {
                                        // En caso de error, mostrar mensaje de error
                                        Toast.makeText(
                                            context,
                                            "Error al eliminar el prestamo",
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
                             item{
                                 Button(
                                     onClick = {
                                         scope.launch {
                                             prestamos = withContext(Dispatchers.IO) {
                                                 prestamoRepository.getAllPrestamos()
                                             }

                                             miembros = withContext(Dispatchers.IO) {
                                                 miembroRepository.getAllMiembros()
                                             }
                                             libros = withContext(Dispatchers.IO) {
                                                 libroRepository.getAllLibros()
                                             }
                                             showListDialog2 = true // Mostrar la ventana emergente
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

                             item{

                                 Spacer(modifier = Modifier.height(8.dp))

                                 Button(
                                     onClick = {
                                         scope.launch {
                                             libros = withContext(Dispatchers.IO) {
                                                 libroRepository.getAllLibros()
                                             }
                                             prestamos = withContext(Dispatchers.IO) {
                                                 prestamoRepository.getAllPrestamos()
                                             }
                                             miembros = withContext(Dispatchers.IO) {
                                                 miembroRepository.getAllMiembros()
                                             }
                                             showListPrestamosByLibro = true // Mostrar la ventana emergente
                                         }
                                     },
                                     modifier = Modifier
                                         .fillMaxWidth()// Asigna el mismo peso a este botón
                                         .padding(8.dp), // Espacio alrededor
                                     colors = ButtonDefaults.buttonColors(
                                         containerColor = Color(0xFF91C34A), // Fondo verde
                                         contentColor = Color.White         // Texto en blanco
                                     )
                                 ) {
                                     Text(text = "Listar Prestamos por Libro")
                                 }
                             }

                             item{

                                 Spacer(modifier = Modifier.height(8.dp))

                                 Button(
                                     onClick = {
                                         scope.launch {
                                             libros = withContext(Dispatchers.IO) {
                                                 libroRepository.getAllLibros()
                                             }
                                             prestamos = withContext(Dispatchers.IO) {
                                                 prestamoRepository.getAllPrestamos()
                                             }
                                             miembros = withContext(Dispatchers.IO) {
                                                 miembroRepository.getAllMiembros()
                                             }
                                             showListPrestamosByMiembro = true // Mostrar la ventana emergente
                                         }
                                     },
                                     modifier = Modifier
                                         .fillMaxWidth()// Asigna el mismo peso a este botón
                                         .padding(8.dp), // Espacio alrededor
                                     colors = ButtonDefaults.buttonColors(
                                         containerColor = Color(0xFF00811C), // Fondo verde
                                         contentColor = Color.White       // Texto en blanco
                                     )
                                 ) {
                                     Text(text = "Listar Prestamos Segun Miembro")
                                 }
                             }
                         }
                     },
                     confirmButton = {
                         TextButton(onClick = {
                             // Limpiar campos cuando se confirme el cierre del diálogo
                             selectedLibroId = null
                             selectedMiembroId = null
                             fecha_prestamo = ""
                             fecha_devolucion = ""

                             showList = false
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

