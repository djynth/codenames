package benchmark

import java.text.DecimalFormat
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions

abstract class Benchmark<K, in V> {
    abstract val n: Int
    abstract val name: String
    private val nFormat = DecimalFormat("#,###")
    private val timeFormat = DecimalFormat("#.000ns")

    fun run() {
        println("Running $name benchmark with N = ${nFormat.format(n)}.")

        val tests = tests()

        val maxNameLength = tests.map { it.name.length }.max() ?: 0

        for (test in tests) {
            var runtime = 0.0
            for (run in 1..n) {
                val input = getInput(run)
                val start = System.nanoTime()
                val output = test.call(this, input)
                val end = System.nanoTime()
                runtime += (end - start)
                checkOutput(run, input, output)
            }

            println("${test.name.padEnd(maxNameLength, ' ')} : ${timeFormat.format(runtime / n)}")
        }
    }

    private fun tests(): List<KFunction<V>> {
        return this::class.functions
                .filter { it.annotations.any { it is BenchmarkTest } }
                .filterIsInstance<KFunction<V>>()
                .sortedBy { it.name }
    }

    abstract fun getInput(run: Int): K

    open fun checkOutput(run: Int, input: K, output: V) { }
}

@Target(AnnotationTarget.FUNCTION)
annotation class BenchmarkTest
