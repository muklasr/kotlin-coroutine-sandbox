package com.muklasr.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.jupiter.api.Test
import java.util.*

class FlowTest {

    @Test
    fun testFlow() {
        val flow = flow<Int> {
            println("Start flow")
            repeat(100) {
                println("Emit $it")
                emit(it)
            }
        }

        runBlocking {
            flow.collect {
                println("Receive $it")
            }
        }
    }

    suspend fun numberFlow(): Flow<Int> = flow {
        repeat(100) {
            emit(it)
        }
    }

    suspend fun changeToString(number: Int): String {
        delay(100)
        return "Number $number"
    }

    @Test
    fun testFlowOperator() {
        runBlocking {
            val flow = numberFlow()
            flow.filter { it % 2 == 0 }
                .map { changeToString(it) }
                .collect {
                    println(it)
                }
        }
    }

    @Test
    fun testFlowException() {
        runBlocking {
            val flow = numberFlow()
            flow.map { check(it < 10); it }
                .onEach { println(it) }
                .catch { println("Error: ${it.message}") }
                .onCompletion { println("Done") }
                .collect()
        }
    }

    @Test
    fun testFlowCancellable() {
        val scope = CoroutineScope(Dispatchers.IO)
        runBlocking {
            val job = scope.launch {
                numberFlow().onEach {
                    if (it > 10) this.cancel()
                    else println(it)
                }.collect()
            }
            job.join()
        }
    }

    @Test
    fun testSharedFlow() {
        val scope = CoroutineScope(Dispatchers.IO)
        val sharedFlow = MutableSharedFlow<Int>()

        scope.launch {
            repeat(10) {
                println("Send $it ${Date()}")
                sharedFlow.emit(it)
                delay(1_000)
            }
        }
        scope.launch {
            repeat(10) {
                sharedFlow.asSharedFlow()
                    .buffer(10)
                    .map { "Receive job 1 $it ${Date()}" }
                    .collect {
                        println(it)
                        delay(2_000)
                    }
            }
        }
        scope.launch {
            repeat(10) {
                sharedFlow.asSharedFlow()
                    .buffer(10)
                    .map { "Receive job 2 ${Date()}" }
                    .collect {
                        println(it)
                        delay(5_000)
                    }
            }
        }

        runBlocking {
            delay(22_000)
            scope.cancel()
        }
    }

    @Test
    fun testStateFlow() {
        val scope = CoroutineScope(Dispatchers.IO)
        val stateFlow = MutableStateFlow(0)

        scope.launch {
            repeat(10) {
                println("Send $it ${Date()}")
                stateFlow.emit(it)
                delay(1_000)
            }
        }
        scope.launch {
            repeat(10) {
                stateFlow.asStateFlow()
                    .map { "Receive job 1 $it ${Date()}" }
                    .collect {
                        println(it)
                        delay(2_000)
                    }
            }
        }

        runBlocking {
            delay(22_000)
            scope.cancel()
        }
    }
}