package core

import player.Player

/**
 * Represents the state of the game as visible by a certain player.
 */
data class GameInfo(private val player: Player, private val game: Game) {
    fun getHistory(): List<Pair<Clue, List<Square>>> {
        return game.getHistory()
    }

    fun getBoard(): Map<Square, Card> {
        return game.board.visibleTo(player)
    }

    /**
     * A convenience function to find the location of the given word on the board, or null if it
     *  does not appear on the board.
     */
    fun locationOf(word: String): Square? {
        val lower = word.toLowerCase()
        return game.board.cards().entries.first { it.value.word == lower }.key
    }
}
