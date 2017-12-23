package player

import core.Clue
import core.GameInfo
import core.Team

class DummySpymaster(team: Team, info: GameInfo) : Spymaster(team, info) {
    override fun giveClue(): Clue {
        return Clue("clue", 1)
    }
}
