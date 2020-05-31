package net.ashone.koncurrency.example0

import java.lang.Thread.sleep
import kotlin.system.measureTimeMillis

data class Task(val name: String, val duration: Long) {
    fun run () {
        println("  > ${Thread.currentThread().name} is running task - $name")
        sleep(duration)
        println("  > ${Thread.currentThread().name} is done with task - $name")
    }
}

fun main() {

    val tasks = listOf(
        Task("hello", 1_000),
        Task("world", 2_000)
    )

    tasks.forEach { println(it) }

    var time = measureTimeMillis {
        tasks.forEach {
            println("Execute: ${it.name}")
            it.run()
        }
    }
    println("time: $time ms")

    // using old skool threads

    time = measureTimeMillis {

        val threads = tasks.map { task ->
            Thread { task.run() }
        }

        println("${Thread.currentThread().name} > Starting threads")
        threads.forEach { it.start() }

        println("${Thread.currentThread().name} > Joining threads")
        threads.forEach { it.join() }
    }
    println("time: $time ms")
}
