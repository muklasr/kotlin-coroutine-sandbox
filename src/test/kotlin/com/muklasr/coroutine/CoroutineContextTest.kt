package com.muklasr.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class CoroutineContextTest {

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun testCoroutineContext() {
        runBlocking {
            val job = GlobalScope.launch {
                val context: CoroutineContext = coroutineContext
                println(context)
                println(context[Job])
                println(context[CoroutineDispatcher])
            }
            job.join()
        }
    }

    @Test
    fun testCoroutineName() {
        val scope = CoroutineScope(Dispatchers.IO)

        val job = scope.launch(CoroutineName("Parent")) {
            println("Parent run in thread ${Thread.currentThread().name}")
            withContext(CoroutineName("Child")) {
                println("Child run in thread ${Thread.currentThread().name}")
            }
        }

        runBlocking {
            job.join()
        }
    }

    @Test
    fun testCoroutineNameContext() {
        val scope = CoroutineScope(Dispatchers.IO + CoroutineName("Test"))

        val job = scope.launch {
            println("Parent run in thread ${Thread.currentThread().name}")
            withContext(Dispatchers.IO) {
                println("Child run in thread ${Thread.currentThread().name}")
            }
        }

        runBlocking {
            job.join()
        }
    }

    @Test
    fun testCoroutineElement() {
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val scope = CoroutineScope(Dispatchers.IO + CoroutineName("Test"))

        val job = scope.launch(CoroutineName("Parent") + dispatcher) {
            println("Parent run in thread ${Thread.currentThread().name}")
            withContext(CoroutineName("Child") + Dispatchers.IO) {
                println("Child run in thread ${Thread.currentThread().name}")
            }
        }

        runBlocking {
            job.join()
        }
    }
}