package com.muklasr.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select
import org.junit.jupiter.api.Test

class SelectTest {

    @Test
    fun testSelect() {
        val scope = CoroutineScope(Dispatchers.IO)

        val deferred1 = scope.async {
            delay(1_000)
            1000
        }

        val deferred2 = scope.async {
            delay(2_000)
            2000
        }

        val job = scope.launch {
            val win = select<Int> {
                deferred1.onAwait { it }
                deferred2.onAwait { it }
            }
            println("Selected $win")
        }

        runBlocking {
            job.join()
        }
    }

    @Test
    fun testSelectChannel() {
        val scope = CoroutineScope(Dispatchers.IO)

        val receiveChannel1 = scope.produce {
            delay(1_000)
            send(1000)
        }

        val receiveChannel2 = scope.produce {
            delay(2_000)
            send(2000)
        }

        val receiveChannel3 = scope.produce {
            delay(500)
            send(500)
        }

        val job = scope.launch {
            val win = select<Int> {
                receiveChannel1.onReceive { it }
                receiveChannel2.onReceive { it }
                receiveChannel3.onReceive { it }
            }
            println("Selected $win")
        }

        runBlocking {
            job.join()
        }
    }

    @Test
    fun testSelectChannelAndDeferred() {
        val scope = CoroutineScope(Dispatchers.IO)

        val receiveChannel1 = scope.produce {
            delay(100)
            send(100)
        }

        val deferred1 = scope.async {
            delay(2_000)
            2000
        }

        val deferred2 = scope.async {
            delay(500)
            500
        }

        val job = scope.launch {
            val win = select<Int> {
                receiveChannel1.onReceive { it }
                deferred1.onAwait { it }
                deferred2.onAwait { it }
            }
            println("Selected $win")
        }

        runBlocking {
            job.join()
        }
    }


}