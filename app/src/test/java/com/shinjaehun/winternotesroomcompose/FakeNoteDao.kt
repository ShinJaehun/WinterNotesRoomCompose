package com.shinjaehun.winternotesroomcompose

import com.shinjaehun.winternotesroomcompose.data.NoteDao
import com.shinjaehun.winternotesroomcompose.data.NoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNoteDao : NoteDao {

    private val notes = mutableListOf<NoteEntity>()
    private var autoIncrementId = 1L

    override suspend fun insertOrUpdateNote(note: NoteEntity) {
        val id = note.noteId ?: autoIncrementId++
        val entity = note.copy(noteId = id)
        notes.removeIf { it.noteId == id } // 중복 방지
        notes.add(entity)
    }

    override suspend fun deleteNote(noteId: Long) {
        notes.removeIf { it.noteId == noteId }
    }

    override fun getNotes(): Flow<List<NoteEntity>> = flow {
        emit(notes.toList())
    }

    override suspend fun getNoteById(noteId: Long): NoteEntity? {
        return notes.find { it.noteId == noteId }
    }
}
