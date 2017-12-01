package core

import player.DummyGuesser
import player.DummySpymaster
import player.Guesser
import player.Spymaster
import java.util.*

// TODO: add game history

class Game(seed: Long = 0) {
    val rand: Random = Random(seed)
    val teamCounts: Map<Team, Int>
    private var currentTeam: Team
    private val board: Board

    private val redSpymaster: Spymaster = DummySpymaster(Team.RED, this)
    private val blueSpymaster: Spymaster = DummySpymaster(Team.BLUE, this)
    private val redGuesser: Guesser = DummyGuesser(Team.RED, this)
    private val blueGuesser: Guesser = DummyGuesser(Team.BLUE, this)

    init {
        val first = if (rand.nextBoolean()) Team.RED else Team.BLUE
        currentTeam = first
        teamCounts = mapOf(
                Pair(Team.RED,  if (first == Team.RED) FIRST_TEAM_CARDS else SECOND_TEAM_CARDS),
                Pair(Team.BLUE, if (first == Team.BLUE) FIRST_TEAM_CARDS else SECOND_TEAM_CARDS),
                Pair(Team.ASSASSIN, ASSASSIN_CARDS),
                Pair(Team.NEUTRAL, neutralCards())
        )

        board = Board(this)
    }

    fun play(): Team {
        while (true) {
            val (spymaster, guesser) = when (currentTeam) {
                Team.RED -> Pair(redSpymaster, redGuesser)
                Team.BLUE -> Pair(blueSpymaster, blueGuesser)
                else -> throw IllegalStateException()
            }

            var clue: Clue
            do {
                clue = spymaster.giveClue()
            } while (!clue.valid())

            for (guessCount in 0 until clue.maxGuesses()) {
                var guess: Square? = null
                do {
                    guess = guesser.guess(clue, guessCount) ?: break
                } while (guess?.valid() != true || board.isRevealed(guess))

                if (guess == null) {
                    break
                }

                val card = board.reveal(guess)

                if (card.team == Team.ASSASSIN) {
                    return currentTeam.opponent()
                }

                val winner = winner()
                if (winner != null) {
                    return winner
                }

                val correct = when (card.team) {
                    Team.RED, Team.BLUE -> currentTeam == card.team
                    else -> false
                }

                if (!correct) {
                    break
                }
            }

            currentTeam = currentTeam.opponent()
        }
    }

    private fun winner(): Team? {
        if (board.unrevealed(Team.RED) == 0) {
            return Team.RED
        }

        if (board.unrevealed(Team.BLUE) == 0) {
            return Team.BLUE
        }

        return null
    }

    companion object {
        const val FIRST_TEAM_CARDS = 9
        const val SECOND_TEAM_CARDS = 8
        const val ASSASSIN_CARDS = 1

        fun neutralCards(): Int {
            return Board.totalCards() - FIRST_TEAM_CARDS - SECOND_TEAM_CARDS - ASSASSIN_CARDS
        }
    }
}
