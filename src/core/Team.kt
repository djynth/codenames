package core

enum class Team {
    RED, BLUE, NEUTRAL, ASSASSIN;

    fun opponent(): Team {
        return when (this) {
            RED -> BLUE
            BLUE -> RED
            else -> throw IllegalArgumentException()
        }
    }
}