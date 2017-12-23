package test

import core.Board
import core.Square
import core.Team
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BoardTest {
    private lateinit var board1: Board
    private lateinit var board2: Board

    @BeforeEach
    fun init() {
        board1 = Board(Random(0), Team.RED)
        board2 = Board(Random(0), Team.BLUE)
    }

    @Test
    fun testDefaultState() {
        assertEquals(Board.ROWS * Board.COLS, Board.TOTAL_CARDS)

        val cards1 = board1.cards()
        val cards2 = board2.cards()

        for (square in Square.validSquares) {
            assertFalse(board1.isRevealed(square))
            assertEquals(cards1[square]?.word, cards2[square]?.word)
        }

        assertEquals(Board.FIRST_TEAM_CARDS, board1.unrevealed(Team.RED).size)
        assertEquals(Board.SECOND_TEAM_CARDS, board1.unrevealed(Team.BLUE).size)
        assertEquals(Board.ASSASSIN_CARDS, board1.unrevealed(Team.ASSASSIN).size)
        assertEquals(Board.NEUTRAL_CARDS, board1.unrevealed(Team.NEUTRAL).size)

        assertEquals(Board.SECOND_TEAM_CARDS, board2.unrevealed(Team.RED).size)
        assertEquals(Board.FIRST_TEAM_CARDS, board2.unrevealed(Team.BLUE).size)
        assertEquals(Board.ASSASSIN_CARDS, board2.unrevealed(Team.ASSASSIN).size)
        assertEquals(Board.NEUTRAL_CARDS, board2.unrevealed(Team.NEUTRAL).size)

        assertEquals(0, board1.revealed(Team.RED).size)
        assertEquals(0, board1.revealed(Team.BLUE).size)
        assertEquals(0, board1.revealed(Team.ASSASSIN).size)
        assertEquals(0, board1.revealed(Team.NEUTRAL).size)

        assertEquals(Board.TOTAL_CARDS, board1.words().size)
        assertEquals(Board.TOTAL_CARDS, board1.cards().size)

        assertEquals(board1.words(), board2.words())
    }

    @Test
    fun testVisible() {
        val visible = board1.visible()
        for (square in Square.validSquares) {
            assertFalse(visible.cardAt(square).revealed)
            assertFalse(visible.isRevealed(square))
        }

        assertTrue(visible.unrevealed(Team.BLUE).isEmpty())
        assertTrue(visible.unrevealed(Team.RED).isEmpty())
        assertTrue(visible.unrevealed(Team.ASSASSIN).isEmpty())
        assertTrue(visible.unrevealed(Team.NEUTRAL).isEmpty())

        assertFalse(board1.unrevealed(Team.BLUE).isEmpty())
        assertFalse(board1.unrevealed(Team.RED).isEmpty())
        assertFalse(board1.unrevealed(Team.ASSASSIN).isEmpty())
        assertFalse(board1.unrevealed(Team.NEUTRAL).isEmpty())
    }

    @Test
    fun testIllegalModificationWords() {
        val words = board1.words()
        if (words is MutableSet) {
            words.removeAll(words)
        } else {
            words.toMutableSet().removeAll(words)
        }

        assertEquals(Board.TOTAL_CARDS, board1.words().size)
    }

    @Test
    fun testIllegalModificationCards() {
        val cards = board1.cards()
        if (cards is MutableMap) {
            for (square in Square.validSquares) {
                cards.remove(square)
            }
        } else {
            val mutableCards = cards.toMutableMap()
            for (square in Square.validSquares) {
                mutableCards.remove(square)
            }
        }

        assertEquals(Board.TOTAL_CARDS, board1.cards().size)
    }
}
