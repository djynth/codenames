package core

/**
 * Represents a single card on the board.
 * If the team of the card is not visible in the current context, it will be null.
 */
data class Card(val word: String, val team: Team?, val revealed: Boolean)