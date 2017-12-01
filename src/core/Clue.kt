package core

/**
 * A single clue given from a team's spymaster to their guesser.
 * A Clue contains a hint, which must be a single word, and a count, which indicates to the guesser
 *  the number of cards associated with the hint (but whose only formal requirement is to be
 *  positive).
 */
data class Clue(val hint: String, val count: Int) {
    fun valid(board: Board): Boolean {
        if (count <= 0) {
            return false
        }

        if (hint.isEmpty()) {
            return false
        }

        if (hint.contains(' ')) {
            return false
        }

        // check that the hint is not on the board
        if (board.words().contains(hint)) {
            return false
        }

        // TODO: check that the hint is an English word
        // TODO: check that the hint has not been used before?

        return true
    }

    fun maxGuesses(): Int {
        return count + 1
    }
}