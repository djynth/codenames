package core

import player.Player

/**
 * The API through which [Player] implementations access information about the state of the game.
 * So that guessers, in particular, cannot view the actual owners of unrevealed cards, the only
 *  access to the [Board] is provided by the [GameInfo] given to the [Player] on initialization.
 */
class GameInfo(private val game: Game, private val spymaster: Boolean) {
    fun getBoard(): Board {
        return if (spymaster) game.board else game.board.visible()
    }

    fun getHistory(): GameHistory {
        return game.getHistory()
    }
}