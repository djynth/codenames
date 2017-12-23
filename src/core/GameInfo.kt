package core

class GameInfo(private val game: Game, private val spymaster: Boolean) {
    fun getBoard(): Board {
        return if (spymaster) game.board else game.board.visible()
    }

    fun getHistory(): GameHistory {
        return game.getHistory()
    }
}