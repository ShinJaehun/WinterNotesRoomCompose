package com.shinjaehun.winternotesroomcompose.domain

object NoteValidator {
    fun validateNote(note: Note): ValidationResult {
        var result = ValidationResult()

        if (note.title.isBlank()) {
            result = result.copy(titleError = "Title can't be empty.")
        }

        if(note.contents.isBlank()) {
            result = result.copy(contentError = "Content can't be empty.")
        }

        return result
    }

    data class ValidationResult(
        val titleError: String? = null,
        val contentError: String? = null
    )
}