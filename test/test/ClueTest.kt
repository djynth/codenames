package test

import core.Board
import core.Clue
import core.Team
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClueTest {
    private lateinit var board: Board

    @BeforeEach
    fun init() {
        board = Board(Random(0), Team.RED)
    }

    @Test
    fun testWordsOnBoard() {
        board.words().forEach { assertFalse(Clue(it, 1).valid(board)) }
    }

    @Test
    fun testMaxGuesses() {
        for (i in 0..10) {
            assertEquals(i+1, Clue("hint", i).maxGuesses())
        }
    }

    @Test
    fun testPositiveCount() {
        assertFalse(Clue("hint", -3).valid(board))
        assertFalse(Clue("hint", -2).valid(board))
        assertFalse(Clue("hint", -1).valid(board))
        assertFalse(Clue("hint", 0).valid(board))
        assertTrue(Clue("hint", 1).valid(board))
        assertTrue(Clue("hint", 2).valid(board))
    }

    @Test
    fun testNonEmpty() {
        assertFalse(Clue("", 2).valid(board))
    }

    @Test
    fun testOneWord() {
        assertTrue(Clue("oneword", 2).valid(board))
        assertFalse(Clue("two words", 2).valid(board))
    }
}