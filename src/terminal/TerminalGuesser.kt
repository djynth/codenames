package terminal

import core.Clue
import core.GameInfo
import core.Square
import core.Team
import player.Guesser

class TerminalGuesser(team: Team, info: GameInfo) : Guesser(team, info) {
    override fun guess(clue: Clue, guessCount: Int): Square? {
        println()
        println("$team's turn to guess!")
        println()
        printBoard(info.getBoard())
        println()
        println("The clue is: \"${clue.hint}\" | ${clue.count}.")

        while (true) {
            println("Input your guess as a word on the board (empty for no guess):")
            val guess = readLine()?.trim()?.toLowerCase() ?: return null
            if (guess.isEmpty()) {
                return null
            }

            val square = info.getBoard().locationOf(guess)
            if (square != null) {
                return square
            }
        }
    }
}
