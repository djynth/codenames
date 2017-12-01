package core

data class Square(val row: Int, val col: Int) {
    fun valid(): Boolean {
        return row in 0 until Board.ROWS && col in 0 until Board.COLS
    }
}