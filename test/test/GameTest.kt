package test

import core.Board
import core.Clue
import core.Game
import core.GameClient
import core.GameInfo
import core.GameListener
import core.Square
import core.Team
import org.junit.jupiter.api.Test
import player.DummyGuesser
import player.DummySpymaster
import player.Guesser
import player.Spymaster
import kotlin.test.assertEquals

class GameTest {
    @Test
    fun testPerfectGuesser() {
        val seed = 13L
        val client = TestClient(seed, { team, info -> PerfectGuesser(team, info, 1) })
        val game = Game(client)
        val listener = TestListener()
        game.addListener(listener)
        (client.redGuesser as PerfectGuesser).board = game.board
        (client.blueGuesser as PerfectGuesser).board = game.board

        val winner = game.play()
        val history = game.getHistory()

        listener.checkHasEnded()

        // since each guesser makes 1 (correct) guess per turn, the second team to go wins
        assertEquals(game.firstTeam().opponent(), winner)
        for (turn in history) {
            assertEquals(1, turn.second.size)
        }
    }

    @Test
    fun testAssassinGuesser() {
        val seed = -20L
        val client = TestClient(seed, { team, info -> AssassinGuesser(team, info) })
        val game = Game(client)
        val listener = TestListener()
        game.addListener(listener)
        (client.redGuesser as AssassinGuesser).board = game.board
        (client.blueGuesser as AssassinGuesser).board = game.board

        val winner = game.play()
        val history = game.getHistory()

        listener.checkHasEnded()

        // since the first team immediately guesses the assassin card, the second team wins
        assertEquals(game.firstTeam().opponent(), winner)
        assertEquals(1, history.size)
    }

    @Test
    fun testIllegalModificationHistory() {
        val seed = 42L
        val game = Game(TestClient(seed, { team, info -> DummyGuesser(team, info) }))
        val listener = TestListener()
        game.addListener(listener)
        game.play()
        listener.checkHasEnded()
        val history = game.getHistory()
        val historySize = history.size
        assert(history.isNotEmpty())

        if (history is MutableList) {
            history.removeAll { true }
        } else {
            val mutableHistory = history.toMutableList()
            mutableHistory.removeAll { true }
        }

        assertEquals(historySize, game.getHistory().size)
    }

    class TestListener : GameListener {
        private var hasStarted = false
        private var hasEnded = false
        override fun onGameStart(game: Game) {
            assert(!hasStarted)
            hasStarted = true
        }

        override fun onGameOver(winner: Team) {
            assert(!hasEnded)
            assert(hasStarted)
            hasEnded = true
        }

        fun checkHasEnded() {
            assert(hasStarted)
            assert(hasEnded)
        }
    }

    class TestClient(
            private val seed: Long,
            private val guesserFactory: (Team, GameInfo) -> Guesser
    ) : GameClient {
        lateinit var blueSpymaster: Spymaster
        lateinit var redSpymaster: Spymaster
        lateinit var blueGuesser: Guesser
        lateinit var redGuesser: Guesser


        override fun seed(): Long {
            return seed
        }

        override fun spymaster(team: Team, info: GameInfo): Spymaster {
            val spymaster = DummySpymaster(team, info)
            when (team) {
                Team.BLUE -> blueSpymaster = spymaster
                Team.RED -> redSpymaster = spymaster
                else -> throw AssertionError()
            }
            return spymaster
        }

        override fun guesser(team: Team, info: GameInfo): Guesser {
            val guesser = guesserFactory(team, info)
            when(team) {
                Team.BLUE -> blueGuesser = guesser
                Team.RED -> redGuesser = guesser
                else -> throw AssertionError()
            }
            return guesser
        }
    }

    class PerfectGuesser(
            team: Team,
            info: GameInfo,
            private val guessesPerTurn: Int = 1
    ) : Guesser(team, info) {
        lateinit var board: Board

        override fun guess(clue: Clue, guessCount: Int): Square? {
            if (guessCount >= guessesPerTurn) {
                return null
            }

            return board.cards().entries.find { !it.value.revealed && it.value.team == team }?.key
        }
    }

    class AssassinGuesser(team: Team, info: GameInfo) : Guesser(team, info) {
        lateinit var board: Board

        override fun guess(clue: Clue, guessCount: Int): Square? {
            return board.cards().entries.find { it.value.team == Team.ASSASSIN }?.key
        }
    }
}