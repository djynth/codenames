package terminal

import core.Game
import java.util.*

fun main(args: Array<String>) {
    println("Welcome to Codenames!")

    val seedPhrase = if (args.isEmpty()) {
        println()
        println("Enter a number or phrase to use as a random seed (empty for a random seed):")
        readLine()
    } else {
        args[0]
    }

    val seed = if (seedPhrase == null || seedPhrase.isEmpty()) {
        Random().nextLong()
    } else {
        seedPhrase.hashCode().toLong()
    }

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
