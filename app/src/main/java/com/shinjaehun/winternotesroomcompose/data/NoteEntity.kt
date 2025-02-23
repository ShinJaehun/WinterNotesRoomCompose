package com.shinjaehun.winternotesroomcompose.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "winter_notes",
    indices = [Index("noteId")]
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val noteId: Long?,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "contents")
    val contents: String,

    @ColumnInfo(name = "date_time")
    val dateTime: String,

    @ColumnInfo(name = "image_path")
    val imagePath: String? = null,

    @ColumnInfo(name = "color")
    val color: String? = null,

    @ColumnInfo(name = "web_link")
    val webLink: String? = null,
)
