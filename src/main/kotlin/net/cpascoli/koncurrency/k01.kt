package net.ashone.koncurrency.example1

import java.lang.Thread.sleep
import kotlin.system.measureTimeMillis

data class Task(val name: String, val duration: Long) {
    fun run () {
        println("  > ${Thread.currentThread().name} is running task $name")
        sleep(duration)
    }
}

fun main() {

    val tasks = listOf(
        Task("hello", 2000),
        Task("world", 2000)
    )

    tasks.forEach { println(it) }

    val time = measureTimeMillis {
        tasks.forEach {
            println("Execute: ${it.name}")
            it.run()

        }
    }
    println("time: $time ms")
}
