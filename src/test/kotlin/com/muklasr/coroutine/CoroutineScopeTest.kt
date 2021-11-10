package com.muklasr.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class CoroutineScopeTest {

    @Test
    fun testScope() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            delay(1_000)
            println("Run in ${Thread.currentThread().name}")
        }
        scope.launch {
            delay(1_000)
            println("Run in ${Thread.currentThread().name}")
        }

        runBlocking {
            delay(2_000)
            println("Done")
        }
    }

    @Test
    fun testScopeCancel() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            delay(2_000)
            println("Run in ${Thread.currentThread().name}")
        }
        scope.launch {
            delay(2_000)
            println("Run in ${Thread.currentThread().name}")
        }

        runBlocking {
            delay(1_000)
            scope.cancel()
            delay(2_000)
            println("Done")
        }
    }


    suspend fun getFoo(): Int {
        delay(1_000)
        return 10
    }

    suspend fun getBar(): Int {
        delay(1_000)
        return 10
    }

    suspend fun getSum(): Int = coroutineScope {
        val foo = async { getFoo() }
        val bar = async { getBar() }
        foo.await() + bar.await()
    }

    @Test
    fun testCoroutineScopeFunction() {
        val scope = CoroutineScope(Dispatchers.IO)
        val job = scope.launch {
            val result = getSum()
            println("Result = $result")
        }

        runBlocking {
            job.join()
        }
    }

    @Test
    fun testParentChildDispatcher() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        val job = scope.launch {
            println("Parent scope : ${Thread.currentThread().name}")
            coroutineScope {
                launch {
                    println("Child scope : ${Thread.currentThread().name}")
                }
            }
        }

        runBlocking {
            job.join()
        }
    }

    @Test
    fun testParentChildCancel() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        val job = scope.launch {
            println("Parent scope : ${Thread.currentThread().name}")
            coroutineScope {
                launch {
                    delay(2_000)
                    println("Child scope : ${Thread.currentThread().name}")
                }
            }
        }

        runBlocking {
            job.cancelAndJoin()
        }
    }
}