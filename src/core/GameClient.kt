package core

import player.Guesser
import player.Player
import player.Spymaster

/**
 * Provides the [Game] with information and context to run the game.
 * All the [Player]s in the [Game] are created by the GameClient.
 */
interface GameClient {
    /**
     * The random seed to be used in the game.
     * This method will only be called once per [Game].
     */
    fun seed(): Long

    /**
     * Creates a [Spymaster] for the given [Team] with the given [GameInfo].
     * This method will be called once for each playable [Team].
     */
    fun spymaster(team: Team, info: GameInfo): Spymaster

    /**
     * Creates a [Guesser] for the given [Team] with the given [GameInfo].
     * This method will be called once for each playable [Team].
     */
    fun guesser(team: Team, info: GameInfo): Guesser
}
