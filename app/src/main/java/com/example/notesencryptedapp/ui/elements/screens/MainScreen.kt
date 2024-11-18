@file:OptIn(
    ExperimentalMaterial3Api::class
)

package com.example.notesencryptedapp.ui.elements.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notesencryptedapp.database.Note
import com.example.notesencryptedapp.database.NoteViewModel
import com.example.notesencryptedapp.routes.Destinations

@Composable
fun MainScreen(
    viewModel: NoteViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val notes by viewModel.allNotes.collectAsState(initial = emptyList())
    val searchedNotes by viewModel.searchedNotes.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopBar(onSearchQueryChanged = { query ->
                viewModel.searchNotes(query)
            })
        },
        bottomBar = {
            BottomBar(onAddNoteClicked = {
                navController.navigate(Destinations.CreateNoteScreenRoute)
            })
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                NotesList(notes = searchedNotes.ifEmpty { notes },
                    onNoteClicked = { note ->
                        navController.navigate("${Destinations.ViewNoteScreenRoute}/${note.id}")
                    })
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NotesApp()
}

@Composable
fun NotesApp() {
    val navController = rememberNavController()
    val noteViewModel: NoteViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Destinations.MainScreenRoute
    ) {
        composable(Destinations.MainScreenRoute) {
            MainScreen(viewModel = noteViewModel, navController = navController)
        }
        composable(Destinations.CreateNoteScreenRoute) {
            CreateNoteScreen(navController = navController, viewModel = noteViewModel)
        }
        composable(
            route = "${Destinations.ViewNoteScreenRoute}/{note_id}",
            arguments = listOf(navArgument("note_id") { type = NavType.IntType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("note_id") ?: return@composable
            ViewNoteScreen(
                noteId = noteId,
                navController = navController,
                viewModel = noteViewModel
            )
        }
    }
}


@Composable
fun TopBar(onSearchQueryChanged: (String) -> Unit) {
    TopAppBar(
        title = { Text("Notes") },
        actions = {
            var searchQuery by remember { mutableStateOf("") }

            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    onSearchQueryChanged(it)
                },
                label = { Text("Search by Title") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) }
            )
        }
    )
}


@Composable
fun BottomBar(onAddNoteClicked: () -> Unit) {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            FloatingActionButton(onClick = onAddNoteClicked) {
                Icon(Icons.Filled.Add, contentDescription = "Add Note")
            }
        }
    }
}


@Composable
fun NotesList(notes: List<Note>, onNoteClicked: (Note) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            count = notes.size,
            itemContent = { index ->
                val note = notes[index]
                ListItem(
                    headlineText = { Text(note.titleText) },
                    overlineText = { Text("Created: ${note.createdAt}") },
                    modifier = Modifier.clickable { onNoteClicked(note) }
                )
            }
        )
    }
}