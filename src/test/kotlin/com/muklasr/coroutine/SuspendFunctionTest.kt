package com.muklasr.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.*

class SuspendFunctionTest {

    suspend fun helloWorld(){
        println("Hello : ${Date()} in ${Thread.currentThread().name}")
        delay(2_000)
        println("World : ${Date()} in ${Thread.currentThread().name}")
    }

    @Test
    fun testSuspendFunction(){
        runBlocking {
            helloWorld()
        }
    }
}