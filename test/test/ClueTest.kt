package test

import core.Board
import core.Clue
import core.Game
import core.Team
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import player.DummyClient
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClueTest {
    private lateinit var game: Game
    private lateinit var board: Board

    @BeforeEach
    fun init() {
        game = Game(DummyClient(0))
        board = game.board
    }

    @Test
    fun testWordsOnBoard() {
        board.words().forEach { assertFalse(Clue(it, 1).valid(board, Team.BLUE)) }
        board.words().forEach { assertFalse(Clue(it, 1).valid(board, Team.RED)) }
    }

    @Test
    fun testMaxGuesses() {
        for (i in 1..10) {
            assertEquals(i+1, Clue("hint", i).maxGuesses())
        }
    }

    @Test
    fun testCount() {
        assertFalse(Clue("hint", -3).valid(board, Team.BLUE))
        assertFalse(Clue("hint", -3).valid(board, Team.RED))

        assertFalse(Clue("hint", -2).valid(board, Team.BLUE))
        assertFalse(Clue("hint", -2).valid(board, Team.RED))

        assertTrue(Clue("hint", -1).valid(board, Team.BLUE))
        assertTrue(Clue("hint", -1).valid(board, Team.RED))

        assertTrue(Clue("hint", 0).valid(board, Team.BLUE))
        assertTrue(Clue("hint", 0).valid(board, Team.RED))

        assertTrue(Clue("hint", 1).valid(board, Team.BLUE))
        assertTrue(Clue("hint", 1).valid(board, Team.RED))

        assertTrue(Clue("hint", 2).valid(board, Team.BLUE))
        assertTrue(Clue("hint", 2).valid(board, Team.RED))

        for (team in setOf(Team.RED, Team.BLUE)) {
            val max = game.cardsFor(team)
            assertTrue(Clue("hint", max).valid(board, team))
            assertFalse(Clue("hint", max + 1).valid(board, team))
        }
    }

    @Test
    fun testNonEmpty() {
        assertFalse(Clue("", 2).valid(board, Team.BLUE))
    }

    @Test
    fun testOneWord() {
        assertTrue(Clue("oneword", 2).valid(board, Team.BLUE))
        assertFalse(Clue("two words", 2).valid(board, Team.BLUE))
    }

    @Test
    fun testCapitalization() {
        assertTrue(Clue("lowercase", 2).valid(board, Team.BLUE))
        assertFalse(Clue("MixedCase", 2).valid(board, Team.BLUE))
        assertFalse(Clue("UPPERCASE", 2).valid(board, Team.BLUE))
        assertTrue(Clue("MixedCase", 2).toLowercase().valid(board, Team.BLUE))
        assertTrue(Clue("UPPERCASE", 2).toLowercase().valid(board, Team.BLUE))
    }
}
