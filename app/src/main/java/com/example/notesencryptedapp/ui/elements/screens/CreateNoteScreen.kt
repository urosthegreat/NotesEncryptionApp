@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.notesencryptedapp.ui.elements.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.notesencryptedapp.database.NoteViewModel

@Composable
fun CreateNoteScreen(navController: NavController, viewModel: NoteViewModel) {
    var title by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    fun onEncryptClicked(title: String, noteText: String, password: String) {
        viewModel.encryptAndSaveNote(title, noteText, password)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("Create Note") }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Note") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    maxLines = Int.MAX_VALUE,
                    singleLine = false
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            if (title.isNotEmpty() && noteText.isNotEmpty() && password.isNotEmpty()) {
                                onEncryptClicked(title, noteText, password)
                            }
                            navController.popBackStack()
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Encrypt")
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CreateNoteScreenPreview() {
    val navController = rememberNavController()
    CreateNoteScreen(navController, viewModel())
}

