package com.shinjaehun.winternotesroomcompose.domain

import kotlinx.coroutines.flow.Flow

interface INoteRepository {
    fun getNotes(): Flow<List<Note>>
//    fun searchNote(keyword: String): Flow<List<Note>>
    suspend fun getNoteById(noteId: Long): Note
    suspend fun deleteNote(noteId: Long)
    suspend fun insertNote(note: Note, imageBytes: ByteArray? = null)
}