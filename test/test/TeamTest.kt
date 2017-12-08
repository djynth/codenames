package test

import core.Team
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TeamTest {
    @Test
    fun testOpponent() {
        assertEquals(Team.RED, Team.BLUE.opponent())
        assertEquals(Team.BLUE, Team.RED.opponent())
        Assertions.assertThrows(IllegalArgumentException::class.java, { Team.ASSASSIN.opponent() })
        Assertions.assertThrows(IllegalArgumentException::class.java, { Team.NEUTRAL.opponent() })
    }
}
