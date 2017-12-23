package core

/**
 * A single clue given from a team's spymaster to their guesser.
 * A Clue contains a hint, which must be a single all-lowercase word, and a count, which indicates
 *  to the guesser the number of cards associated with the hint.
 * Formal clue requirements:
 *  - The hint must be a single (i.e. no whitespace) word which is not on the board
 *  - The count may be 0, infinity (represented as -1), or a positive number up to the number of the
 *    team's unrevealed cards on the board
 */
data class Clue(val hint: String, val count: Int) {
    fun valid(game: Game, team: Team): Boolean {
        if (count < -1) {
            return false
        }

        if (count > game.board.unrevealed(team).size) {
            return false
        }

        if (!hintRegex.matches(hint)) {
            return false
        }

        // check that the hint is not on the board
        if (game.board.words().any { Card.wordsEqual(it, hint) }) {
            return false
        }

        // TODO: check that the hint is an English word/name/brand/etc?
        // will need a dictionary of valid hints that AIs can pull from

        return true
    }

    fun maxGuesses(): Int {
        return count + 1
    }

    fun toLowercase(): Clue {
        return copy(hint = hint.toLowerCase())
    }

    companion object {
        private val hintRegex = Regex("^[a-z]+\$")
    }
}
