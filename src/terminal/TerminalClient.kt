package terminal

import core.Card
import core.Clue
import core.Game
import core.GameClient
import core.GameInfo
import core.GameListener
import core.Square
import core.Team
import player.Guesser
import player.Spymaster
import java.util.*

fun main(args: Array<String>) {
    println("Welcome to Codenames!")

    val seedPhrase = if (args.isEmpty()) {
        println()
        println("Enter a number or phrase to use as a random seed (empty for a random seed):")
        val line = readLine()
        if (line.isNullOrBlank()) null else line
    } else {
        args[0]
    }

    val seed = seedPhrase?.hashCode()?.toLong() ?: Random().nextLong()

    val client = TerminalClient(seed)
    val game = Game(client)
    game.addListener(TerminalListener())
    val winner = game.play()

    println("${teamWithColor(winner)} wins!")
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

class TerminalListener : GameListener {
    override fun onClue(clue: Clue, team: Team) {
        println("\r\n".repeat(100))
    }

    override fun onGuess(guess: Square?, card: Card?, correct: Boolean) {
        if (card != null) {
            println()
            println("Revealed ${cardWithColor(card)}")
        }
    }
}
