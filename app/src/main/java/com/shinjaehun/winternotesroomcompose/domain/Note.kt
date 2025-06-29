package com.shinjaehun.winternotesroomcompose.domain

data class Note(
    val noteId: Long?,
    val title: String,
    val contents: String,
    val dateTime: String,
//    val imageBytes: ByteArray?,
//    val thumbnailBytes: ByteArray?,

    val imagePath: String?,           // ✅ 추가
    val thumbnailPath: String?,           // ✅ 추가

    val color: ImageColor?,
    val webLink: String?,
)
