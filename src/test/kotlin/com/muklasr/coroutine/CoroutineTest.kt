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
    fun testThread(){
        repeat(10_000){
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
    fun testCoroutineMany(){
        repeat(100_000){
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

}