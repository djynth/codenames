package core

import player.DummyGuesser
import player.DummySpymaster
import player.Guesser
import player.Spymaster
import java.util.*

// TODO: error returns for invalid clues/guesses

class Game(
        seed: Long = 0,
        private val redSpymaster: Spymaster = DummySpymaster(Team.RED),
        private val blueSpymaster: Spymaster = DummySpymaster(Team.BLUE),
        private val redGuesser: Guesser = DummyGuesser(Team.RED),
        private val blueGuesser: Guesser = DummyGuesser(Team.BLUE)
) {
    private val rand: Random = Random(seed)
    private var currentTeam: Team
    private val history = mutableListOf<Pair<Clue, List<Square>>>()
    val board: Board

    init {
        currentTeam = if (rand.nextBoolean()) Team.RED else Team.BLUE
        board = Board(rand, currentTeam)
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

            // TODO: add each guess to the history as they are given

            val guesses = mutableListOf<Square>()
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

                val correct = when (card.team) {
                    Team.RED, Team.BLUE -> currentTeam == card.team
                    else -> false
                }

                if (!correct) {
                    break
                }
            }

            history.add(Pair(clue, guesses.toList()))

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
