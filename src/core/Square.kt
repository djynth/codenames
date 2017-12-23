package core

data class Square(val row: Int, val col: Int) {
    fun valid(): Boolean {
        return row in 1..Board.ROWS && col in 1..Board.COLS
    }

    fun validGuess(board: Board): Boolean {
        return valid() && !board.isRevealed(this)
    }

    companion object {
        /**
         * All the valid squares on the board, in row-major order.
         */
        val validSquares: List<Square> = (1..Board.ROWS).flatMap { row ->
            (1..Board.COLS).map { col -> Square(row, col) }
        }
    }

    class InvalidSquareException : Exception()
}
