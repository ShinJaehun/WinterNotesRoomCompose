package com.shinjaehun.winternotesroomcompose.data

import com.shinjaehun.winternotesroomcompose.domain.Note

suspend fun NoteEntity.toNote(imageStorage: ImageStorage): Note {
    return Note(
        noteId = noteId,
        title = title,
        contents = contents,
        dateTime = dateTime,
        imageBytes = imagePath?.let { imageStorage.getImage(it) },
        color = color,
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
        color = color,
        webLink = webLink
    )
}