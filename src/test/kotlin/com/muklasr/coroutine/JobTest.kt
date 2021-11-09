package com.muklasr.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

@DelicateCoroutinesApi
class JobTest {

    @Test
    fun testJob() {
        runBlocking {
            GlobalScope.launch {
                delay(2_000)
                println("Coroutine done ${Thread.currentThread().name}")
            }
        }
    }

    @Test
    fun testJobLazy() {
        runBlocking {
            val job: Job = GlobalScope.launch(start = CoroutineStart.LAZY) {
                delay(2_000)
                println("Coroutine done ${Thread.currentThread().name}")
            }
            job.start()

            delay(3_000)
        }
    }

    @Test
    fun testJobJoin() {
        runBlocking {
            val job: Job = GlobalScope.launch {
                delay(2_000)
                println("Coroutine done ${Thread.currentThread().name}")
            }
            job.join()
        }
    }

    @Test
    fun testJobCancel() {
        runBlocking {
            val job: Job = GlobalScope.launch {
                delay(2_000)
                println("Coroutine done ${Thread.currentThread().name}")
            }
            job.cancel()

            delay(3_000)
        }
    }

    @Test
    fun testJobJoinAll() {
        runBlocking {
            val job1: Job = GlobalScope.launch {
                delay(2_000)
                println("Coroutine done ${Thread.currentThread().name}")
            }
            val job2: Job = GlobalScope.launch {
                delay(2_000)
                println("Coroutine done ${Thread.currentThread().name}")
            }
            joinAll(job1, job2)
        }
    }
}