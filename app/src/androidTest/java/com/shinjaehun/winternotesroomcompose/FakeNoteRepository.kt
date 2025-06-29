package com.shinjaehun.winternotesroomcompose

import com.shinjaehun.winternotesroomcompose.domain.INoteRepository
import com.shinjaehun.winternotesroomcompose.domain.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNoteRepository : INoteRepository {

    private val notes = mutableListOf<Note>()
    private var idCounter = 1L

    override fun getNotes(): Flow<List<Note>> = flow {
        emit(notes.toList())
    }

    override suspend fun getNoteById(noteId: Long): Note {
        return notes.find { it.noteId == noteId }
            ?: throw IllegalStateException("Note not found")
    }

    override suspend fun deleteNote(noteId: Long) {
        notes.removeIf { it.noteId == noteId }
    }

    override suspend fun insertNote(note: Note, imageBytes: ByteArray?) {
        val noteToInsert = if (note.noteId == null) {
            note.copy(
                noteId = idCounter++,
                imagePath = if (imageBytes != null) "fake/original/path.jpg" else null,
                thumbnailPath = if (imageBytes != null) "fake/thumbnail/path.jpg" else null
            )
        } else {
            note
        }

        notes.removeIf { it.noteId == noteToInsert.noteId }
        notes.add(noteToInsert)
    }
}
