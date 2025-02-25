package com.shinjaehun.winternotesroomcompose.presentation

import com.shinjaehun.winternotesroomcompose.domain.Note
import com.shinjaehun.winternotesroomcompose.domain.ImageColor

sealed interface NoteListEvent {
    object OnAddNewNoteClick: NoteListEvent
    object DismissNote: NoteListEvent

    data class OnTitleChanged(val value: String): NoteListEvent
    data class OnContentsChanged(val value: String): NoteListEvent

    data class OnColorChanged(val value: ImageColor): NoteListEvent

    data class OnUrlChanged(val value: String): NoteListEvent
    object OnUrlDeleteClicked: NoteListEvent

    data class OnImagePicked(val bytes: ByteArray): NoteListEvent
    object OnAddImageClicked: NoteListEvent
    object OnImageDeleteClicked: NoteListEvent

    object SaveNote: NoteListEvent

    data class SelectNote(val note: Note): NoteListEvent
    data class EditNote(val note: Note): NoteListEvent

    object DeleteNote: NoteListEvent
}