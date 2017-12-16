package terminal

import core.Game

fun main(args: Array<String>) {
    println("Welcome to Codenames!")

    val seed = if (args.isNotEmpty()) args[0].toLong() else 0
    val game = Game(seed, { TerminalSpymaster(it) }, { TerminalGuesser(it) })
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
