package core

import java.util.*

// TODO: error returns for invalid clues/guesses

/**
 * Represents a single game being played.
 * Note that Players do not have access to the Game object (since then they would be able to access
 *  the full [Board]), only a [GameInfo] proxy.
 */
class Game(client: GameClient) {
    private val rand: Random = Random(client.seed())
    private val history = mutableListOf<Pair<Clue, List<Square>>>()
    private val listeners = mutableListOf<GameListener>()
    val blueSpymaster = client.spymaster(Team.BLUE, GameInfo(this, true))
    val redSpymaster = client.spymaster(Team.RED, GameInfo(this, true))
    val blueGuesser = client.guesser(Team.BLUE, GameInfo(this, false))
    val redGuesser = client.guesser(Team.RED, GameInfo(this, false))
    val firstTeam = if (rand.nextBoolean()) Team.RED else Team.BLUE
    val board = Board(rand, firstTeam)

    /**
     * Runs a single game.
     */
    fun play(): Team {
        listeners.forEach { it.onGameStart(this) }

        var currentTeam = firstTeam
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

                winner = if (card.team == Team.ASSASSIN) {
                    currentTeam.opponent()
                } else {
                    winner()
                }

                if (winner != null || !correct) {
                    break
                }
            }

            currentTeam = currentTeam.opponent()
        }

        listeners.forEach { it.onGameOver(winner) }

        return winner
    }

    /**
     * Gets a copy of the history of the Game.
     */
    fun getHistory(): GameHistory {
        return history.toList()
    }

    /**
     * Adds the given [GameListener] to be notified about future game events.
     */
    fun addListener(listener: GameListener) {
        listeners.add(listener)
    }

    /**
     * Gets the number of cards belonging to the given [Team] (both revealed and unrevealed).
     */
    fun cardsFor(team: Team): Int {
        return if (team == firstTeam) Board.FIRST_TEAM_CARDS else Board.SECOND_TEAM_CARDS
    }

    /**
     * Determines the winner of the Game, i.e. the [Team] with no unrevealed cards, or null if there
     *  is no winner.
     */
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
