package terminal

import core.Board
import core.Card
import core.Square
import core.Team

const val ANSI_RESET = "\u001B[0m"
const val ANSI_BLACK = "\u001B[30m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_YELLOW = "\u001B[33m"
const val ANSI_BLUE = "\u001B[34m"
const val ANSI_WHITE = "\u001B[37m"

fun readNonemptyLine(): String {
    while (true) {
        val line = readLine()?.trim()?.toLowerCase()
        if (line != null && !line.isEmpty()) {
            return line
        }
    }
}

fun printBoard(board: Map<Square, Card>) {
    val maxLength = board.values.map { it.word.length }.max() ?: 0
    val textWidth = ((maxLength + 3) * Board.COLS) + 1

    val sb = StringBuilder()
    for (row in 1..Board.ROWS) {
        repeat(textWidth, { sb.append("-") })
        sb.appendln()
        for (col in 1..Board.COLS) {
            val card = board[Square(row, col)] ?: throw Square.InvalidSquareException()
            val totalPadding = maxLength - card.word.length
            val padStart = totalPadding / 2
            val padEnd = totalPadding - padStart
            sb.append("|")
            repeat(padStart + 1, { sb.append(" ") })
            sb.append(colorOf(card.team))
            sb.append(card.word.toUpperCase())
            sb.append(ANSI_RESET)
            repeat(padEnd + 1, { sb.append(" ") })
        }
        sb.appendln("|")
    }
    repeat(textWidth, { sb.append("-") })

    println(sb)
}

private fun colorOf(team: Team?): String {
    return when(team) {
        Team.RED -> ANSI_RED
        Team.BLUE -> ANSI_BLUE
        Team.NEUTRAL -> ANSI_YELLOW
        Team.ASSASSIN -> ANSI_BLACK
        null -> ANSI_WHITE
    }
}
