package com.shinjaehun.winternotesroomcompose.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.shinjaehun.winternotesroomcompose.R
import com.shinjaehun.winternotesroomcompose.domain.ImageColor
import com.shinjaehun.winternotesroomcompose.domain.Note
import com.shinjaehun.winternotesroomcompose.presentation.NoteListEvent
import com.shinjaehun.winternotesroomcompose.presentation.NoteListState

@Composable
fun AddNoteSheet(
    state: NoteListState,
    newNote: Note?,
    isOpen: Boolean,
    onEvent: (NoteListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheetFromWish(
        visible = isOpen,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(60.dp))
                if(newNote?.imageBytes == null) {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(40))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable {
                                onEvent(NoteListEvent.OnAddImageClicked)
                            }
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                shape = RoundedCornerShape(40)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_image),
                            contentDescription = "Add Image",
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                } else {
                    NoteImage(
                        note = newNote,
                        modifier = Modifier
                            .size(150.dp)
                            .clickable {
                                onEvent(NoteListEvent.OnAddImageClicked)
                            }
                    )
                }
                Spacer(Modifier.height(16.dp))
                NoteTextField(
                    value = newNote?.title ?: "",
                    placeholder = "Title",
                    error = state.titleError,
                    onValueChanged = {
                        onEvent(NoteListEvent.OnTitleChanged(it))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                NoteColorButtons(
                    selectedColor = newNote?.color ?: ImageColor.WHITE,
                    onOptionSelected = {
                        onEvent(NoteListEvent.OnColorChanged(it))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                NoteTextField(
                    value = newNote?.contents ?: "",
                    placeholder = "Contents",
                    error = state.contentsError,
                    onValueChanged = {
                        onEvent(NoteListEvent.OnContentsChanged(it))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                NoteTextField(
                    value = newNote?.webLink ?: "",
                    placeholder = "Web Link",
                    error = null,
                    onValueChanged = {
                        onEvent(NoteListEvent.OnUrlChanged(it))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        onEvent(NoteListEvent.SaveNote)
                    }
                ) {
                    Text(text = "Save")
                }
            }
            IconButton(
                onClick = {
                    onEvent(NoteListEvent.DismissNote)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close"
                )
            }
        }
    }
}

@Composable
fun NoteTextField(
    value: String,
    placeholder: String,
    error: String?,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        OutlinedTextField(
            value = value,
            placeholder = {
                Text(text = placeholder)
            },
            onValueChange = onValueChanged,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )
        if(error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun NoteColorButtons(
    selectedColor: ImageColor,
    onOptionSelected: (ImageColor) -> Unit,
    modifier: Modifier = Modifier
) {
//    var selectedOption by remember { mutableStateOf<ImageColor>(ImageColor.RED) }

    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ImageColor.entries.forEach { imageColor ->
            Column(
                modifier = Modifier
                    .clickable { onOptionSelected(imageColor) }
                    .padding(horizontal = 6.dp),
            ) {
                RadioButton(
                    selected = ( imageColor == selectedColor ),
                    onClick = { onOptionSelected(imageColor) }
                )
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .border(2.dp, Color.LightGray, CircleShape)
                        .padding(1.dp)
                        .clip(CircleShape)
                        .background(imageColor.colorValue)
                )
                Text(text = imageColor.toString())
            }
        }
    }
}