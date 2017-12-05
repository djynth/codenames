package test

import core.Board
import core.Square
import core.Team
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class BoardTest {
    private lateinit var board: Board

    @BeforeEach
    fun init() {
        board = Board(Random(0), Team.RED)
    }

    @Test
    fun testDefaultState() {
        for (row in 0 until Board.ROWS) {
            for (col in 0 until Board.COLS) {
                assertFalse(board.isRevealed(Square(row, col)))
            }
        }

        assertEquals(Board.FIRST_TEAM_CARDS, board.unrevealed(Team.RED))
        assertEquals(Board.SECOND_TEAM_CARDS, board.unrevealed(Team.BLUE))
        assertEquals(Board.ASSASSIN_CARDS, board.unrevealed(Team.ASSASSIN))
        assertEquals(Board.neutralCards(), board.unrevealed(Team.NEUTRAL))

        assertEquals(0, board.revealed(Team.RED).size)
        assertEquals(0, board.revealed(Team.BLUE).size)
        assertEquals(0, board.revealed(Team.ASSASSIN).size)
        assertEquals(0, board.revealed(Team.NEUTRAL).size)

        assertEquals(Board.ROWS * Board.COLS, board.words().size)
    }

    @Test
    fun testIllegalModificationWords() {
        val words = board.words()
        if (words is MutableSet) {
            words.removeAll(words)
        } else {
            words.toMutableSet().removeAll(words)
        }

        assertEquals(Board.ROWS * Board.COLS, board.words().size)
    }

    @Test
    fun testIllegalModificationCards() {
        val cards = board.cards()
        for (i in 0 until cards.size) {
            cards[i] = arrayOf()
        }

        assertEquals(Board.ROWS * Board.COLS, board.cards().map { it.size }.sum())
    }

}