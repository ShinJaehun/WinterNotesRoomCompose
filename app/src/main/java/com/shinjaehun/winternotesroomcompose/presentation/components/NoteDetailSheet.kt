package com.shinjaehun.winternotesroomcompose.presentation.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shinjaehun.winternotesroomcompose.data.ImageStorage
import com.shinjaehun.winternotesroomcompose.domain.Note
import com.shinjaehun.winternotesroomcompose.presentation.NoteListEvent

@Composable
fun NoteDetailSheet(
    isOpen: Boolean,
    selectedNote: Note?,
    onEvent: (NoteListEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val imageStorage = remember { ImageStorage(context) }

    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }

    // 이미지 비동기 로드
    LaunchedEffect(selectedNote?.imagePath) {
        imageBytes = selectedNote?.imagePath?.let { path ->
            imageStorage.getImage(path)
        }
    }

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
                NoteImage(
                    title = selectedNote?.title,
//                    imageBytes = selectedNote?.imageBytes,
                    imageBytes = imageBytes,
                    iconSize = 50.dp,
                    modifier = Modifier
                        .size(150.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = selectedNote?.title.toString() ,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
                EditRow(
                    onEditClick = {
                        selectedNote?.let {
                            onEvent(NoteListEvent.EditNote(it))
                        }
                    },
                    onDeleteClick = {
                        onEvent(NoteListEvent.DeleteNote)
                    }
                )
                Spacer(Modifier.height(16.dp))
                NoteInfoSection(
                    title = "Contents",
                    value = selectedNote?.contents ?: "",
                    icon = null,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
                NoteInfoSection(
                    title = "Web Link",
                    value = selectedNote?.webLink ?: "",
                    icon = Icons.Default.Home,
                    modifier = Modifier.fillMaxWidth()
                )
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
fun EditRow(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        FilledTonalIconButton(
            onClick = onEditClick,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Icon(
               imageVector = Icons.Rounded.Edit,
                contentDescription = "Edit note"
            )
        }
        FilledTonalIconButton(
            onClick = onDeleteClick,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Delete note"
            )
        }
    }
}

@Composable
fun NoteInfoSection(
    title: String,
    value: String,
    icon: ImageVector?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 12.sp
            )
            Text(
                text = value,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )
        }
    }
}