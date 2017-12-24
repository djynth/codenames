package core

/**
 * A single clue given from a team's spymaster to their guesser.
 * A Clue contains a [hint], which must be a single all-lowercase word, and a [count], which
 *  indicates to the guesser the number of cards associated with the hint.
 */
data class Clue(val hint: String, val count: Int) {
    /**
     * Determines whether this Clue is allowed to be given.
     * The formal requirements are:
     *  - The hint must be a single word (i.e. no whitespace) which is not on the board
     *  - The count may be 0, infinity (represented as -1), or a positive number not exceeding the
     *    number of the given team's unrevealed cards on the board
     */
    fun valid(board: Board, team: Team): Boolean {
        if (count < -1) {
            return false
        }

        if (count > board.unrevealed(team).size) {
            return false
        }

        if (!HINT_REGEX.matches(hint)) {
            return false
        }

        // check that the hint is not on the board
        if (board.words().any { Card.wordsEqual(it, hint) }) {
            return false
        }

        // TODO: check that the hint is an English word/name/brand/etc?
        // will need a dictionary of valid hints that AIs can pull from

        return true
    }

    /**
     * The maximum number of guesses allowed to a [Team] after receiving this Clue.
     */
    fun maxGuesses(): Int {
        return count + 1
    }

    /**
     * Creates a copy of this Clue in which the hint is all lowercase.
     * Lowercase Clues are the canonical version, although Clues of any case are considered
     *  equivalent.
     */
    fun toLowercase(): Clue {
        return copy(hint = hint.toLowerCase())
    }

    override fun toString(): String {
        return "\"$hint\" | $count"
    }

    companion object {
        private val HINT_REGEX = Regex("^[a-z]+\$")
    }
}
