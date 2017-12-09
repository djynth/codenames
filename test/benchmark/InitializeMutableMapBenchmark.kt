package benchmark

import java.util.*

fun main(vararg args: String) {
    IntToStringInitializeMutableMapBenchmark().run()
    StringToIntInitializeMutableMapBenchmark().run()
}

/**
 * Running Mutable Map Initialization [Int -> String] benchmark with N = 100,000.
 * mapAndConvert : 270834.560ns
 * putAll        : 280893.316ns
 * putOneByOne   : 258984.348ns
 */
class IntToStringInitializeMutableMapBenchmark : InitializeMutableMapBenchmark<Int, String>() {
    override val name = "Mutable Map Initialization [Int -> String]"
    override val generator = { x: Int -> "$x -> ${3*x + 1}" }

    override fun inputElement(rand: Random): Int {
        return rand.nextInt()
    }
}

/**
 * Running Mutable Map Initialization [String -> Int] benchmark with N = 100,000.
 * mapAndConvert : 159355.277ns
 * putAll        : 161101.786ns
 * putOneByOne   : 134287.411ns
 */
class StringToIntInitializeMutableMapBenchmark : InitializeMutableMapBenchmark<String, Int>() {
    override val name = "Mutable Map Initialization [String -> Int]"
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

abstract class InitializeMutableMapBenchmark<K, V> : Benchmark<List<K>, MutableMap<K, V>>() {
    override val n = 100_000
    private val m = 2_000

    abstract val generator: (K) -> V
    abstract fun inputElement(rand: Random): K

    override fun getInput(run: Int): List<K> {
        val rand = Random(run.toLong())
        return (1..m).map { inputElement(rand) }
    }

    override fun checkOutput(run: Int, input: List<K>, output: MutableMap<K, V>) {
        for (x in input) {
            assert(output[x] == generator(x))
        }
    }

    @BenchmarkTest
    fun mapAndConvert(input: List<K>): MutableMap<K, V> {
        return input.map { Pair(it, generator(it)) }.toMap().toMutableMap()
    }

    @BenchmarkTest
    fun putOneByOne(input: List<K>): MutableMap<K, V> {
        val map = mutableMapOf<K, V>()
        input.forEach { map.put(it, generator(it)) }
        return map
    }

    @BenchmarkTest
    fun putAll(input: List<K>): MutableMap<K, V> {
        val map = mutableMapOf<K, V>()
        map.putAll(input.map { Pair(it, generator(it)) })
        return map
    }
}
