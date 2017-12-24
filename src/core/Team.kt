package core

enum class Team {
    RED, BLUE, NEUTRAL, ASSASSIN;

    /**
     * The opponent of this [Team], i.e. [Team.RED] if it is [Team.BLUE] or [Team.BLUE] if it is
     *  [Team.RED].
     *
     * @throws IllegalArgumentException if this [Team] does not have an opponent, i.e. it is neither
     *                                  [Team.BLUE] or [Team.RED]
     */
    fun opponent(): Team {
        return when (this) {
            RED -> BLUE
            BLUE -> RED
            else -> throw IllegalArgumentException()
        }
    }
}
