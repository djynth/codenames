package player

import core.Clue
import core.Game
import core.Square
import core.Team

class DummyGuesser(team: Team, game: Game): Guesser(team, game) {
    override fun guess(clue: Clue, guessCount: Int): Square {
        return Square(0, 0)
    }
}