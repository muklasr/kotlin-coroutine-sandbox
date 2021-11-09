package com.muklasr.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.*

@DelicateCoroutinesApi
class TimeoutTest {

    @Test
    fun testTimeout() {
        runBlocking {
            val job = GlobalScope.launch {
                println("start")
                withTimeout(5_000) {
                    repeat(100) {
                        delay(1_000)
                        println("$it ${Date()}")
                    }
                }
                println("finish")
            }
            job.join()
        }
    }

    @Test
    fun testTimeoutOrNull() {
        runBlocking {
            val job = GlobalScope.launch {
                println("start")
                withTimeoutOrNull(5_000) {
                    repeat(100) {
                        delay(1_000)
                        println("$it ${Date()}")
                    }
                }
                println("finish")
            }
            job.join()
        }
    }
}