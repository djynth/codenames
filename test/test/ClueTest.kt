package test

import core.Clue
import core.Game
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClueTest {
    private lateinit var game: Game

    @BeforeEach
    fun init() {
        game = Game(0)
    }

    @Test
    fun testWordsOnBoard() {
        game.board.words().forEach { assertFalse(Clue(it, 1).valid(game)) }
    }

    @Test
    fun testMaxGuesses() {
        for (i in 1..10) {
            assertEquals(i+1, Clue("hint", i).maxGuesses())
        }
    }

    @Test
    fun testPositiveCount() {
        assertFalse(Clue("hint", -3).valid(game))
        assertFalse(Clue("hint", -2).valid(game))
        assertFalse(Clue("hint", -1).valid(game))
        assertFalse(Clue("hint", 0).valid(game))
        assertTrue(Clue("hint", 1).valid(game))
        assertTrue(Clue("hint", 2).valid(game))
    }

    @Test
    fun testNonEmpty() {
        assertFalse(Clue("", 2).valid(game))
    }

    @Test
    fun testOneWord() {
        assertTrue(Clue("oneword", 2).valid(game))
        assertFalse(Clue("two words", 2).valid(game))
    }

    @Test
    fun testCapitalization() {
        assertTrue(Clue("lowercase", 2).valid(game))
        assertFalse(Clue("MixedCase", 2).valid(game))
        assertFalse(Clue("UPPERCASE", 2).valid(game))
        assertTrue(Clue("MixedCase", 2).toLowercase().valid(game))
        assertTrue(Clue("UPPERCASE", 2).toLowercase().valid(game))
    }
}
