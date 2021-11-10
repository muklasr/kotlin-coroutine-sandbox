package com.muklasr.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class SupervisorJobTest {

    @Test
    fun testJob() {
        //Jika ada child yg error maka child lainnya dibatalkan
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher + Job())

        val job1 = scope.launch {
            delay(2_000)
            println("Job 1 Done")
        }
        val job2 = scope.launch {
            delay(1_000)
            throw IllegalArgumentException("Job 2 Error")
        }

        runBlocking {
            joinAll(job1, job2)
        }
    }

    @Test
    fun testSupervisorJob() {
        //Jika ada child yg error maka child lainnya tidak dibatalkan
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher + SupervisorJob())

        val job1 = scope.launch {
            delay(2_000)
            println("Job 1 Done")
        }
        val job2 = scope.launch {
            delay(1_000)
            throw IllegalArgumentException("Job 2 Error")
        }

        runBlocking {
            joinAll(job1, job2)
        }
    }

    @Test
    fun testSupervisorScopeFunction() {
        //Jika ada child yg error maka child lainnya tidak dibatalkan
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher + Job())

        runBlocking {
            scope.launch { //bukan supervisor
                supervisorScope { //supervisor
                    launch {
                        delay(2_000)
                        println("Job 1 Done")
                    }
                    launch {
                        delay(1_000)
                        throw IllegalArgumentException("Job 2 Error")
                    }
                }
            }

            delay(3_000)
        }
    }

    @Test
    fun testJobExceptionHandler() {
        val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
            println("Error: ${throwable.message}")
        }

        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        runBlocking {
            val job = scope.launch {
                launch(exceptionHandler) { //USELESS
                    println("Job child")
                    throw IllegalArgumentException("Child error")
                }
            }

            job.join()
        }
    }

    @Test
    fun testSupervisorJobExceptionHandler() {
        val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
            println("Error: ${throwable.message}")
        }

        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        runBlocking {
            val job = scope.launch {
                supervisorScope {
                    launch(exceptionHandler) {
                        println("Job child")
                        throw IllegalArgumentException("Child error")
                    }
                }
            }

            job.join()
        }
    }
}