package terminal

import core.Game
import core.GameClient
import core.GameInfo
import core.Team
import player.Guesser
import player.Spymaster
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

    val client = TerminalClient(seed)
    val game = Game(client)
    val winner = game.play()

    println("$winner wins!")
    println()
    println("Game log:")
    println(game.getHistory())
    println()
    printBoard(game.board)
    println()
    println("Thanks for playing!")
}

class TerminalClient(private val seed: Long) : GameClient {
    override fun seed(): Long {
        return seed
    }

    override fun spymaster(team: Team, info: GameInfo): Spymaster {
        return TerminalSpymaster(team, info)
    }

    override fun guesser(team: Team, info: GameInfo): Guesser {
        return TerminalGuesser(team, info)
    }
}
