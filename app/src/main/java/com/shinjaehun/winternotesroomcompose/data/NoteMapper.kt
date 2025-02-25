package com.shinjaehun.winternotesroomcompose.data

import androidx.compose.foundation.Image
import com.shinjaehun.winternotesroomcompose.domain.ImageColor
import com.shinjaehun.winternotesroomcompose.domain.Note

suspend fun NoteEntity.toNote(imageStorage: ImageStorage): Note {
    return Note(
        noteId = noteId,
        title = title,
        contents = contents,
        dateTime = dateTime,
        imageBytes = imagePath?.let { imageStorage.getImage(it) },
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

fun Note.toNoteEntity(imagePath: String?): NoteEntity {
    return NoteEntity(
        noteId = noteId,
        title = title,
        contents = contents,
        dateTime = dateTime,
        imagePath = imagePath,
        color = color.toString(),
        webLink = webLink
    )
}