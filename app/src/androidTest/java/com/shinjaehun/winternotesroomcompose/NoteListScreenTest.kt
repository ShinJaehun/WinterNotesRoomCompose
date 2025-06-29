package com.shinjaehun.winternotesroomcompose

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.shinjaehun.winternotesroomcompose.presentation.NoteListScreen
import com.shinjaehun.winternotesroomcompose.presentation.NoteListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var testViewModel: NoteListViewModel

    @Before
    fun setup() {
        val fakeRepository = FakeNoteRepository()
        testViewModel = NoteListViewModel(fakeRepository)
    }

    @Test
    fun clickingFab_opensAddNoteSheet() {
        composeTestRule.setContent {
            NoteListScreen(
                viewModel = testViewModel,
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Add note", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitUntil(
            timeoutMillis = 3_000L,
            condition = {
                composeTestRule.onAllNodes(hasText("Title")).fetchSemanticsNodes().isNotEmpty()
            }
        )

        composeTestRule
            .onNodeWithText("Title")
            .assertIsDisplayed()
    }

}
