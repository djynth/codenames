package core

/**
 * Represents the history of a single [Game] as a list of [Clue]s and the [Square] guesses they
 *  generated, with the most recent [Clue]s at the end and the [Square]s in the order they were
 *  guessed.
 */
typealias GameHistory = List<Pair<Clue, List<Square>>>
