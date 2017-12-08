package test

import core.Board
import core.Square
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SquareTest {
    @Test
    fun testValidity() {
        for (row in 1..Board.ROWS) {
            for (col in 1..Board.COLS) {
                assertTrue(Square(row, col).valid())
            }
        }

        assertFalse(Square(0, 0).valid())
        assertFalse(Square(0, 1).valid())
        assertFalse(Square(1, 0).valid())
        assertFalse(Square(1, -1).valid())
        assertFalse(Square(-1, 1).valid())
        assertFalse(Square(Board.ROWS+1, 1).valid())
        assertFalse(Square(1, Board.COLS+1).valid())
    }

    @Test
    fun testValidSquares() {
        assertEquals(Board.totalCards(), Square.validSquares.size)
        assertEquals(Board.totalCards(), Square.validSquares.toSet().size)

        var index = 0
        for (row in 1..Board.ROWS) {
            for (col in 1..Board.COLS) {
                assertEquals(index, Square.validSquares.indexOf(Square(row, col)))
                index++
            }
        }
    }
}
