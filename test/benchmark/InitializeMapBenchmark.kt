package benchmark

fun main(vararg args: String) {
    IntToStringInitializeMapBenchmark().run()
    StringToIntInitializeMapBenchmark().run()
    IntToStringInitializeMapBenchmark().run()
}

class IntToStringInitializeMapBenchmark : InitializeMapBenchmark<Int, String>() {
    private val m = 1_000
    override val generator = { x: Int -> "$x -> ${3*x + 1}" }
    override val input = (1..m).toList()
}

class StringToIntInitializeMapBenchmark : InitializeMapBenchmark<String, Int>() {
    private val m = 1_000
    override val generator = String::length
    override val input = (1..m).map { "a".repeat(it) }
}

abstract class InitializeMapBenchmark<K, V> : Benchmark<List<K>, Map<K, V>>() {
    override val name = "Map Initialization"
    override val n = 50_000L

    abstract val generator: (K) -> V
    abstract val input: List<K>

    override fun getInput(run: Long): List<K> {
        return input
    }

    override fun checkOutput(run: Long, output: Map<K, V>) {
        assert(output !is MutableMap)

        for (x in getInput(run)) {
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
