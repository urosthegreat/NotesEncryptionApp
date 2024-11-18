package com.example.notesencryptedapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.notesencryptedapp.utils.Converters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                ).addCallback(NoteDatabaseCallback(scope)).build()
                INSTANCE = instance
                return@synchronized instance
            }
        }
    }

    private class NoteDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch { populateDatabase(database.noteDao()) }
            }
        }

        suspend fun populateDatabase(noteDao: NoteDao) {
            noteDao.deleteAll()
            noteDao.insert(
                Note(
                    noteText = "Note 1",
                    keyText = "key123",
                    titleText = "Note title 1",
                    createdAt = Calendar.getInstance().time,
                    password = "Password 1"
                )
            )
            noteDao.insert(
                Note(
                    noteText = "Note 2",
                    keyText = "key456",
                    titleText = "Note title 2",
                    createdAt = Calendar.getInstance().time,
                    password = "Password 2"
                )
            )
            noteDao.insert(
                Note(
                    noteText = "Note 3",
                    keyText = "key789",
                    titleText = "Note title 3",
                    createdAt = Calendar.getInstance().time,
                    password = "Password 3"
                )
            )
        }
    }
}
