package player

import core.Clue
import core.GameInfo
import core.Square
import core.Team

sealed class Player(val team: Team)

abstract class Spymaster(team: Team) : Player(team) {
    /**
     * Generates a Clue for this team's Guesser on this turn.
     *
     * @param info the game state as visible to this Spymaster
     */
    abstract fun giveClue(info: GameInfo): Clue
}

abstract class Guesser(team: Team) : Player(team) {
    /**
     * Produces a Square as the next guess for the given Clue.
     * This function will be called multiple times (until null is returned, a guess is incorrect,
     *  the game has ended, or the clue's max guesses has been reached) with the same Clue to
     *  generate a sequence of guesses.
     * To stop guessing, return null.
     *
     * @param clue       the Clue given by this team's Spymaster on this turn
     * @param guessCount the number of guesses already made; i.e. 0 for the first guess, 1 for the
     *                   second, and so on
     * @param info       the game state as visible to this Guesser
     */
    abstract fun guess(clue: Clue, guessCount: Int, info: GameInfo): Square?
}