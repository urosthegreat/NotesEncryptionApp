package com.example.notesencryptedapp.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    fun getAllNotes() {
        noteDao.getAllNotes()
    }

    fun getAllNotes1(): Flow<List<Note>> = noteDao.getAllNotes()


    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    suspend fun deleteAll() {
        noteDao.deleteAll()
    }

    suspend fun deleteNoteById(noteId: Int) {
        noteDao.deleteById(noteId)
    }

    suspend fun getNoteById(noteId: Int): Note? {
        return noteDao.getNoteById(noteId)
    }

    suspend fun searchNotesByTitle(query: String): List<Note> {
        return noteDao.searchNotesByTitle(query).single()
    }
}
