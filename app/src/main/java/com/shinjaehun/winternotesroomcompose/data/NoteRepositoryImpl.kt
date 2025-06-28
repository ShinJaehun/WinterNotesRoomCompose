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
    private val imageStorage: ImageStorage
): INoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
            .map { noteEntities ->
                coroutineScope {
                    noteEntities
                        .map {
                            async { it.toNote(imageStorage) }
                        }
                        .map { it.await() }
                }
            }
    }

//    override suspend fun insertNote(note: Note) {
//        val imagePath: String?
//        if (note.noteId == null) {
//            if (note.imageBytes != null && note.thumbnailBytes != null) {
//                imagePath = note.imageBytes.let {
//                    imageStorage.saveImage(it)
//                }
//            } else {
////                Log.i(TAG, "no image!")
//                imagePath = null
//            }
//        } else {
//            val beforeUpdateNote = note.noteId.let { dao.getNoteById(it) }
//            val beforeUpdateNoteImageBytes = beforeUpdateNote.imagePath?.let {
//                imageStorage.getImage(it)
//            }
//            val updateNoteImageBytes = note.imageBytes
//            val isSameImage = (beforeUpdateNoteImageBytes != null &&
//                    updateNoteImageBytes != null &&
//                    beforeUpdateNoteImageBytes.contentEquals(updateNoteImageBytes)) ||
//                    (beforeUpdateNoteImageBytes == null &&
//                            updateNoteImageBytes == null)
//            if (isSameImage) {
//                Log.i(TAG, "same image!!!!!!!!!!!!!!!!!!!")
//                imagePath = beforeUpdateNote.imagePath
//            } else {
//                Log.i(TAG, "different image")
//                beforeUpdateNote.imagePath?.let { imageStorage.deleteImage(it) }
//                imagePath = imageStorage.saveImage(updateNoteImageBytes!!)
//            }
//        }
//
//        Log.i(TAG, "new Note image path: $imagePath")
//        dao.insertOrUpdateNote(note.toNoteEntity(imagePath))
//    }

    override suspend fun insertNote(note: Note) {
        val imagePathResult: ImagePathResult?
        if (note.noteId == null) {
            imagePathResult = if (note.imageBytes != null) {
                note.imageBytes.let {
                    imageStorage.saveImageAndThumbnail(it)
                }
            } else {
                null
            }
        } else {
            val beforeUpdateNote = note.noteId.let { dao.getNoteById(it) }
            val beforeUpdateNoteImageBytes = beforeUpdateNote.imagePath?.let {
                imageStorage.getImage(it)
            }
            val updateNoteImageBytes = note.imageBytes
            val isSameImage = (beforeUpdateNoteImageBytes != null &&
                    updateNoteImageBytes != null &&
                    beforeUpdateNoteImageBytes.contentEquals(updateNoteImageBytes)) ||
                    (beforeUpdateNoteImageBytes == null &&
                            updateNoteImageBytes == null)
            imagePathResult = if (isSameImage) {
                Log.i(TAG, "same image!!!!!!!!!!!!!!!!!!!")
                ImagePathResult(
                    originalPath = beforeUpdateNote.imagePath,
                    thumbnailPath = beforeUpdateNote.thumbnailPath
                )
            } else {
                Log.i(TAG, "different image")
                beforeUpdateNote.imagePath?.let { imageStorage.deleteImage(it) }
                beforeUpdateNote.thumbnailPath?.let { imageStorage.deleteImage(it) }
                val newResult = imageStorage.saveImageAndThumbnail(updateNoteImageBytes!!)
                newResult
            }
        }

        Log.i(TAG, "new Note image path: $imagePathResult")
        dao.insertOrUpdateNote(
            note.toNoteEntity(
                imagePath = imagePathResult?.originalPath,
                thumbnailPath = imagePathResult?.thumbnailPath
            )
        )
    }

    override suspend fun getNoteById(noteId: Long): Note {
        return dao.getNoteById(noteId).toNote(imageStorage)
    }

    override suspend fun deleteNote(noteId: Long) {
        val entity = dao.getNoteById(noteId)
        entity.imagePath?.let {
            imageStorage.deleteImage(it)
        }
        entity.thumbnailPath?.let {
            imageStorage.deleteImage(it)
        }
        dao.deleteNote(noteId)
    }
}