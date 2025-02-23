package com.shinjaehun.winternotesroomcompose.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinjaehun.winternotesroomcompose.domain.INoteRepository
import com.shinjaehun.winternotesroomcompose.domain.Note
import com.shinjaehun.winternotesroomcompose.domain.NoteValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val repository: INoteRepository
): ViewModel() {

    private val _state = MutableStateFlow(NoteListState(
        notes = notes
    ))
    val state = _state.asStateFlow()

//    private val _state = MutableStateFlow(NoteListState())
//    val state = combine(
//        _state,
//        repository.getNotes()
//    ) { state, notes ->
//        state.copy(
//            notes = notes
//        )
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), NoteListState())

    var newNote: Note? by mutableStateOf(null)
        private set

    fun onEvent(event: NoteListEvent) {
        when(event) {
            NoteListEvent.DeleteNote -> {
                viewModelScope.launch {
                    _state.value.selectedNote?.noteId?.let { id ->
                        _state.update { it.copy(
                            isSelectedNoteSheetOpen = false
                        ) }
                        repository.deleteNote(id)
                        delay(300L)
                        _state.update { it.copy(
                            selectedNote = null
                        ) }
                    }
                }
            }
            NoteListEvent.DismissNote -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        isSelectedNoteSheetOpen = false,
                        isAddNoteSheetOpen = false,
                        titleError = null,
                        contentsError = null
                    ) }
                    delay(300L)
                    _state.update { it.copy(
                        selectedNote = null
                    ) }
                }
            }
            is NoteListEvent.EditNote -> {
                _state.update { it.copy(
                    selectedNote = null,
                    isAddNoteSheetOpen = true,
                    isSelectedNoteSheetOpen = false
                ) }
                newNote = event.note
            }
            NoteListEvent.OnAddNewNoteClick -> {
                _state.update { it.copy(
                    isAddNoteSheetOpen = true
                ) }
                newNote = Note(
                    noteId = null,
                    title = "",
                    contents = "",
                    dateTime = currentTime(),
                    imageBytes = null,
                    color = null,
                    webLink = null,
                )
            }
            is NoteListEvent.OnColorChanged -> {
                newNote = newNote?.copy(
                    color = event.value
                )
            }
            is NoteListEvent.OnContentsChanged -> {
                newNote = newNote?.copy(
                    contents = event.value
                )
            }
            is NoteListEvent.OnImagePicked -> {
                newNote = newNote?.copy(
                    imageBytes = event.bytes
                )
            }
            NoteListEvent.OnImageDeleteClicked -> TODO()
            is NoteListEvent.OnTitleChanged -> {
                newNote = newNote?.copy(
                    title = event.value
                )
            }
            is NoteListEvent.OnUrlChanged -> {
                newNote = newNote?.copy(
                    webLink = event.value
                )
            }
            NoteListEvent.OnUrlDeleteClicked -> TODO()
            NoteListEvent.SaveNote -> {
                newNote?.let { note ->
                    val result = NoteValidator.validateNote(note)
                    val errors = listOfNotNull(
                        result.titleError,
                        result.contentError
                    )
                    if(errors.isEmpty()) {
                        _state.update { it.copy(
                            isAddNoteSheetOpen = false,
                            titleError = null,
                            contentsError = null
                        ) }
                        viewModelScope.launch {
                            repository.insertNote(note)
                            delay(300L)
                            newNote = null
                        }
                    } else {
                        _state.update { it.copy(
                            titleError = result.titleError,
                            contentsError = result.contentError
                        ) }
                    }
                }
            }
            is NoteListEvent.SelectNote -> {
                _state.update { it.copy(
                    selectedNote = event.note,
                    isSelectedNoteSheetOpen = true
                ) }
            }
            else -> Unit
        }
    }

}

private val notes = (1..30).map {
    Note(
        noteId = null,
        title = "Title $it",
        contents = "contents $it",
        dateTime = currentTime(),
        imageBytes = null,
        color = null,
        webLink = null
    )
}

internal fun currentTime() = SimpleDateFormat(
    "yyyy MMMM dd, EEEE, HH:mm a",
    Locale.getDefault()).format(Date())