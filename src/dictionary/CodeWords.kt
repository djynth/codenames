package dictionary

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.streams.toList

class CodeWords {

    private val filename = "original.txt"
    private val words: List<String>

    init {
        words = Files.lines(Paths.get(filename)).toList()
    }

    fun pickWords(num: Int, rand: Random): Set<String> {
        val picked = mutableSetOf<String>()
        while (picked.size < num) {
            picked.add(words[rand.nextInt(words.size)])
        }
        return picked
    }
}