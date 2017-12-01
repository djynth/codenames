package player

import core.Clue
import core.Game
import core.Square
import core.Team

sealed class Player(val team: Team, val game: Game)

abstract class Spymaster(team: Team, game: Game) : Player(team, game) {
    /**
     * Generates a Clue for this team's Guesser on this turn.
     */
    abstract fun giveClue(): Clue
}

abstract class Guesser(team: Team, game: Game) : Player(team, game) {
    /**
     * Produces a Square as the next guess for the given Clue.
     * To stop guessing, return null.
     *
     * @param clue       the Clue given by this team's Spymaster on this turn
     * @param guessCount the number of guesses already made; i.e. 0 for the first guess, 1 for the
     *                   second, and so on
     */
    abstract fun guess(clue: Clue, guessCount: Int): Square?
}
