package com.shinjaehun.winternotesroomcompose.domain

data class Note(
    val noteId: Long?,
    val title: String,
    val contents: String,
    val dateTime: String,
    val imageBytes: ByteArray?,
    val thumbnailBytes: ByteArray?,
    val color: ImageColor?,
    val webLink: String?,
)
