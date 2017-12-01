package core

import player.Player

/**
 * Represents the state of the game as visible by a certain player.
 */
data class GameInfo(private val player: Player, private val game: Game) {
    fun getHistory(): List<Pair<Clue, List<Square>>> {
        return game.getHistory()
    }

    fun getBoard(): Array<Array<Card>> {
        return game.board.visibleTo(player)
    }

    /**
     * A convenience function to find the location of the given word on the board, or null if it
     *  does not appear on the board.
     */
    fun locationOf(word: String): Square? {
        val lower = word.toLowerCase()
        return getBoard().mapIndexed { row, cards ->
            val col = cards
                    .mapIndexed { col, card -> if (card.word == lower) col else -1 }
                    .firstOrNull { it >= 0 }
            if (col == null) {
                null
            } else {
                Square(row, col)
            }
        }.filterNotNull().firstOrNull()
    }
}