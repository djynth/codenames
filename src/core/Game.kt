package core

import java.util.*

// TODO: error returns for invalid clues/guesses

class Game(client: GameClient) {
    private val rand: Random = Random(client.seed())
    private val history = mutableListOf<Pair<Clue, List<Square>>>()
    private val listeners = mutableListOf<GameListener>()
    private val firstTeam = if (rand.nextBoolean()) Team.RED else Team.BLUE
    private var currentTeam = firstTeam
    private val blueSpymaster = client.spymaster(Team.BLUE, GameInfo(this, true))
    private val redSpymaster = client.spymaster(Team.RED, GameInfo(this, true))
    private val blueGuesser = client.guesser(Team.BLUE, GameInfo(this, false))
    private val redGuesser = client.guesser(Team.RED, GameInfo(this, false))
    val board = Board(rand, currentTeam)

    fun play(): Team {
        listeners.forEach { it.onGameStart(this) }

        var winner: Team? = null
        while (winner == null) {
            val (spymaster, guesser) = when (currentTeam) {
                Team.RED -> Pair(redSpymaster, redGuesser)
                Team.BLUE -> Pair(blueSpymaster, blueGuesser)
                else -> throw IllegalStateException()
            }

            var clue: Clue
            do {
                clue = spymaster.giveClue().toLowercase()
            } while (!clue.valid(board, currentTeam))

            listeners.forEach { it.onClue(clue, currentTeam) }

            val guesses = mutableListOf<Square>()
            history.add(Pair(clue, guesses))
            for (guessCount in 0 until clue.maxGuesses()) {
                var guess: Square?
                do {
                    guess = guesser.guess(clue, guessCount)
                } while (guess?.validGuess(board) == false)

                if (guess == null) {
                    listeners.forEach { it.onGuess(guess, null, false) }
                    break
                }

                guesses.add(guess)

                val card = board.reveal(guess)
                val correct = card.team == currentTeam
                listeners.forEach { it.onGuess(guess, card, correct) }

                if (card.team == Team.ASSASSIN) {
                    winner = currentTeam.opponent()
                    break
                } else {
                    winner = winner()
                    if (winner != null) {
                        break
                    }
                }

                if (!correct) {
                    break
                }
            }

            currentTeam = currentTeam.opponent()
        }

        listeners.forEach { it.onGameOver(winner) }

        return winner
    }

    fun getHistory(): GameHistory {
        return history.toList()
    }

    fun addListener(listener: GameListener) {
        listeners.add(listener)
    }

    fun cardsFor(team: Team): Int {
        return if (team == firstTeam) Board.FIRST_TEAM_CARDS else Board.SECOND_TEAM_CARDS
    }

    fun firstTeam(): Team {
        return firstTeam
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
