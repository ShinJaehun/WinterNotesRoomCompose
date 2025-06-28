package com.shinjaehun.winternotesroomcompose.presentation

import android.util.Log
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

private const val TAG = "NoteListViewModel"

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val repository: INoteRepository
): ViewModel() {

//    private val _state = MutableStateFlow(NoteListState(
//        notes = notes
//    ))
//    val state = _state.asStateFlow()

    private val _state = MutableStateFlow(NoteListState())
    val state = combine(
        _state,
        repository.getNotes()
    ) { state, notes ->
        state.copy(
            notes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), NoteListState())

    var newNote: Note? by mutableStateOf(null)
        private set

    fun onEvent(event: NoteListEvent) {
        when(event) {
            NoteListEvent.DeleteNote -> {
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    _state.value.selectedNote?.noteId?.let { id ->
                        _state.update { it.copy(
                            isSelectedNoteSheetOpen = false
                        ) }
                        repository.deleteNote(id)
                        delay(300L)
                        _state.update { it.copy(
                            selectedNote = null,
                            isLoading = false
                        ) }
                    }
                }
            }
            NoteListEvent.DismissNote -> {
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    _state.update { it.copy(
                        isSelectedNoteSheetOpen = false,
                        isAddNoteSheetOpen = false,
                        titleError = null,
                        contentsError = null
                    ) }
                    delay(300L)
                    _state.update { it.copy(
                        selectedNote = null,
                        isLoading = false
                    ) }
                }
            }
            is NoteListEvent.EditNote -> {
                _state.update { it.copy(isLoading = true) }
                _state.update { it.copy(
                    selectedNote = null,
                    isAddNoteSheetOpen = true,
                    isSelectedNoteSheetOpen = false,
                    isLoading = false
                ) }
                newNote = event.note
            }
            NoteListEvent.OnAddNewNoteClick -> {
                _state.update { it.copy(isLoading = true) }
                _state.update { it.copy(
                    isAddNoteSheetOpen = true,
                    isLoading = false
                ) }
                newNote = Note(
                    noteId = null,
                    title = "",
                    contents = "",
                    dateTime = currentTime(),
                    imageBytes = null,
                    thumbnailBytes = null,
                    imagePath = null,
                    thumbnailPath = null,
                    color = null,
                    webLink = null,
                )
            }
            is NoteListEvent.OnColorChanged -> {
                Log.i(TAG, "color selected: ${event.value.toString()}")
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
                Log.i(TAG, "On Image Picked, Image Bytes: ${event.bytes}")
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
                _state.update { it.copy(isLoading = true) }
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
                            delay(300L)  // UI가 닫히는 애니메이션 타이밍 보장
                            newNote = null
                            _state.update { it.copy(isLoading = false) }
                        }
                    } else {
                        _state.update { it.copy(
                            titleError = result.titleError,
                            contentsError = result.contentError,
                            isLoading = false
                        ) }
                    }
                }
            }
//            is NoteListEvent.SelectNote -> {
//                Log.i(TAG, "clicked note: ${event.note}")
//                viewModelScope.launch {
//                    _state.update { it.copy(isLoading = true) }
//
//                    delay(100)
//                    _state.update { it.copy(
//                        selectedNote = event.note,
//                        isSelectedNoteSheetOpen = true,
//                        isLoading = false
//                    )}
//                 }
//            }

            is NoteListEvent.SelectNote -> {
                Log.i(TAG, "clicked note: ${event.note}")
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }

                    delay(100)   // UI가 닫히는 애니메이션 타이밍 보장

                    val lightNote = event.note.copy(imageBytes = null) // 이미지 제거
                    _state.update {
                        it.copy(
                            selectedNote = lightNote,
                            isSelectedNoteSheetOpen = true,
                            isLoading = false
                        )
                    }
                }
            }
            else -> Unit
        }
    }

}

//private val notes = (1..30).map {
//    Note(
//        noteId = null,
//        title = "Title $it",
//        contents = "contents $it",
//        dateTime = currentTime(),
//        imageBytes = null,
//        color = null,
//        webLink = null
//    )
//}
