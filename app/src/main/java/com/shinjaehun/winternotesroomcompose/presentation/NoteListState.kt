package com.shinjaehun.winternotesroomcompose.presentation

import com.shinjaehun.winternotesroomcompose.domain.Note

data class NoteListState(
    val notes: List<Note> = emptyList(),
//    val searchedNotes: List<Note> = emptyList()
    val selectedNote: Note? = null,

    val isAddNoteSheetOpen: Boolean = false,
    val isSelectedNoteSheetOpen: Boolean = false,

    val titleError: String? = null,
    val contentsError: String? = null,

    val isLoading: Boolean = false
)
