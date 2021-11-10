package com.muklasr.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.concurrent.thread

class CoroutineTest {

    suspend fun hello() {
        delay(1_000)
        println("Hello World")
    }

    @DelicateCoroutinesApi
    @Test
    fun testCoroutine() {
        GlobalScope.launch {
            hello()
        }

        println("Menunggu")
        runBlocking {
            delay(2_000)
        }
        println("Selesai")
    }

    @Test
    fun testThread() {
        repeat(10_000) {
            thread {
                Thread.sleep(1_000)
                println("Done $it at ${Date()}")
            }
        }
        println("Waiting")
        Thread.sleep(3_000)
        println("Finish")
    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun testCoroutineMany() {
        repeat(100_000) {
            GlobalScope.launch {
                delay(1_000)
                println("Done $it at ${Date()} ${Thread.currentThread().name}")
            }
        }
        println("Waiting")
        runBlocking {
            delay(3_000)
        }
        println("Finish")
    }

    @Test
    fun testParentChild() {
        runBlocking {
            val job = GlobalScope.launch {
                launch {
                    delay(2_000)
                    println("Child 1 Done")
                }

                launch {
                    delay(4_000)
                    println("Child 2 Done")
                }
                delay(1_000)
                println("Parent Done")
            }

            job.join()
        }
    }

    @Test
    fun testParentChildCancel() {
        runBlocking {
            val job = GlobalScope.launch {
                launch {
                    delay(2_000)
                    println("Child 1 Done")
                }

                launch {
                    delay(4_000)
                    println("Child 2 Done")
                }
                delay(1_000)
                println("Parent Done")
            }

            job.cancelChildren()
            job.join()
        }
    }

    @Test
    fun testAwaitCancellation() {
        runBlocking {
            val job = launch {
                try {
                    println("Job start")
                    awaitCancellation()
                } finally {
                    println("Job cancelled")
                }
            }

            delay(5_000)
            job.cancelAndJoin()
        }
    }
}