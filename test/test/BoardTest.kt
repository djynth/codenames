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
    private lateinit var board1: Board
    private lateinit var board2: Board

    @BeforeEach
    fun init() {
        board1 = Board(Random(0), Team.RED)
        board2 = Board(Random(0), Team.BLUE)
    }

    @Test
    fun testDefaultState() {
        assertEquals(Board.ROWS * Board.COLS, Board.totalCards())

        val cards1 = board1.cards()
        val cards2 = board2.cards()

        for (row in 0 until Board.ROWS) {
            for (col in 0 until Board.COLS) {
                assertFalse(board1.isRevealed(Square(row, col)))
                assertEquals(cards1[row][col].word, cards2[row][col].word)
            }
        }

        assertEquals(Board.FIRST_TEAM_CARDS, board1.unrevealed(Team.RED))
        assertEquals(Board.SECOND_TEAM_CARDS, board1.unrevealed(Team.BLUE))
        assertEquals(Board.ASSASSIN_CARDS, board1.unrevealed(Team.ASSASSIN))
        assertEquals(Board.neutralCards(), board1.unrevealed(Team.NEUTRAL))

        assertEquals(Board.SECOND_TEAM_CARDS, board2.unrevealed(Team.RED))
        assertEquals(Board.FIRST_TEAM_CARDS, board2.unrevealed(Team.BLUE))
        assertEquals(Board.ASSASSIN_CARDS, board2.unrevealed(Team.ASSASSIN))
        assertEquals(Board.neutralCards(), board2.unrevealed(Team.NEUTRAL))

        assertEquals(0, board1.revealed(Team.RED).size)
        assertEquals(0, board1.revealed(Team.BLUE).size)
        assertEquals(0, board1.revealed(Team.ASSASSIN).size)
        assertEquals(0, board1.revealed(Team.NEUTRAL).size)

        assertEquals(Board.totalCards(), board1.words().size)
        assertEquals(Board.totalCards(), board1.cards().map { it.size }.sum())

        assertEquals(board1.words(), board2.words())
    }

    @Test
    fun testIllegalModificationWords() {
        val words = board1.words()
        if (words is MutableSet) {
            words.removeAll(words)
        } else {
            words.toMutableSet().removeAll(words)
        }

        assertEquals(Board.totalCards(), board1.words().size)
    }

    @Test
    fun testIllegalModificationCards() {
        val cards = board1.cards()
        for (i in 0 until cards.size) {
            cards[i] = arrayOf()
        }

        assertEquals(Board.totalCards(), board1.cards().map { it.size }.sum())
    }

}