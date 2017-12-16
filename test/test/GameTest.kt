package test

import core.Game
import core.Team
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import player.DummyGuesser
import player.DummySpymaster
import kotlin.test.assertEquals

class GameTest {
    private lateinit var dummyGame: Game

    @BeforeEach
    fun init() {
        val seed = 42L
        dummyGame = Game(
                seed,
                DummySpymaster(Team.RED),
                DummySpymaster(Team.BLUE),
                DummyGuesser(Team.RED, seed),
                DummyGuesser(Team.BLUE, seed)
        )
    }

    @Test
    fun testIllegalModificationHistory() {
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
}