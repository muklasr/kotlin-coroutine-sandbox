package com.muklasr.coroutine

import javafx.application.Application.launch
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import org.junit.jupiter.api.Test

class ChannelTest {

    @Test
    fun testChannel() {
        runBlocking {
            val channel = Channel<Int>()
            val job1 = launch {
                println("Send data 1 to channel")
                channel.send(1)
                println("Send data 2 to channel")
                channel.send(2)
            }
            val job2 = launch {
                println("Receive data ${channel.receive()}")
                println("Receive data ${channel.receive()}")
            }

            joinAll(job1, job2)
            channel.close()
        }
    }

    @Test
    fun testChannelUnlimited() {
        runBlocking {
            val channel = Channel<Int>(capacity = Channel.UNLIMITED) //ada buffernya
            val job1 = launch {
                println("Send data 1 to channel")
                channel.send(1)
                println("Send data 2 to channel")
                channel.send(2)
            }
            val job2 = launch {
                println("Receive data ${channel.receive()}")
                println("Receive data ${channel.receive()}")
            }

            joinAll(job1, job2)
            channel.close()
        }
    }

    @Test
    fun testChannelConflated() {
        runBlocking {
            val channel = Channel<Int>(capacity = Channel.CONFLATED) //data lama dihapus
            val job1 = launch {
                println("Send data 1 to channel")
                channel.send(1)
                println("Send data 2 to channel")
                channel.send(2)
                println("Send data 3 to channel")
                channel.send(3)
            }
            val job2 = launch {
                println("Receive data ${channel.receive()}")
            }

            joinAll(job1, job2)
            channel.close()
        }
    }

    @Test
    fun testChannelBufferOverflowSuspend() {
        runBlocking {
            val channel = Channel<Int>(capacity = 10, onBufferOverflow = BufferOverflow.SUSPEND)
            val job1 = launch {
                repeat(100){
                    println("Send data $it to channel")
                    channel.send(it)
                }
            }
            val job2 = launch {
                repeat(10) {
                    println("Receive data ${channel.receive()}")
                }
            }

            joinAll(job1, job2)
            channel.close()
        }
    }

    @Test
    fun testChannelBufferOverflowDropOldest() {
        runBlocking {
            val channel = Channel<Int>(capacity = 10, onBufferOverflow = BufferOverflow.DROP_OLDEST)
            val job1 = launch {
                repeat(100){
                    println("Send data $it to channel")
                    channel.send(it)
                }
            }
            val job2 = launch {
                repeat(10) {
                    println("Receive data ${channel.receive()}")
                }
            }

            joinAll(job1, job2)
            channel.close()
        }
    }

    @Test
    fun testChannelBufferOverflowDropLatest() {
        runBlocking {
            val channel = Channel<Int>(capacity = 10, onBufferOverflow = BufferOverflow.DROP_LATEST)
            val job1 = launch {
                repeat(100){
                    println("Send data $it to channel")
                    channel.send(it)
                }
            }
            val job2 = launch {
                repeat(10) {
                    println("Receive data ${channel.receive()}")
                }
            }

            joinAll(job1, job2)
            channel.close()
        }
    }

    @Test
    fun testChannelUndeliveredElement() {
        val channel = Channel<Int>(capacity = 10){
            println("Undelivered element $it")
        }
        channel.close()

        runBlocking {

            val job = launch {
                channel.send(10)
                channel.send(100)
            }

            job.join()
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testProduce() {
        val scope = CoroutineScope(Dispatchers.IO)
        val channel = scope.produce<Int> {
            repeat(100){
                println("Send data $it to channel")
                send(it)
            }
        }

        val job = scope.launch {
            repeat(100) {
                println("Receive data ${channel.receive()}")
            }
        }

        runBlocking {
            job.join()
        }
    }
}