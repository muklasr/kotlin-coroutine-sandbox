package com.muklasr.coroutine

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.*

class TickerTest {

    @ObsoleteCoroutinesApi
    @Test
    fun testTicker() {
        val receiveChannel = ticker(delayMillis = 1_000)
        runBlocking{
            val job = launch {
                repeat(10){
                    receiveChannel.receive()
                    println(Date())
                }
            }
            job.join()
        }
    }
}