@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.notesencryptedapp.ui.elements.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun ViewNoteScreen(
    noteId: Int,
    viewModel: NoteViewModel,
    navController: NavController
) {
    val note by remember { mutableStateOf(viewModel.note) }

    LaunchedEffect(noteId) {
        viewModel.fetchNoteById(noteId)
    }

    var title by remember { mutableStateOf(note.value?.titleText ?: "Note title empty") }
    var isDecrypted by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }

    var decryptedText by remember { mutableStateOf("Encrypted Note Content") }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        readOnly = !isDecrypted,
                        singleLine = true
                    )
                },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More Actions")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                viewModel.deleteNoteById(noteId)
                                navController.popBackStack()
                                showMenu = false
                            },
                            modifier = Modifier,
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Delete"
                                )
                            },
                            trailingIcon = null,
                            enabled = true,
                            contentPadding = PaddingValues(),
                            interactionSource = remember { MutableInteractionSource() }
                        )
                    }

                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isDecrypted) decryptedText else "Encrypted Note Content",
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
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
                            isDecrypted = true
                            viewModel.decryptNote(noteId, password) { decrypted ->
                                decryptedText = decrypted
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Decrypt")
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ViewNoteScreenPreview() {
    val navController = rememberNavController()
    ViewNoteScreen(
        noteId = 1,
        viewModel = viewModel(),
        navController = navController
    )
}
