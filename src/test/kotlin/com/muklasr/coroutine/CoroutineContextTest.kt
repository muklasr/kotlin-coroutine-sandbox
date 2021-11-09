package com.muklasr.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.coroutines.CoroutineContext

class CoroutineContextTest {

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun testCoroutineContext(){
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
}