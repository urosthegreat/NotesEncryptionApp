package com.example.notesencryptedapp.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesencryptedapp.network.SecureTextApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {
    private val _note = MutableLiveData<Note?>()
    val note: LiveData<Note?> = _note

    fun fetchNoteById(noteId: Int) {
        viewModelScope.launch {
            _note.value = noteRepository.getNoteById(noteId)
        }
    }

    fun insert(note: Note) = viewModelScope.launch {
        noteRepository.insert(note)
    }

    var allNotes: Flow<List<Note>> = noteRepository.getAllNotes1()


    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.update(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            noteRepository.deleteAll()
        }
    }

    fun deleteNoteById(noteId: Int) {
        viewModelScope.launch {
            noteRepository.deleteNoteById(noteId)
        }
    }

    fun decryptNote(noteId: Int, password: String, onDecrypted: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = noteRepository.getNoteById(noteId)
            if (note != null && note.password == password) {
                try {
                    val decryptedText =
                        SecureTextApiService.decryptData(note.keyText, note.noteText)
                    onDecrypted(decryptedText)
                } catch (e: IOException) {
                    Log.e("NoteViewModel", "Error decrypting note", e)
                }
            } else {
                onDecrypted("Failed to decrypt note")
                Log.e("NoteViewModel", "Note not found or password mismatch")
                //TODO(Here we can do a fail-safe mechanism for when API does not work)
            }
        }
    }

    fun encryptAndSaveNote(title: String, noteText: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val key = SecureTextApiService.getKey()
                val encryptedText = SecureTextApiService.encryptData(key, noteText)
                val newNote = Note(
                    titleText = title,
                    noteText = encryptedText,
                    keyText = key,
                    password = password,
                    createdAt = Date()
                )
                noteRepository.insert(newNote)
            } catch (e: Exception) {
                Log.e("NoteViewModel", "Error encrypting note", e)
            }
        }
    }

    private val _searchedNotes = MutableLiveData<List<Note>>()
    val searchedNotes: LiveData<List<Note>> = _searchedNotes

    fun searchNotes(query: String) {
        viewModelScope.launch {
            _searchedNotes.value = noteRepository.searchNotesByTitle(query)
        }
    }
}