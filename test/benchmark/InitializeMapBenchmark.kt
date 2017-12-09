package benchmark

import java.util.*

fun main(vararg args: String) {
    IntToStringInitializeMapBenchmark().run()
    StringToIntInitializeMapBenchmark().run()
}

/**
 * Running Map Initialization [Int -> String] benchmark with N = 100,000.
 * mapAndConvert : 224267.732ns
 * putAll        : 275329.377ns
 * putOneByOne   : 262536.464ns
 */
class IntToStringInitializeMapBenchmark : InitializeMapBenchmark<Int, String>() {
    override val name = "Map Initialization [Int -> String]"
    override val generator = { x: Int -> "$x -> ${3*x + 1}" }

    override fun inputElement(rand: Random): Int {
        return rand.nextInt()
    }
}

/**
 * Running Map Initialization [String -> Int] benchmark with N = 100,000.
 * mapAndConvert : 107773.056ns
 * putAll        : 155017.196ns
 * putOneByOne   : 134173.070ns
 */
class StringToIntInitializeMapBenchmark : InitializeMapBenchmark<String, Int>() {
    override val name = "Map Initialization [String -> Int]"
    override val generator = String::length

    override fun inputElement(rand: Random): String {
        val sb = StringBuilder()
        val length = rand.nextInt(10) + 5
        for (i in 1..length) {
            sb.append('a' + rand.nextInt(26))
        }
        return sb.toString()
    }
}

abstract class InitializeMapBenchmark<K, V> : Benchmark<List<K>, Map<K, V>>() {
    override val n = 100_000
    private val m = 2_000

    abstract val generator: (K) -> V
    abstract fun inputElement(rand: Random): K

    override fun getInput(run: Int): List<K> {
        val rand = Random(run.toLong())
        return (1..m).map { inputElement(rand) }
    }

    override fun checkOutput(run: Int, input: List<K>, output: Map<K, V>) {
        assert(output !is MutableMap)

        for (x in input) {
            assert(output[x] == generator(x))
        }
    }

    @BenchmarkTest
    fun mapAndConvert(input: List<K>): Map<K, V> {
        return input.map { Pair(it, generator(it)) }.toMap()
    }

    @BenchmarkTest
    fun putOneByOne(input: List<K>): Map<K, V> {
        val map = mutableMapOf<K, V>()
        input.forEach { map.put(it, generator(it)) }
        return map
    }

    @BenchmarkTest
    fun putAll(input: List<K>): Map<K, V> {
        val map = mutableMapOf<K, V>()
        map.putAll(input.map { Pair(it, generator(it)) })
        return map
    }
}
