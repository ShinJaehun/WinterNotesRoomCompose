package com.shinjaehun.winternotesroomcompose.data

import android.util.Log
import com.shinjaehun.winternotesroomcompose.domain.INoteRepository
import com.shinjaehun.winternotesroomcompose.domain.Note
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

private const val TAG = "NoteRepositoryImpl"

class NoteRepositoryImpl(
    private val dao: NoteDao,
    private val imageStorage: IImageStorage
): INoteRepository {

    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
            .map { noteEntities ->
                noteEntities.map { it.toNote() } // imagePath와 thumbnailPath만 포함
            }
    }

    override suspend fun insertNote(note: Note, imageBytes: ByteArray?) {

        val imagePathResult: ImagePathResult? =
            if(note.noteId == null) {
                imageBytes?.let { imageStorage.saveImageAndThumbnail(it) }
            } else {
                val existing = dao.getNoteById(note.noteId)
                val existingBytes = existing?.imagePath?.let { imageStorage.getImage(it)}

                val isSameImage = imageBytes != null &&
                        existingBytes != null &&
                        existingBytes.contentHashCode() == imageBytes.contentHashCode() &&
                        existingBytes.contentEquals(imageBytes)

                if (isSameImage) {
                    ImagePathResult(
                        originalPath = existing?.imagePath,
                        thumbnailPath = existing?.thumbnailPath
                    )
                } else {
                    existing?.imagePath?.let { imageStorage.deleteImage(it)}
                    existing?.thumbnailPath?.let { imageStorage.deleteImage(it)}
                    imageBytes?.let { imageStorage.saveImageAndThumbnail(it)}
                }
            }

        val updateNote = note.copy(
            imagePath = imagePathResult?.originalPath,
            thumbnailPath = imagePathResult?.thumbnailPath
        )

        dao.insertOrUpdateNote(updateNote.toNoteEntity())
    }

    override suspend fun getNoteById(noteId: Long): Note {
        return dao.getNoteById(noteId)?.toNote() ?: throw IllegalStateException("Note not found")
    }

    override suspend fun deleteNote(noteId: Long) {
        val entity = dao.getNoteById(noteId)
        entity?.imagePath?.let {
            imageStorage.deleteImage(it)
        }
        entity?.thumbnailPath?.let {
            imageStorage.deleteImage(it)
        }
        dao.deleteNote(noteId)
    }
}