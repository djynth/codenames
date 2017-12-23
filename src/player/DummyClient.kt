package player

import core.GameClient
import core.GameInfo
import core.Team

class DummyClient(private val seed: Long) : GameClient {
    override fun seed(): Long {
        return seed
    }

    override fun spymaster(team: Team, info: GameInfo): Spymaster {
        return DummySpymaster(team, info)
    }

    override fun guesser(team: Team, info: GameInfo): Guesser {
        return DummyGuesser(team, info)
    }
}