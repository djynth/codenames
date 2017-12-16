package core

import player.DummyGuesser
import player.DummySpymaster
import player.Guesser
import player.Spymaster
import java.util.*

// TODO: error returns for invalid clues/guesses

class Game(
        seed: Long = 0,
        spymasterFactory: (Team) -> Spymaster = { DummySpymaster(it) },
        guesserFactory: (Team) -> Guesser = { DummyGuesser(it, seed) }
) {
    private val rand: Random = Random(seed)
    private var currentTeam: Team
    private val history = mutableListOf<Pair<Clue, List<Square>>>()
    val firstTeam: Team
    val blueSpymaster: Spymaster
    val redSpymaster: Spymaster
    val blueGuesser: Guesser
    val redGuesser: Guesser
    val board: Board

    init {
        firstTeam = if (rand.nextBoolean()) Team.RED else Team.BLUE
        currentTeam = firstTeam
        board = Board(rand, currentTeam)

        blueSpymaster = spymasterFactory(Team.BLUE)
        redSpymaster = spymasterFactory(Team.RED)
        blueGuesser = guesserFactory(Team.BLUE)
        redGuesser = guesserFactory(Team.RED)
    }

    fun play(): Team {
        var winner: Team? = null
        while (winner == null) {
            val (spymaster, guesser) = when (currentTeam) {
                Team.RED -> Pair(redSpymaster, redGuesser)
                Team.BLUE -> Pair(blueSpymaster, blueGuesser)
                else -> throw IllegalStateException()
            }

            var clue: Clue
            do {
                clue = spymaster.giveClue(GameInfo(spymaster, this))
            } while (!clue.valid(board))

            val guesses = mutableListOf<Square>()
            history.add(Pair(clue, guesses))
            for (guessCount in 0 until clue.maxGuesses()) {
                var guess: Square? = null
                do {
                    guess = guesser.guess(clue, guessCount, GameInfo(guesser, this)) ?: break
                } while (guess?.valid() != true || board.isRevealed(guess))

                if (guess == null) {
                    break
                }

                guesses.add(guess)

                val card = board.reveal(guess)

                if (card.team == Team.ASSASSIN) {
                    winner = currentTeam.opponent()
                    break
                } else {
                    winner = winner()
                    if (winner != null) {
                        break
                    }
                }

                val correct = currentTeam == card.team
                if (!correct) {
                    break
                }
            }

            currentTeam = currentTeam.opponent()
        }

        return winner
    }

    fun getHistory(): List<Pair<Clue, List<Square>>> {
        return history.toList()
    }

    private fun winner(): Team? {
        if (board.unrevealed(Team.RED).isEmpty()) {
            return Team.RED
        }

        if (board.unrevealed(Team.BLUE).isEmpty()) {
            return Team.BLUE
        }

        return null
    }
}
