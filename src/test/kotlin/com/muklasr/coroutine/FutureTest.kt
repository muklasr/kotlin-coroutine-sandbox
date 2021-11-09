package com.muklasr.coroutine

import org.junit.jupiter.api.Test
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.system.measureTimeMillis

class FutureTest {

    val executorService = Executors.newFixedThreadPool(10)

    fun getFoo1(): Int {
        Thread.sleep(5_000)
        return 10
    }

    fun getBar1(): Int {
        Thread.sleep(5_000)
        return 10
    }

    fun getFoo2(): Int {
        Thread.sleep(5_000)
        return 10
    }

    fun getBar2(): Int {
        Thread.sleep(5_000)
        return 10
    }

    @Test
    fun testNonParallel() {
        val totalTime = measureTimeMillis {
            val foo1 = getFoo1()
            val bar1 = getBar1()
            val foo2 = getFoo2()
            val bar2 = getBar2()

            val result = foo1 + bar1 + foo2 + bar2
            println("Result = $result")
        }
        println("Total time: $totalTime")
    }

    @Test
    fun testFuture() {
        val totalTime = measureTimeMillis {
            val foo1: Future<Int> = executorService.submit(Callable { getFoo1() })
            val bar1: Future<Int> = executorService.submit(Callable { getBar1() })
            val foo2: Future<Int> = executorService.submit(Callable { getFoo2() })
            val bar2: Future<Int> = executorService.submit(Callable { getBar2() })

            val result = foo1.get() + bar1.get() + foo2.get() + bar2.get()
            println("Result = $result")
        }
        println("Total time: $totalTime")
    }
}