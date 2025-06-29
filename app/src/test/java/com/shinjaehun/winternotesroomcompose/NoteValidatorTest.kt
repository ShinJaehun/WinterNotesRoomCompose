package com.shinjaehun.winternotesroomcompose

import com.shinjaehun.winternotesroomcompose.domain.ImageColor
import com.shinjaehun.winternotesroomcompose.domain.Note
import com.shinjaehun.winternotesroomcompose.domain.NoteValidator
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class NoteValidatorTest {

    private fun currentDate(): String =
        SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(Date())

    @Test
    fun `validateNote returns no errors for valid note`() {
        val note = Note(
            noteId = null,
            title = "Valid title",
            contents = "Some content",
            dateTime = currentDate(),
            imagePath = null,
            thumbnailPath = null,
            color = ImageColor.YELLOW,
            webLink = null
        )

        val result = NoteValidator.validateNote(note)
        assertNull(result.titleError)
        assertNull(result.contentError)
    }

    @Test
    fun `validateNote returns error for blank title`() {
        val note = Note(
            noteId = null,
            title = "   ",
            contents = "Content here",
            dateTime = currentDate(),
            imagePath = null,
            thumbnailPath = null,
            color = ImageColor.YELLOW,
            webLink = null
        )

        val result = NoteValidator.validateNote(note)
        assertNotNull(result.titleError)
        assertNull(result.contentError)
    }

    @Test
    fun `validateNote returns error for blank content`() {
        val note = Note(
            noteId = null,
            title = "Title here",
            contents = "   ",
            dateTime = currentDate(),
            imagePath = null,
            thumbnailPath = null,
            color = ImageColor.YELLOW,
            webLink = null
        )

        val result = NoteValidator.validateNote(note)
        assertNull(result.titleError)
        assertNotNull(result.contentError)
    }

    @Test
    fun `validateNote returns errors for blank title and content`() {
        val note = Note(
            noteId = null,
            title = "   ",
            contents = "   ",
            dateTime = currentDate(),
            imagePath = null,
            thumbnailPath = null,
            color = ImageColor.YELLOW,
            webLink = null
        )

        val result = NoteValidator.validateNote(note)
        assertNotNull(result.titleError)
        assertNotNull(result.contentError)
    }
}