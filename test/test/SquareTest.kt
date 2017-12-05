package test

import core.Board
import core.Square
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SquareTest {

    @Test
    fun testValidity() {
        for (row in 0 until Board.ROWS) {
            for (col in 0 until Board.COLS) {
                assertTrue(Square(row, col).valid())
            }
        }

        assertFalse(Square(-1, 0).valid())
        assertFalse(Square(0, -1).valid())
        assertFalse(Square(Board.ROWS, 0).valid())
        assertFalse(Square(0, Board.COLS).valid())
    }
}