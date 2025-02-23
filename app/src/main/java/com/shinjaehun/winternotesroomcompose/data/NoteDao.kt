package com.shinjaehun.winternotesroomcompose.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM winter_notes ORDER BY date_time DESC")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM winter_notes WHERE noteId = :noteId")
    suspend fun getNoteById(noteId: Long): NoteEntity

//    @Query("SELECT * FROM winter_notes WHERE title LIKE '%' || :keyword || '%' OR contents LIKE '%' || :keyword || '%'")
//    suspend fun searchNote(keyword: String): List<RoomNote>

    @Query("DELETE FROM winter_notes WHERE noteId = :noteId")
    suspend fun deleteNote(noteId: Long)

    @Upsert
    suspend fun insertOrUpdateNote(note: NoteEntity)
}