package player

import core.Clue
import core.GameInfo
import core.Square
import core.Team

class DummyGuesser(team: Team): Guesser(team) {
    override fun guess(clue: Clue, guessCount: Int, info: GameInfo): Square {
        return Square(0, 0)
    }
}