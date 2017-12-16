package core

import dictionary.CodeWords
import extractByCount
import player.Player
import player.Spymaster
import java.util.*

class Board(rand: Random, first: Team) {
    private val cards: MutableMap<Square, Card>

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
        cards = Square.validSquares.map { square ->
            val index = ((square.row-1) * Board.COLS) + (square.col-1)
            Pair(square, Card(words[index], teams[index], false))
        }.toMap().toMutableMap()
    }

    fun isRevealed(square: Square): Boolean {
        return cards[square]?.revealed ?: throw Square.InvalidSquareException()
    }

    fun reveal(square: Square): Card {
        val card = cards[square] ?: throw Square.InvalidSquareException()

        if (card.revealed) {
            throw IllegalStateException()
        }

        val revealed = card.copy(revealed = true)
        cards[square] = revealed
        return revealed
    }

    fun unrevealed(team: Team): Collection<Card> {
        return cards.values.filter { !it.revealed && it.team == team }
    }

    fun revealed(team: Team): Collection<Card> {
        return cards.values.filter { it.revealed && it.team == team }
    }

    fun cards(): Map<Square, Card> {
        return cards.toMap()
    }

    fun locationOf(card: Card): Square? {
        return locationOf(card.word)
    }

    fun locationOf(word: String): Square? {
        return cards.entries.find { Card.wordsEqual(it.value.word, word) }?.key
    }

    fun visibleTo(player: Player): Map<Square, Card> {
        return Square.validSquares.map { square ->
            val card = cards[square] ?: throw IllegalStateException()
            val visible = player is Spymaster || card.revealed
            Pair(square, card.copy(team = if (visible) card.team else null))
        }.toMap()
    }

    fun words(): Set<String> {
        return cards.values.map { it.word }.toSet()
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
