package core

import dictionary.CodeWords
import extractByCount
import player.Guesser
import java.util.*

/**
 * Represents the cards on the table, as viewed from a single perspective (i.e. the spymasters or
 *  guessers on a team).
 * The Board is immutable except for cards being revealed by [reveal].
 * Note that the team of a [Card] on the Board is set to null if the owner of the card cannot be
 *  seen in the Board's context (i.e. the card is not revealed and the Board is only showing visible
 *  cards).
 */
class Board {
    private val cards: MutableMap<Square, Card>

    /**
     * Creates a new randomly-generated and fully-visible Board.
     *
     * @param rand  the source of randomness
     * @param first the first team to play (which determines the number of cards belong to them)
     */
    internal constructor(rand: Random, first: Team) {
        val dict = CodeWords()
        val words = dict.pickWords(TOTAL_CARDS, rand).toList()
        val teamCounts = mapOf(
                Pair(first, FIRST_TEAM_CARDS),
                Pair(first.opponent(), SECOND_TEAM_CARDS),
                Pair(Team.ASSASSIN, ASSASSIN_CARDS),
                Pair(Team.NEUTRAL, NEUTRAL_CARDS)
        )
        val teams = teamCounts.extractByCount(rand)

        cards = mutableMapOf()
        Square.validSquares.forEach { square ->
            val index = ((square.row-1) * Board.COLS) + (square.col-1)
            cards.put(square, Card(words[index], teams[index], false))
        }
    }

    /**
     * Creates an empty Board.
     * This should only be used in [visible] when the newly created Board is immediately filled.
     */
    private constructor() {
        this.cards = mutableMapOf()
    }

    /**
     * Gets the [Card] at the given [Square].
     *
     * @throws Square.InvalidSquareException if there is no Card at the given Square
     */
    fun cardAt(square: Square): Card {
        return cards[square] ?: throw Square.InvalidSquareException()
    }

    /**
     * Determines whether the given [Square] has been revealed.
     *
     * @throws Square.InvalidSquareException if there is no Card at the given Square
     */
    fun isRevealed(square: Square): Boolean {
        return cards[square]?.revealed ?: throw Square.InvalidSquareException()
    }

    /**
     * Gets all the [Card]s belonging to the given [Team] which have not been revealed (if that
     *  information is available, otherwise will return an empty Collection).
     */
    fun unrevealed(team: Team): Collection<Card> {
        return cards.values.filter { !it.revealed && it.team == team }
    }

    /**
     * Gets all the [Card]s belonging to the given [Team] which have been revealed.
     */
    fun revealed(team: Team): Collection<Card> {
        return cards.values.filter { it.revealed && it.team == team }
    }

    /**
     * Gets a [Map] from the [Square]s on this Board to the [Card]s occupying them.
     */
    fun cards(): Map<Square, Card> {
        return cards.toMap()
    }

    /**
     * Gets all words on all the [Card]s on the Board.
     */
    fun words(): Set<String> {
        return cards.values.map { it.word }.toSet()
    }

    /**
     * Determines the location of a [Card] containing the given word (as determined by
     *  [Card.wordsEqual]), or null if it does not occur on the Board.
     */
    fun locationOf(word: String): Square? {
        return cards.entries.find { Card.wordsEqual(it.value.word, word) }?.key
    }

    /**
     * Reveals the [Card] at the given location and returns the revealed [Card].
     * This method may only be called by the [Game] loop when a guess is submitted.
     *
     * @throws Square.InvalidSquareException if there is no Card at the given Square
     * @throws IllegalStateException if the given Square is already revealed
     */
    internal fun reveal(square: Square): Card {
        val card = cards[square] ?: throw Square.InvalidSquareException()

        if (card.revealed) {
            throw IllegalStateException()
        }

        if (card.team == null) {
            throw IllegalAccessException()
        }

        val revealed = card.copy(revealed = true)
        cards[square] = revealed
        return revealed
    }

    /**
     * Gets a copy of this Board which contains only the [Team] information for visible [Card]s.
     * Such a copy is the only version of the Board which should be given to [Guesser] players.
     */
    internal fun visible(): Board {
        val board = Board()
        for ((square, card) in cards.entries) {
            board.cards[square] = card.copy(team = if (card.revealed) card.team else null)
        }
        return board
    }

    companion object {
        const val ROWS = 5
        const val COLS = 5
        const val TOTAL_CARDS = ROWS * COLS
        const val FIRST_TEAM_CARDS = 9
        const val SECOND_TEAM_CARDS = 8
        const val ASSASSIN_CARDS = 1
        const val NEUTRAL_CARDS =
                TOTAL_CARDS - FIRST_TEAM_CARDS - SECOND_TEAM_CARDS - ASSASSIN_CARDS
    }
}
