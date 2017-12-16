package player

import core.Board
import core.Clue
import core.GameInfo
import core.Square
import core.Team
import java.util.*

class DummyGuesser(team: Team, seed: Long = 0) : Guesser(team) {
    private val rand = Random(seed)

    override fun guess(clue: Clue, guessCount: Int, info: GameInfo): Square {
        return Square(rand.nextInt(Board.ROWS) + 1, rand.nextInt(Board.COLS) + 1)
    }
}
