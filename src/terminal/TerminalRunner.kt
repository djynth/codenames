package terminal

import core.Game
import core.Team

fun main(args: Array<String>) {
    println("Welcome to Codenames!")

    val seed = if (args.isNotEmpty()) args[0].toLong() else 0
    val game = Game(seed, TerminalSpymaster(Team.RED), TerminalSpymaster(Team.BLUE), TerminalGuesser(Team.RED), TerminalGuesser(Team.BLUE))
    val winner = game.play()

    println("$winner wins!")
    println()
    println("Game log:")
    println(game.getHistory())
    println()
    printBoard(game.board.cards())
    println()
    println("Thanks for playing!")
}
