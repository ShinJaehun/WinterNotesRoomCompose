package com.shinjaehun.winternotesroomcompose

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.shinjaehun.winternotesroomcompose.data.NoteDatabase
import com.shinjaehun.winternotesroomcompose.data.NoteRepositoryImpl
import com.shinjaehun.winternotesroomcompose.domain.ImageColor
import com.shinjaehun.winternotesroomcompose.domain.Note
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteRepositoryTest {

    private lateinit var fakeDao: FakeNoteDao
    private lateinit var repository: NoteRepositoryImpl

    @Before
    fun setup() {
        fakeDao = FakeNoteDao()
        val fakeImageStorage = FakeImageStorage()
        repository = NoteRepositoryImpl(fakeDao, fakeImageStorage)
    }

    @Test
    fun insertNote_withoutImage_savesCorrectly() = runTest {
        val note = Note(
            noteId = null,
            title = "Test",
            contents = "Content",
            dateTime = "2025-06-26",
            imagePath = null,
            thumbnailPath = null,
            color = ImageColor.YELLOW,
            webLink = null
        )

        repository.insertNote(note, null)
        val result = repository.getNotes().first()

        assertEquals(1, result.size)
        assertEquals("Test", result.first().title)
    }

    @Test
    fun insertNote_withImage_savesImagePath() = runTest {
        val imageBytes = "imageData".toByteArray()
        val note = Note(
            noteId = null,
            title = "With Image",
            contents = "Has image",
            dateTime = "2025-06-26",
            imagePath = null,
            thumbnailPath = null,
            color = ImageColor.GREEN,
            webLink = null
        )

        repository.insertNote(note, imageBytes)
        val result = repository.getNotes().first().first()

        assertEquals("With Image", result.title)
        assertEquals("fake/original/path.jpg", result.imagePath)
        assertEquals("fake/thumbnail/path.jpg", result.thumbnailPath)
    }

    @Test
    fun deleteNote_removesItFromDatabase() = runTest {
        val note = Note(
            noteId = 1,
            title = "To Delete",
            contents = "bye",
            dateTime = "2025-06-26",
            imagePath = null,
            thumbnailPath = null,
            color = ImageColor.GREEN,
            webLink = null
        )

        repository.insertNote(note, null)
        val inserted = repository.getNotes().first().first()

        repository.deleteNote(inserted.noteId!!)
        val notesAfterDelete = repository.getNotes().first()

        assertEquals(0, notesAfterDelete.size)
    }
}
