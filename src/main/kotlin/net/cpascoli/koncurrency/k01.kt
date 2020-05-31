package net.ashone.koncurrency.example1

import kotlin.system.measureTimeMillis
import java.lang.Thread.sleep
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


data class Task(val name: String, val duration: Long) {
    fun run () {
        println("  > ${Thread.currentThread().name} is running task - $name")
        sleep(duration)
        println("  > ${Thread.currentThread().name} is done with task - $name")
    }
}

fun main() {

    val executor = Executors.newFixedThreadPool(2)

    val tasks = listOf(
        Task("hello", 1_000),
        Task("world", 2_000)
    )

    tasks.forEach { println(it) }

    val time = measureTimeMillis {
        tasks.forEach { task ->
            executor.execute {
                task.run()
            }
        }

        executor.shutdown()
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)
    }

    println("time: $time ms")

}
