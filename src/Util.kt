import java.util.*

/**
 * Generates a randomly-ordered List containing each of the keys in this map occurring the number of
 *  times given by their value in this map.
 *
 * @param rand the source of randomness for ordering the extracted List
 */
fun <K> Map<K, Int>.extractByCount(rand: Random): List<K> {
    val values = entries
            .filter { it.value > 0 }
            .flatMap { Collections.nCopies(it.value, it.key) }
    Collections.shuffle(values, rand)
    return values
}