package terminal

import core.Clue
import core.GameInfo
import core.Team
import player.Spymaster

class TerminalSpymaster(team: Team, info: GameInfo) : Spymaster(team, info) {
    override fun giveClue(): Clue {
        println()
        println("${teamWithColor(team)}'s turn to give a clue!")
        println()
        println("Press enter to reveal the spymaster board.")
        readLine()
        printBoard(info.getBoard(), true)

        while (true) {
            println("Enter your clue:")
            val hint = readNonemptyLine()
            println("Enter your count:")
            val count = readNonemptyLine().toIntOrNull() ?: continue

            val clue = Clue(hint, count).toLowercase()
            if (clue.valid(info.getBoard(), team)) {
                return clue
            }
        }
    }
}
