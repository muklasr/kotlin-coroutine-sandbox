package com.muklasr.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

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
}