package core

/**
 * A single clue given from a team's spymaster to their guesser.
 * A Clue contains a hint, which must be a single word, and a count, which indicates to the guesser
 *  the number of cards associated with the hint (but whose only formal requirement is to be
 *  positive).
 */
data class Clue(val hint: String, val count: Int) {
    fun valid(): Boolean {
        if (count <= 0) {
            return false
        }

        if (hint.isEmpty()) {
            return false
        }

        if (hint.contains(' ')) {
            return false
        }

        // TODO: check that the hint is an English word

        return true
    }

    fun maxGuesses(): Int {
        return count + 1
    }
}