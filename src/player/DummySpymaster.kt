package player

import core.Clue
import core.Game
import core.Team

class DummySpymaster(team: Team, game: Game): Spymaster(team, game) {
    override fun giveClue(): Clue {
        return Clue("clue", 1)
    }
}