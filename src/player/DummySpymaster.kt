package player

import core.Clue
import core.GameInfo
import core.Team

class DummySpymaster(team: Team): Spymaster(team) {
    override fun giveClue(info: GameInfo): Clue {
        return Clue("clue", 1)
    }
}