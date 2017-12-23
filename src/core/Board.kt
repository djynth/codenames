package core

import dictionary.CodeWords
import extractByCount
import java.util.*

class Board {
    private val cards: MutableMap<Square, Card>

    internal constructor(rand: Random, first: Team) {
        val dict = CodeWords()
        val words = dict.pickWords(totalCards(), rand).toList()
        val teamCounts = mapOf(
                Pair(first, FIRST_TEAM_CARDS),
                Pair(first.opponent(), SECOND_TEAM_CARDS),
                Pair(Team.ASSASSIN, Board.ASSASSIN_CARDS),
                Pair(Team.NEUTRAL, Board.neutralCards())
        )
        val teams = teamCounts.extractByCount(rand)

        cards = mutableMapOf()
        Square.validSquares.forEach { square ->
            val index = ((square.row-1) * Board.COLS) + (square.col-1)
            cards.put(square, Card(words[index], teams[index], false))
        }
    }

    private constructor(cards: Map<Square, Card>) {
        this.cards = mutableMapOf()
        for ((square, card) in cards.entries) {
            this.cards[square] = card.copy(team = if (card.revealed) card.team else null)
        }
    }

    fun cardAt(square: Square): Card {
        return cards[square] ?: throw Square.InvalidSquareException()
    }

    fun isRevealed(square: Square): Boolean {
        return cards[square]?.revealed ?: throw Square.InvalidSquareException()
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

    fun words(): Set<String> {
        return cards.values.map { it.word }.toSet()
    }

    fun locationOf(word: String): Square? {
        return cards.entries.find { Card.wordsEqual(it.value.word, word) }?.key
    }

    internal fun reveal(square: Square): Card {
        val card = cards[square] ?: throw Square.InvalidSquareException()

        if (card.revealed) {
            throw IllegalStateException()
        }

        val revealed = card.copy(revealed = true)
        cards[square] = revealed
        return revealed
    }

    internal fun visible(): Board {
        return Board(cards)
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
