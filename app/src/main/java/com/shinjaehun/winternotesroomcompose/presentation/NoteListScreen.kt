package com.shinjaehun.winternotesroomcompose.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shinjaehun.winternotesroomcompose.presentation.components.AddNoteSheet
import com.shinjaehun.winternotesroomcompose.presentation.components.NoteDetailSheet
import com.shinjaehun.winternotesroomcompose.presentation.components.NoteListItem

@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel = viewModel(),
    imagePicker: ImagePicker
) {
    val state by viewModel.state.collectAsState()
    val onEvent = viewModel::onEvent
    val newNote = viewModel.newNote

    imagePicker.registerPicker { imageBytes ->
        onEvent(NoteListEvent.OnImagePicked(imageBytes))
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(NoteListEvent.OnAddNewNoteClick)
                },
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                   imageVector = Icons.Default.Add,
                   contentDescription = "Add note"
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "My notes (${state.notes.size})",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            items(
                items = state.notes,
                key = { it.noteId ?: it.hashCode() } // noteId가 null인 경우까지 대비
            ) { note ->
                NoteListItem(
                    note = note,
                    modifier = Modifier
                        .clickable {
                            onEvent(NoteListEvent.SelectNote(note))
                        }
                )
            }
        }
    }
    if(state.isLoading) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        NoteDetailSheet(
            isOpen = state.isSelectedNoteSheetOpen,
            selectedNote = state.selectedNote,
            onEvent = onEvent
        )
        AddNoteSheet(
            state = state,
            newNote = newNote,
            isOpen = state.isAddNoteSheetOpen,
            onEvent = { event ->
                if (event is NoteListEvent.OnAddImageClicked) {
                    imagePicker.pickImage()
                }
                onEvent(event)
            }
        )
    }
}