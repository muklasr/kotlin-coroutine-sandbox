package com.muklasr.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.*

@DelicateCoroutinesApi
class CancellableCoroutineTest {

    @Test
    fun testCanNotCancel() {
        runBlocking {
            val job = GlobalScope.launch {
                println("start coroutine ${Date()}")
                Thread.sleep(2_000)
                println("end coroutine ${Date()}")
            }
            job.cancel()
            delay(3_000)
        }
    }

    @Test
    fun testCancel() {
        runBlocking {
            val job = GlobalScope.launch {
                if (!isActive) throw CancellationException()
                println("start coroutine ${Date()}")

                ensureActive()
                Thread.sleep(2_000)

                ensureActive()
                println("end coroutine ${Date()}")
            }
            job.cancel()
            delay(3_000)
        }
    }

    @Test
    fun testCancellableFinally() {
        runBlocking {
            val job = GlobalScope.launch {
                try {
                    println("start coroutine ${Date()}")
                    delay(2_000)
                    println("end coroutine ${Date()}")
                } finally {
                    println("finish")
                }
            }
            job.cancelAndJoin()
        }
    }
}