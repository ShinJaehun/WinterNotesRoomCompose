package com.shinjaehun.winternotesroomcompose

import com.shinjaehun.winternotesroomcompose.domain.INoteRepository
import com.shinjaehun.winternotesroomcompose.domain.ImageColor
import com.shinjaehun.winternotesroomcompose.domain.Note
import com.shinjaehun.winternotesroomcompose.presentation.NoteListEvent
import com.shinjaehun.winternotesroomcompose.presentation.NoteListState
import com.shinjaehun.winternotesroomcompose.presentation.NoteListViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteListViewModelTest {

    private lateinit var repository: INoteRepository
    private lateinit var viewModel: NoteListViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk<INoteRepository>()
        every { repository.getNotes() } returns flowOf(emptyList())
        coEvery { repository.insertNote(any(), any()) } just Runs
        coEvery { repository.deleteNote(any()) } just Runs

        viewModel = NoteListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEvent OnAddNewNoteClick initializes newNote`() = runTest {
        viewModel.onEvent(NoteListEvent.OnAddNewNoteClick)
        testDispatcher.scheduler.advanceUntilIdle()

        val note = viewModel.newNote
        assertNotNull(note)
        assertEquals("", note?.title)
        assertEquals("", note?.contents)
    }

    @Test
    fun `onEvent OnTitleChanged updates newNote title`() = runTest {
        viewModel.onEvent(NoteListEvent.OnAddNewNoteClick)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(NoteListEvent.OnTitleChanged("Updated Title"))
        assertEquals("Updated Title", viewModel.newNote?.title)
    }

//    @Test
//    fun `onEvent OnImagePicked updates tempImageBytes and clears imagePath`() = runTest {
//        viewModel.onEvent(NoteListEvent.OnAddNewNoteClick)
//        testDispatcher.scheduler.advanceUntilIdle() // newNote 초기화 기다림
//
//        val testBytes = "test".toByteArray()
//        viewModel.onEvent(NoteListEvent.OnImagePicked(testBytes))
//        testDispatcher.scheduler.runCurrent() // state 반영 기다림
//
//        val state = viewModel.state.value
//        assertEquals(testBytes.toList(), state.tempImageBytes?.toList())
//        assertNull(viewModel.newNote?.imagePath)
//        assertNull(viewModel.newNote?.thumbnailPath)
//    }

//    @Test
//    fun `onEvent OnImagePicked updates tempImageBytes and clears imagePath`() = runTest {
//        val testBytes = "test".toByteArray()
//        viewModel.onEvent(NoteListEvent.OnImagePicked(testBytes))
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        val state = viewModel.state.value
//        assertEquals(testBytes.toList(), state.tempImageBytes?.toList())
//        assertNull(viewModel.newNote?.imagePath)
//        assertNull(viewModel.newNote?.thumbnailPath)
//    }

//    @Test
//    fun `onEvent OnImagePicked updates tempImageBytes and clears imagePath`() = runTest {
//        viewModel.onEvent(NoteListEvent.OnAddNewNoteClick)
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        val testBytes = "test".toByteArray()
//        viewModel.onEvent(NoteListEvent.OnImagePicked(testBytes))
//
//        // ⚠️ 여기 한 번 더 advanceUntilIdle()을 호출해야 state update가 끝나
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        val state = viewModel.state.value
//        assertEquals(testBytes.toList(), state.tempImageBytes?.toList())  // <- 이제 통과 가능
//        assertNull(viewModel.newNote?.imagePath)
//        assertNull(viewModel.newNote?.thumbnailPath)
//    }

//    @Test
//    fun `onEvent OnImagePicked updates tempImageBytes and clears imagePath`() = runTest {
//        viewModel.onEvent(NoteListEvent.OnAddNewNoteClick)
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        val testBytes = "test".toByteArray()
//        viewModel.onEvent(NoteListEvent.OnImagePicked(testBytes))
//        testDispatcher.scheduler.runCurrent()
//
//        // ✅ 핵심: combine이 적용된 `state` 말고, 내부 상태 `_state`를 확인해야 함
////        val internalState = viewModel
////            .javaClass
////            .getDeclaredField("_state")
////            .apply { isAccessible = true }
////            .get(viewModel) as MutableStateFlow<*>
//
//        val internalState = viewModel.getInternalState()
//        assertEquals(testBytes.toList(), internalState.tempImageBytes?.toList())
//
//        val value = internalState
//            .javaClass
//            .getMethod("getValue")
//            .invoke(internalState) as NoteListState
//
//        assertEquals(testBytes.toList(), value.tempImageBytes?.toList())
//        assertNull(viewModel.newNote?.imagePath)
//        assertNull(viewModel.newNote?.thumbnailPath)
//    }

    @Test
    fun `onEvent OnImagePicked updates tempImageBytes and clears imagePath`() = runTest {
        val testBytes = "test".toByteArray()

        // Note가 아직 null일 수 있으므로 이미지 이벤트 먼저 보내고 초기화 확인
        viewModel.onEvent(NoteListEvent.OnImagePicked(testBytes))
        testDispatcher.scheduler.runCurrent() // 이벤트 반영

        val state = viewModel.getInternalState()
        val note = viewModel.getNewNoteForTest()

        // tempImageBytes 가 정확히 반영되었는지 확인
        assertEquals(testBytes.toList(), state.tempImageBytes?.toList())

        // newNote가 자동 생성되었는지, 그리고 imagePath/thumbnailPath 초기화 되었는지 확인
        assertNotNull(note)
        assertNull(note?.imagePath)
        assertNull(note?.thumbnailPath)
    }

    @Test
    fun `onEvent SaveNote triggers insertNote when valid`() = runTest {
        viewModel.onEvent(NoteListEvent.OnAddNewNoteClick)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.updateNewNoteForTest { it?.copy(title = "T", contents = "C") }
        viewModel.onEvent(NoteListEvent.SaveNote)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify {
            repository.insertNote(match {
                it.title == "T" && it.contents == "C"
            }, null)
        }
    }

    @Test
    fun `onEvent DeleteNote triggers deleteNote with correct id`() = runTest {
        val note = Note(
            noteId = 100L,
            title = "T",
            contents = "C",
            dateTime = "Now",
            imagePath = null,
            thumbnailPath = null,
            color = ImageColor.WHITE,
            webLink = null
        )

        viewModel.onEvent(NoteListEvent.SelectNote(note))
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(NoteListEvent.DeleteNote)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.deleteNote(100L) }
    }
}
