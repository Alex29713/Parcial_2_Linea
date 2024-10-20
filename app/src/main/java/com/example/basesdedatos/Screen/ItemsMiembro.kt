package com.example.basesdedatos.Screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.basesdedatos.Model.Miembros
import com.example.basesdedatos.R
import com.example.basesdedatos.Repository.MiembroRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiembroApp(miembroRepository: MiembroRepository, navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var fecha_inscripcion by remember { mutableStateOf("") }

    var selectedMiembroId by remember { mutableStateOf<Int?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var miembros by remember { mutableStateOf(listOf<Miembros>()) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showListDialog by remember { mutableStateOf(false) } // Controla el diálogo de lista
    var showListDialog2 by remember { mutableStateOf(false) } // Controla el diálogo de lista

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Crear el DatePickerDialog
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            // Actualizamos el estado cuando se selecciona la fecha
            fecha_inscripcion = "$dayOfMonth/${month + 1}/$year"
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
                Text(
                    text = "Miembros",
                    fontWeight = FontWeight.Bold,
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            item {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person, // Ícono de persona
                            contentDescription = "Icono de nombre",
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
                TextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = { Text("Apellido") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AccountBox, // Ícono de persona
                            contentDescription = "Icono de apellido",
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
                OutlinedTextField(
                    value = fecha_inscripcion,
                    onValueChange = { fecha_inscripcion = it },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    ),
                    label = { Text("Fecha de Inscripción") },
                    trailingIcon = {
                        IconButton(onClick = { datePickerDialog.show() }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                        }
                    },
                    enabled = false,  // Evitar que el usuario escriba manualmente
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Abrir el DatePickerDialog cuando se haga clic en el campo de texto
                            datePickerDialog.show()
                        }
                )
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
                            val miembro = Miembros(
                                nombre = nombre,
                                apellido = apellido,
                                fecha_inscripcion = fecha_inscripcion,
                            )
                            if (nombre.isNotEmpty() && apellido.isNotEmpty() && fecha_inscripcion.isNotEmpty()) {
                                scope.launch {
                                    withContext(Dispatchers.IO) {
                                        miembroRepository.insert(miembro)
                                    }
                                }
                                nombre = ""
                                apellido = ""
                                fecha_inscripcion = ""
                                Toast.makeText(context, "Miembro Registrado", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Rellene todos los campos",
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
                                miembros = withContext(Dispatchers.IO) {
                                    miembroRepository.getAllMiembros()
                                }
                                showListDialog2 = true // Mostrar la ventana emergente
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
                                miembros = withContext(Dispatchers.IO) {
                                    miembroRepository.getAllMiembros()
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
                        title = { Text(text = "Miembros Registrados") },
                        text = {
                            LazyColumn(
                                modifier = Modifier
                                    .heightIn(max = 300.dp)
                            ) {
                                item {
                                    miembros.forEach { miembro ->
                                        Card(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .wrapContentWidth() // Para que la Card se adapte al contenido
                                                .fillMaxWidth()
                                                .clickable { expanded = !expanded },
                                            colors = CardDefaults.cardColors(),
                                            elevation = CardDefaults.cardElevation(8.dp),
                                            onClick = {
                                                selectedMiembroId = miembro.miembro_id
                                                nombre = miembro.nombre
                                                apellido = miembro.apellido
                                                fecha_inscripcion = miembro.fecha_inscripcion
                                                showListDialog =
                                                    true // Mostrar la ventana emergente
                                            }
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(16.dp)
                                            ) {
                                                // Texto para "Nombre" en negrita
                                                Row {
                                                    Text(
                                                        text = "Nombre: ",
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = "${miembro.nombre} ${miembro.apellido}"
                                                    )
                                                }

                                                // Texto para "Nacionalidad" en negrita
                                                Row {
                                                    Text(
                                                        text = "Fecha Inscripcion: ",
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = miembro.fecha_inscripcion
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
                                nombre = ""
                                apellido = ""
                                fecha_inscripcion = ""
                                showListDialog2 = false
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
                        title = { Text(text = "Miembros Registrados") },
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
                                                .clickable { expanded = !expanded },
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0xFF4CAF50)
                                            ),
                                            elevation = CardDefaults.cardElevation(8.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(16.dp),
                                                horizontalArrangement = Arrangement.Center,
                                            ) {


                                                Text(
                                                    text = if (selectedMiembroId != null) {
                                                        "Miembro ID: ${selectedMiembroId} seleccionado"
                                                    } else {
                                                        "Seleccionar Miembro"
                                                    }
                                                )
                                            }
                                            DropdownMenu(
                                                expanded = expanded,
                                                onDismissRequest = { expanded = false },
                                                modifier = Modifier.heightIn(max = 200.dp)
                                                    .align(Alignment.CenterHorizontally)
                                            ) {
                                                miembros.forEach { miembro ->
                                                    DropdownMenuItem(
                                                        text = {
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Icon(
                                                                    Icons.Default.Person,  // Cambia este icono si prefieres otro
                                                                    contentDescription = "Icono de miembro",
                                                                    modifier = Modifier.size(12.dp)
                                                                )
                                                                Spacer(modifier = Modifier.width(8.dp))
                                                                Column {
                                                                    Text(
                                                                        text = "${miembro.nombre} ${miembro.apellido}",  // Muestra el nombre completo del miembro
                                                                        fontWeight = FontWeight.Bold
                                                                    )
                                                                    Spacer(
                                                                        modifier = Modifier.height(
                                                                            4.dp
                                                                        )
                                                                    )
                                                                    Text(
                                                                        text = "Fecha Inscripción: ${miembro.fecha_inscripcion}",  // Muestra la fecha de inscripción del miembro
                                                                        style = MaterialTheme.typography.bodySmall
                                                                    )
                                                                }
                                                            }
                                                        },
                                                        onClick = {
                                                            selectedMiembroId = miembro.miembro_id
                                                            nombre = miembro.nombre
                                                            apellido = miembro.apellido
                                                            fecha_inscripcion =
                                                                miembro.fecha_inscripcion
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
                                    TextField(
                                        value = nombre,
                                        onValueChange = { nombre = it },
                                        label = { Text("Nombre") },
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
                                        value = apellido,
                                        onValueChange = { apellido = it },
                                        label = { Text("Apellido") },
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
                                    OutlinedTextField(
                                        value = fecha_inscripcion,
                                        onValueChange = { fecha_inscripcion = it },
                                        label = { Text("Fecha de Inscripción") },
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color.White,
                                            unfocusedBorderColor = Color.White
                                        ),
                                        trailingIcon = {
                                            IconButton(onClick = { datePickerDialog.show() }) {
                                                Icon(
                                                    Icons.Default.DateRange,
                                                    contentDescription = "Seleccionar fecha"
                                                )
                                            }
                                        },
                                        enabled = false,  // Evitar que el usuario escriba manualmente
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                // Abrir el DatePickerDialog cuando se haga clic en el campo de texto
                                                datePickerDialog.show()
                                            }
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                item {
                                    // Botones de Editar y Eliminar
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        TextButton(onClick = {
                                            if (selectedMiembroId == null) {
                                                // Mostrar mensaje de error si no se ha seleccionado un usuario
                                                Toast.makeText(
                                                    context,
                                                    "Por favor, seleccione un miembro primero",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                if (nombre.isNotEmpty() && apellido.isNotEmpty() && fecha_inscripcion.isNotEmpty()) {
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
                                            if (selectedMiembroId == null) {
                                                // Mostrar mensaje de error si no se ha seleccionado un usuario
                                                Toast.makeText(
                                                    context,
                                                    "Por favor, seleccione un miembro primero",
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
                                nombre = ""
                                apellido = ""
                                fecha_inscripcion = ""
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
                        text = { Text(text = "¿Está seguro de que desea actualizar este miembro?") },
                        confirmButton = {
                            TextButton(onClick = {
                                selectedMiembroId?.let { miembroId ->
                                    scope.launch {
                                        try {
                                            withContext(Dispatchers.IO) {
                                                miembroRepository.updateById(
                                                    miembroId,
                                                    nombre,
                                                    apellido,
                                                    fecha_inscripcion
                                                )
                                            }
                                            // Refrescar la lista de usuarios después de la actualización
                                            miembros = withContext(Dispatchers.IO) {
                                                miembroRepository.getAllMiembros()
                                            }

                                            // Mostrar mensaje de éxito y limpiar los campos
                                            Toast.makeText(
                                                context,
                                                "Miembro Actualizado",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            // Limpiar los campos
                                            nombre = ""
                                            apellido = ""
                                            fecha_inscripcion = ""

                                            showEditDialog = false
                                            showListDialog = false
                                            selectedMiembroId =
                                                null // Cerrar también el menú de lista

                                        } catch (e: Exception) {
                                            // En caso de error, mostrar mensaje de error
                                            Toast.makeText(
                                                context,
                                                "Error al actualizar el Miembro",
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
                        text = { Text(text = "¿Está seguro de que desea eliminar este miembro?") },
                        confirmButton = {
                            TextButton(onClick =
                            {
                                selectedMiembroId?.let { miembroId ->
                                    scope.launch {
                                        try {
                                            withContext(Dispatchers.IO) {
                                                miembroRepository.deleteByIdMiembro(miembroId)
                                            }

                                            // Mostrar mensaje de éxito y limpiar los campos
                                            Toast.makeText(
                                                context,
                                                "Miembro Eliminado",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            nombre = ""
                                            apellido = ""
                                            fecha_inscripcion = ""

                                            showDeleteDialog = false
                                            showListDialog =
                                                false // Cerrar también el menú de lista
                                            selectedMiembroId = null

                                        } catch (e: Exception) {
                                            // En caso de error, mostrar mensaje de error
                                            Toast.makeText(
                                                context,
                                                "Error al eliminar el miembro",
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
            }
        }
    }
}
