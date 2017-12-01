package core

import dictionary.CodeWords
import extractByCount
import player.Player
import player.Spymaster
import java.util.*

class Board(rand: Random, first: Team) {
    // cards[i][j] = card at row i and column j
    // 0 <= i < ROWS; 0 <= j < COLS
    private val cards: Array<Array<Card>>

    init {
        val dict = CodeWords()
        val words = dict.pickWords(totalCards(), rand).map { it.toLowerCase() }
        val teamCounts = mapOf(
                Pair(Team.RED,  if (first == Team.RED) FIRST_TEAM_CARDS else SECOND_TEAM_CARDS),
                Pair(Team.BLUE, if (first == Team.BLUE) FIRST_TEAM_CARDS else SECOND_TEAM_CARDS),
                Pair(Team.ASSASSIN, Board.ASSASSIN_CARDS),
                Pair(Team.NEUTRAL, Board.neutralCards())
        )
        val teams = teamCounts.extractByCount(rand)
        var index = -1
        cards = Array(ROWS, { Array(COLS, {
            index++
            Card(words[index], teams[index], false)
        }) })
    }

    fun isRevealed(square: Square): Boolean {
        return cards[square.row][square.col].revealed
    }

    fun reveal(square: Square): Card {
        val card = cards[square.row][square.col]

        if (card.revealed) {
            throw IllegalStateException()
        }

        val revealed = card.copy(revealed = true)
        cards[square.row][square.col] = revealed
        return revealed
    }

    // the number of unrevealed cards for the given team
    fun unrevealed(team: Team): Int {
        return cards.map { it.count { !it.revealed && it.team == team } }.sum()
    }

    fun revealed(team: Team): Collection<Card> {
        return cards.flatMap { it.filter { it.revealed && it.team == team } }
    }

    fun cards(): Array<Array<Card>> {
        // TODO: clone?
        return cards
    }

    fun visibleTo(player: Player): Array<Array<Card>> {
        return Array(ROWS, { row -> Array(COLS, { col ->
            val card = cards[row][col]
            val visible = player is Spymaster || card.revealed
            card.copy(team = if (visible) card.team else null)
        }) })
    }

    fun words(): Set<String> {
        return cards.flatMap { it.map { it.word } }.toSet()
    }

    companion object {
        const val ROWS = 5
        const val COLS = 5
        const val FIRST_TEAM_CARDS = 9
        const val SECOND_TEAM_CARDS = 8
        const val ASSASSIN_CARDS = 1

        fun totalCards(): Int {
            return ROWS * COLS
        }

        fun neutralCards(): Int {
            return Board.totalCards() - FIRST_TEAM_CARDS - SECOND_TEAM_CARDS - ASSASSIN_CARDS
        }
    }
}