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

fun printBoard(board: Board, spymaster: Boolean = false) {
    val maxLength = board.words().map { it.length }.max() ?: 0
    val textWidth = ((maxLength + 3) * Board.COLS) + 1

    val sb = StringBuilder()
    for (row in 1..Board.ROWS) {
        repeat(textWidth, { sb.append("-") })
        sb.appendln()
        for (col in 1..Board.COLS) {
            val card = board.cardAt(Square(row, col))
            val totalPadding = maxLength - card.word.length
            val padStart = totalPadding / 2
            val padEnd = totalPadding - padStart
            sb.append("|")
            repeat(padStart + 1, { sb.append(" ") })
            sb.append(cardWithColor(card, spymaster))
            repeat(padEnd + 1, { sb.append(" ") })
        }
        sb.appendln("|")
    }
    repeat(textWidth, { sb.append("-") })

    println(sb)
}

fun teamWithColor(team: Team): String {
    return colorOf(team) + team + ANSI_RESET
}

fun cardWithColor(card: Card, spymaster: Boolean = false): String {
    if (spymaster && card.revealed) {
        return card.word.toUpperCase()
    }
    return colorOf(card.team) + card.word.toUpperCase() + ANSI_RESET
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
