package com.shinjaehun.winternotesroomcompose.data

import androidx.compose.foundation.Image
import com.shinjaehun.winternotesroomcompose.domain.ImageColor
import com.shinjaehun.winternotesroomcompose.domain.Note
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

//suspend fun NoteEntity.toNote(imageStorage: ImageStorage): Note {
//    return Note(
//        noteId = noteId,
//        title = title,
//        contents = contents,
//        dateTime = dateTime,
//        imageBytes = imagePath?.let { imageStorage.getImage(it) },
//        thumbnailBytes = thumbnailPath?.let { imageStorage.getImage(it)},
//        color = when(color) {
//            "WHITE" -> ImageColor.WHITE
//            "RED" -> ImageColor.RED
//            "GREEN" -> ImageColor.GREEN
//            "YELLOW" -> ImageColor.YELLOW
//            else -> null
//        },
//        webLink = webLink
//    )
//}

//suspend fun NoteEntity.toNote(imageStorage: ImageStorage): Note = coroutineScope {
//    val imageDeferred = async { imagePath?.let { imageStorage.getImage(it) } }
//    val thumbnailDeferred = async { thumbnailPath?.let { imageStorage.getImage(it) } }
//
//    Note(
//        noteId = noteId,
//        title = title,
//        contents = contents,
//        dateTime = dateTime,
//        imageBytes = imageDeferred.await(),
//        thumbnailBytes = thumbnailDeferred.await(),
//        color = when(color) {
//            "WHITE" -> ImageColor.WHITE
//            "RED" -> ImageColor.RED
//            "GREEN" -> ImageColor.GREEN
//            "YELLOW" -> ImageColor.YELLOW
//            else -> null
//        },
//        webLink = webLink
//    )
//}

suspend fun NoteEntity.toNote(imageStorage: ImageStorage): Note {
    return Note(
        noteId = noteId,
        title = title,
        contents = contents,
        dateTime = dateTime,
        imageBytes = null,   // <- Lazy load 용으로 비워둠
        thumbnailBytes = thumbnailPath?.let { imageStorage.getImage(it)},
        imagePath = imagePath,
        thumbnailPath = thumbnailPath,
        color = when(color) {
            "WHITE" -> ImageColor.WHITE
            "RED" -> ImageColor.RED
            "GREEN" -> ImageColor.GREEN
            "YELLOW" -> ImageColor.YELLOW
            else -> null
        },
        webLink = webLink
    )
}

fun Note.toNoteEntity(imagePath: String?, thumbnailPath: String?): NoteEntity {
    return NoteEntity(
        noteId = noteId,
        title = title,
        contents = contents,
        dateTime = dateTime,
        imagePath = imagePath,
        thumbnailPath = thumbnailPath,
        color = color.toString(),
        webLink = webLink
    )
}