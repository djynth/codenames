package core

interface GameListener {
    fun onGameStart(game: Game) { }
    fun onGameOver(winner: Team) { }
}