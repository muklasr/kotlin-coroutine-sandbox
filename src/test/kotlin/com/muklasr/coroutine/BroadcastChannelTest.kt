package com.muklasr.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.broadcast
import org.junit.jupiter.api.Test

class BroadcastChannelTest {

    @Test
    fun testBroadcastChannel() {
        val broadcastChannel = BroadcastChannel<Int>(capacity = 10)
        val receiveChannel1 = broadcastChannel.openSubscription()
        val receiveChannel2 = broadcastChannel.openSubscription()

        val scope = CoroutineScope(Dispatchers.IO)

        val jobSend = scope.launch {
            repeat(10) {
                broadcastChannel.send(it)
            }
        }

        val job1 = scope.launch {
            repeat(10) {
                println("Job 1 receive ${receiveChannel1.receive()}")
            }
        }

        val job2 = scope.launch {
            repeat(10) {
                println("Job 2 receive ${receiveChannel2.receive()}")
            }
        }

        runBlocking {
            joinAll(job1, job2, jobSend)
        }
    }

    @Test
    fun testBroadcastFunction() {
        val scope = CoroutineScope(Dispatchers.IO)

        val broadcastChannel = scope.broadcast<Int>(capacity = 10) {
            repeat(10) {
                send(it)
            }
        }

        val receiveChannel1 = broadcastChannel.openSubscription()
        val receiveChannel2 = broadcastChannel.openSubscription()

        val job1 = scope.launch {
            repeat(10) {
                println("Job 1 receive ${receiveChannel1.receive()}")
            }
        }

        val job2 = scope.launch {
            repeat(10) {
                println("Job 2 receive ${receiveChannel2.receive()}")
            }
        }

        runBlocking {
            joinAll(job1, job2)
        }
    }

    @Test
    fun testConflatedBroadcastChannel() {
        val conflatedBroadcastChannel = ConflatedBroadcastChannel<Int>()
        val receiveChannel = conflatedBroadcastChannel.openSubscription()

        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            repeat(10){
                delay(1_000)
                println("Send $it")
                conflatedBroadcastChannel.send(it)
            }
        }

        scope.launch {
            repeat(10){
                delay(3_000)
                println("Receive ${receiveChannel.receive()}")
            }
        }

        runBlocking {
            delay(15_000)
            scope.cancel()
        }
    }
}