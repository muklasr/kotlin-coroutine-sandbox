package com.muklasr.coroutine

import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.Executors

class ExecutorServiceTest {

    @Test
    fun testSingleThreadPool() {
        //SingleThreadExecutor: Menggunakan 1 thread
        val executorService = Executors.newSingleThreadExecutor()
        repeat(10) {
            executorService.execute {
                Thread.sleep(1_000)
                println("Done at $it ${Thread.currentThread().name} ${Date()}")
            }
            println("Selesai memasukkan runnable $it")
        }

        println("Menunggu")
        Thread.sleep(11_000)
        println("Selesai")
    }

    @Test
    fun testFixedThreadPool() {
        //FixedThreadExecutor: Menggunakan thread sesuai yang diminta
        val executorService = Executors.newFixedThreadPool(3)
        repeat(10) {
            executorService.execute {
                Thread.sleep(1_000)
                println("Done at $it ${Thread.currentThread().name} ${Date()}")
            }
            println("Selesai memasukkan runnable $it")
        }

        println("Menunggu")
        Thread.sleep(11_000)
        println("Selesai")
    }

    @Test
    fun testCachedThreadPool() {
        //CachedThreadExecutor: Menggunakan thread sebanyak-banyaknya sampai memori habis!
        val executorService = Executors.newCachedThreadPool()
        repeat(10) {
            executorService.execute {
                Thread.sleep(1_000)
                println("Done at $it ${Thread.currentThread().name} ${Date()}")
            }
            println("Selesai memasukkan runnable $it")
        }

        println("Menunggu")
        Thread.sleep(11_000)
        println("Selesai")
    }
}