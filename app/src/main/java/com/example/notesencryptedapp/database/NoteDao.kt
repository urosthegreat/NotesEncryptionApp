package com.example.notesencryptedapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note_table ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

    @Query("DELETE FROM note_table WHERE id = :noteId")
    suspend fun deleteById(noteId: Int)

    @Update
    suspend fun update(note: Note)

    @Query("SELECT * FROM note_table WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): Note?

    @Query("SELECT * FROM note_table WHERE note_table.titleText LIKE '%' || :query || '%'")
    fun searchNotesByTitle(query: String): Flow<List<Note>>
}
