package core

interface GameListener {
    fun onGameStart(game: Game) { }
    fun onGameOver(winner: Team) { }
    fun onClue(clue: Clue, team: Team) { }
    fun onGuess(guess: Square?, card: Card?, correct: Boolean) { }
}