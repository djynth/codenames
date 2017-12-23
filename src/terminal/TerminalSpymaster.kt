package terminal

import core.Clue
import core.GameInfo
import core.Team
import player.Spymaster

class TerminalSpymaster(team: Team, info: GameInfo) : Spymaster(team, info) {
    override fun giveClue(): Clue {
        println()
        println("$team's turn to give a clue!")
        println()
        println("Press enter to reveal the spymaster board.")
        readLine()
        printBoard(info.getBoard())
        println("Enter your clue:")
        val clue = readNonemptyLine()
        println("Enter your count:")
        val count = readNonemptyLine().toIntOrNull() ?: 0

        // add whitespace to cover the revealed board
        for (i in 1..50) {
            println()
        }

        return Clue(clue, count)
    }
}
