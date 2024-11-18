package com.example.notesencryptedapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "noteText") val noteText: String,
    @ColumnInfo(name = "keyText") val keyText: String,
    @ColumnInfo(name = "titleText") val titleText: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "createdAt") val createdAt: Date
)