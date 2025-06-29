package com.shinjaehun.winternotesroomcompose.presentation

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinjaehun.winternotesroomcompose.domain.INoteRepository
import com.shinjaehun.winternotesroomcompose.domain.ImageColor
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

    @VisibleForTesting
    fun getInternalState(): NoteListState = _state.value

    @VisibleForTesting
    fun getNewNoteForTest(): Note? = newNote

    @VisibleForTesting
    fun updateNewNoteForTest(update: (Note?) -> Note?) {
        newNote = update(newNote)
    }

    fun onEvent(event: NoteListEvent) {
        when(event) {
            NoteListEvent.DeleteNote -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            isSelectedNoteSheetOpen = false
                        )
                    }
                    _state.value.selectedNote?.noteId?.let { id ->
                        repository.deleteNote(id)
                    }
                    delay(300L)
                    _state.update {
                        it.copy(
                            selectedNote = null,
                            isLoading = false
                        )
                    }
                }
            }
            NoteListEvent.DismissNote -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isSelectedNoteSheetOpen = false,
                            isAddNoteSheetOpen = false,
                        )
                    }
                    delay(300L)
                    _state.update {
                        it.copy(
                            titleError = null,
                            contentsError = null,
                            tempImageBytes = null,
                            selectedNote = null,
                        )
                    }
                }
            }
            is NoteListEvent.EditNote -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            isAddNoteSheetOpen = true,
                            isSelectedNoteSheetOpen = false,
                        )
                    }
                    delay(300L)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedNote = null,
                        )
                    }
                    newNote = event.note
                }
            }
            NoteListEvent.OnAddNewNoteClick -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            isAddNoteSheetOpen = true
                        )
                    }
                    delay(300L)
                    _state.update { it.copy(isLoading = false)}
                    newNote = Note(
                        noteId = null,
                        title = "",
                        contents = "",
                        dateTime = currentTime(),
                        imagePath = null,
                        thumbnailPath = null,
                        color = ImageColor.WHITE,
                        webLink = null,
                    )
                }
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
                if (newNote == null) {
                    newNote = Note(
                        noteId = null,
                        title = "",
                        contents = "",
                        dateTime = currentTime(),
                        imagePath = null,
                        thumbnailPath = null,
                        color = ImageColor.WHITE,
                        webLink = null
                    )
                }
                newNote = newNote?.copy(
                    imagePath = null, // will be replaced after saving
                    thumbnailPath = null
                )
                _state.update { it.copy(tempImageBytes = event.bytes) } // temporary cache
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
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    newNote?.let { note ->
                        val result = NoteValidator.validateNote(note)
                        val errors = listOfNotNull(
                            result.titleError,
                            result.contentError
                        )
                        if (errors.isEmpty()) {
                            _state.update {
                                it.copy(
                                    isAddNoteSheetOpen = false,
                                    titleError = null,
                                    contentsError = null
                                )
                            }
                            repository.insertNote(note, _state.value.tempImageBytes)
                            _state.update {
                                it.copy(
                                    tempImageBytes = null,
                                    isLoading = false
                                )
                            }
                            delay(300L)  // UI가 닫히는 애니메이션 타이밍 보장
                            newNote = null

                        } else {
                            _state.update {
                                it.copy(
                                    titleError = result.titleError,
                                    contentsError = result.contentError,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
            }
            is NoteListEvent.SelectNote -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isLoading = true,
                            isSelectedNoteSheetOpen = true,
                        )
                    }
                    delay(300)   // UI가 닫히는 애니메이션 타이밍 보장
                    _state.update {
                        it.copy(
                            selectedNote = event.note,
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
