package com.muklasr.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

@DelicateCoroutinesApi
class SequentialSuspendFunctionTest {

    suspend fun getFoo(): Int {
        delay(1_000)
        return 10
    }

    suspend fun getBar(): Int {
        delay(1_000)
        return 10
    }

    @Test
    fun testSequential() {
        runBlocking {
            val time = measureTimeMillis {
                getFoo()
                getBar()
            }
            println("Total time: $time")
        }
    }

    @Test
    fun testSequentialCoroutine() {
        runBlocking {
            val job = GlobalScope.launch {
                val time = measureTimeMillis {
                    getFoo()
                    getBar()
                }
                println("Total time: $time")
            }
            job.join()
        }
    }

    @Test
    fun testConcurrent() {
        runBlocking {
            val time = measureTimeMillis {
                val job1 = GlobalScope.launch { getFoo() }
                val job2 = GlobalScope.launch { getBar() }

                joinAll(job1, job2)
            }
            println("Total time: $time")
        }
    }
}