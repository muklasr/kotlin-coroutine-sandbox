package com.muklasr.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

@OptIn(DelicateCoroutinesApi::class)
class CoroutineDispatcherTest {

    @Test
    fun testDispatcher() {
        runBlocking {
            println("Run in ${Thread.currentThread().name}")
            val job1 = GlobalScope.launch(Dispatchers.Default) {
                println("Job 1 run in ${Thread.currentThread().name}")
            }
            val job2 = GlobalScope.launch(Dispatchers.IO) {
                println("Job 2 run in ${Thread.currentThread().name}")
            }
            joinAll(job1, job2)
        }
    }

    @Test
    fun testUnconfined() {
        runBlocking {
            println("runBlocking run in ${Thread.currentThread().name}")

            GlobalScope.launch(Dispatchers.Unconfined) { //Threadnya dapat berubah-ubah
                println("Unconfined run in ${Thread.currentThread().name}")
                delay(1_000)
                println("Unconfined run in ${Thread.currentThread().name}")
                delay(1_000)
                println("Unconfined run in ${Thread.currentThread().name}")
            }
            GlobalScope.launch { //Threadnya tidak berubah-ubah
                println("Confined run in ${Thread.currentThread().name}")
                delay(1_000)
                println("Confined run in ${Thread.currentThread().name}")
                delay(1_000)
                println("Confined run in ${Thread.currentThread().name}")
            }

            delay(2_000)
        }
    }

    @Test
    fun testExecutorService() {
        val dispatcherService = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val dispatcherWeb = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        runBlocking {
            val job1 = GlobalScope.launch(dispatcherService) {
                println("Job1 run in ${Thread.currentThread().name}")
            }
            val job2 = GlobalScope.launch(dispatcherWeb) {
                println("Job2 run in ${Thread.currentThread().name}")
            }
            joinAll(job1, job2)
        }
    }

    @Test
    fun testWithContext() {
        val dispatcherClient = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        runBlocking {
            val job = GlobalScope.launch(Dispatchers.IO) {
                println("1 ${Thread.currentThread().name}")
                withContext(dispatcherClient) {
                    println("2 ${Thread.currentThread().name}")
                }
                println("3 ${Thread.currentThread().name}")
                withContext(dispatcherClient) {
                    println("4 ${Thread.currentThread().name}")
                }
            }
            job.join()
        }
    }

    @Test
    fun testCancelFinally(){
        runBlocking {
            val job = GlobalScope.launch {
                try {
                    println("Start")
                    delay(1_000)
                    println("End")
                } finally {
                    println(isActive)
                    delay(1_000)
                    println("Finally")
                }
            }
            job.cancelAndJoin()
        }
    }

    @Test
    fun testNonCancellable(){
        runBlocking {
            val job = GlobalScope.launch {
                try {
                    println("Start")
                    delay(3_000)
                    println("End")
                } finally {
                    withContext(NonCancellable) {
                        println(isActive)
                        delay(1_000)
                        println("Finally")
                    }
                }
            }
            job.cancelAndJoin()
        }
    }
}