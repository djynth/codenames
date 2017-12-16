package test

import core.Board
import core.Clue
import core.Game
import core.GameInfo
import core.Square
import core.Team
import org.junit.jupiter.api.Test
import player.Guesser
import kotlin.test.assertEquals

class GameTest {
    @Test
    fun testPerfectGuesser() {
        val seed = 13L
        val game = Game(seed = seed, guesserFactory = { PerfectGuesser(it, 1) })
        (game.redGuesser as PerfectGuesser).board = game.board
        (game.blueGuesser as PerfectGuesser).board = game.board

        val winner = game.play()
        val history = game.getHistory()

        // since each guesser makes 1 (correct) guess per turn, the second team to go wins
        assertEquals(game.firstTeam.opponent(), winner)
        for (turn in history) {
            assertEquals(1, turn.second.size)
        }
    }

    @Test
    fun testAssassinGuesser() {
        val seed = -20L
        val game = Game(seed = seed, guesserFactory = { AssassinGuesser(it) })
        (game.redGuesser as AssassinGuesser).board = game.board
        (game.blueGuesser as AssassinGuesser).board = game.board

        val winner = game.play()
        val history = game.getHistory()

        // since the first team immediately guesses the assassin card, the second team wins
        assertEquals(game.firstTeam.opponent(), winner)
        assertEquals(1, history.size)
    }

    @Test
    fun testIllegalModificationHistory() {
        val seed = 42L
        val dummyGame = Game(seed)
        dummyGame.play()
        val history = dummyGame.getHistory()
        val historySize = history.size
        assert(history.isNotEmpty())

        if (history is MutableList) {
            history.removeAll { true }
        } else {
            val mutableHistory = history.toMutableList()
            mutableHistory.removeAll { true }
        }

        assertEquals(historySize, dummyGame.getHistory().size)
    }

    class PerfectGuesser(
            team: Team,
            private val guessesPerTurn: Int = 1
    ) : Guesser(team) {
        lateinit var board: Board

        override fun guess(clue: Clue, guessCount: Int, info: GameInfo): Square? {
            if (guessCount >= guessesPerTurn) {
                return null
            }

            return board.cards().entries.find { !it.value.revealed && it.value.team == team }?.key
        }
    }

    class AssassinGuesser(team: Team) : Guesser(team) {
        lateinit var board: Board

        override fun guess(clue: Clue, guessCount: Int, info: GameInfo): Square? {
            return board.cards().entries.find { it.value.team == Team.ASSASSIN }?.key
        }
    }
}