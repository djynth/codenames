package core

/**
 * A single clue given from a team's spymaster to their guesser.
 * A Clue contains a hint, which must be a single all-lowercase word, and a count, which indicates
 *  to the guesser the number of cards associated with the hint (but whose only formal requirement
 *  is to be positive).
 */
data class Clue(val hint: String, val count: Int) {
    fun valid(game: Game): Boolean {
        if (count <= 0) {
            return false
        }

        // TODO: could set a max on count of Board.totalCards() to prevent abuse

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
