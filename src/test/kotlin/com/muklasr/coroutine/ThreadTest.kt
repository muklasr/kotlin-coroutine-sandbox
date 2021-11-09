package com.muklasr.coroutine

import org.junit.jupiter.api.Test
import java.util.*
import kotlin.concurrent.thread

class ThreadTest {

    @Test
    fun testThreadName() {
        val threadName = Thread.currentThread().name
        println("Running in thread $threadName")
    }

    @Test
    fun testNewThread() {
        val runnable = Runnable {
            println(Date())
            Thread.sleep(2_000)
            println("Finished at ${Date()}")
        }

        val thread = Thread(runnable)
        thread.start()

        println("Menunggu selesai")
        Thread.sleep(3_000)
        println("Selesai")
    }

    @Test
    fun testThreadKotlinFun(){
        thread {
            println(Date())
            Thread.sleep(2_000)
            println("Finish at ${Date()}")
        }

        println("Menunggu selesai")
        Thread.sleep(3_000)
        println("Selesai")
    }

    @Test
    fun testMultipleThread() {
        val thread1 = Thread {
            println(Date())
            Thread.sleep(2_000)
            println("Finished Thread1 (${Thread.currentThread().name}) at ${Date()}")
        }
        val thread2 = Thread {
            println(Date())
            Thread.sleep(2_000)
            println("Finished Thread2 (${Thread.currentThread().name}) at ${Date()}")
        }

        thread1.start()
        thread2.start()

        println("Menunggu selesai")
        Thread.sleep(3_000)
        println("Selesai")
    }
}