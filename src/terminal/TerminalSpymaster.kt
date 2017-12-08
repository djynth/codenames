package terminal

import core.Clue
import core.GameInfo
import core.Team
import player.Spymaster

class TerminalSpymaster(team: Team) : Spymaster(team) {
    override fun giveClue(info: GameInfo): Clue {
        println()
        println("$team's turn to give a clue!")
        println()
        println("Press enter to reveal the spymaster board.")
        readLine()
        printBoard(info.getBoard())
        println("Enter your clue:")
        val clue = readNonemptyLine()
        println("Enter your count:")
        val count = readNonemptyLine().toInt()      // TODO: error checking on number conversion

        // add whitespace to cover the revealed board
        for (i in 1..50) {
            println()
        }

        return Clue(clue, count)
    }
}
