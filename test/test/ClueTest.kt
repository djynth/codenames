package test

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

    @BeforeEach
    fun init() {
        game = Game(DummyClient(0))
    }

    @Test
    fun testWordsOnBoard() {
        game.board.words().forEach { assertFalse(Clue(it, 1).valid(game, Team.BLUE)) }
        game.board.words().forEach { assertFalse(Clue(it, 1).valid(game, Team.RED)) }
    }

    @Test
    fun testMaxGuesses() {
        for (i in 1..10) {
            assertEquals(i+1, Clue("hint", i).maxGuesses())
        }
    }

    @Test
    fun testCount() {
        assertFalse(Clue("hint", -3).valid(game, Team.BLUE))
        assertFalse(Clue("hint", -3).valid(game, Team.RED))

        assertFalse(Clue("hint", -2).valid(game, Team.BLUE))
        assertFalse(Clue("hint", -2).valid(game, Team.RED))

        assertTrue(Clue("hint", -1).valid(game, Team.BLUE))
        assertTrue(Clue("hint", -1).valid(game, Team.RED))

        assertTrue(Clue("hint", 0).valid(game, Team.BLUE))
        assertTrue(Clue("hint", 0).valid(game, Team.RED))

        assertTrue(Clue("hint", 1).valid(game, Team.BLUE))
        assertTrue(Clue("hint", 1).valid(game, Team.RED))

        assertTrue(Clue("hint", 2).valid(game, Team.BLUE))
        assertTrue(Clue("hint", 2).valid(game, Team.RED))

        for (team in setOf(Team.RED, Team.BLUE)) {
            val max = game.cardsFor(team)
            assertTrue(Clue("hint", max).valid(game, team))
            assertFalse(Clue("hint", max + 1).valid(game, team))
        }
    }

    @Test
    fun testNonEmpty() {
        assertFalse(Clue("", 2).valid(game, Team.BLUE))
    }

    @Test
    fun testOneWord() {
        assertTrue(Clue("oneword", 2).valid(game, Team.BLUE))
        assertFalse(Clue("two words", 2).valid(game, Team.BLUE))
    }

    @Test
    fun testCapitalization() {
        assertTrue(Clue("lowercase", 2).valid(game, Team.BLUE))
        assertFalse(Clue("MixedCase", 2).valid(game, Team.BLUE))
        assertFalse(Clue("UPPERCASE", 2).valid(game, Team.BLUE))
        assertTrue(Clue("MixedCase", 2).toLowercase().valid(game, Team.BLUE))
        assertTrue(Clue("UPPERCASE", 2).toLowercase().valid(game, Team.BLUE))
    }
}
