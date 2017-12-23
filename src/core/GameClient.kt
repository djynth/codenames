package core

import player.Guesser
import player.Spymaster

interface GameClient {
    fun seed(): Long

    fun spymaster(team: Team, info: GameInfo): Spymaster
    fun guesser(team: Team, info: GameInfo): Guesser
}
