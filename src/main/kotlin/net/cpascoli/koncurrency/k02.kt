package net.ashone.koncurrency.example2

import java.lang.Thread.sleep
import java.util.concurrent.*
import kotlin.system.measureTimeMillis


data class Task(val name: String, val duration: Long) : Callable<String>  {

    override fun call() : String {
        println("  > ${Thread.currentThread().name} is running task $name")
        sleep(duration)
        return name.reversed()
    }
}

fun main() {

    val executor = Executors.newFixedThreadPool(2)

    val tasks = listOf(
        Task("hello", 4_000),
        Task("world", 1_000)
    )

    tasks.forEach { println(it) }

    val futures : List<Future<String>> = tasks.map { task ->
        executor.submit(task)
    }

    val time = measureTimeMillis {
        val results = futures.map { future ->
            future.get()
        }
        results.forEach { println("got result: $it") }
    }
    println("time: $time ms")

    executor.shutdown()
    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)
}
