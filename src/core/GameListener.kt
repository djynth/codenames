package core

/**
 * Captures game events after being registered via [Game.addListener].
 */
interface GameListener {
    /**
     * Invoked when the [Game] begins.
     */
    fun onGameStart(game: Game) { }

    /**
     * Invoked when the [Game] ends.
     */
    fun onGameOver(winner: Team) { }

    /**
     * Invoked when a [Clue] is given by a spymaster.
     */
    fun onClue(clue: Clue, team: Team) { }

    /**
     * Invoked when a guess is given by a guesser.
     */
    fun onGuess(guess: Square?, card: Card?, correct: Boolean) { }
}