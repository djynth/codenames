package core

/**
 * Represents a single square on the board, in which a [Card] can be placed.
 * The cards are arranged in a two dimensional grid, with the rows and columns indexed from 1 up to
 *  [Board.ROWS] and [Board.COLS].
 */
data class Square(val row: Int, val col: Int) {
    /**
     * Determines whether this is a valid Square on the [Board], i.e. the [row] and [col] are
     *  between 1 and [Board.ROWS] or [Board.COLS] (inclusive), respectively.
     */
    fun valid(): Boolean {
        return row in 1..Board.ROWS && col in 1..Board.COLS
    }

    /**
     * Determines whether this Square is a valid guess to reveal a card on the given [Board], i.e.
     *  the Square is valid per [valid] and has not been revealed on the given [Board].
     */
    fun validGuess(board: Board): Boolean {
        return valid() && !board.isRevealed(this)
    }

    override fun toString(): String {
        return "[$row,$col]"
    }

    companion object {
        /**
         * All the valid Squares on the board, in row-major order.
         */
        val validSquares: List<Square> = (1..Board.ROWS).flatMap { row ->
            (1..Board.COLS).map { col -> Square(row, col) }
        }
    }

    class InvalidSquareException : Exception()
}
