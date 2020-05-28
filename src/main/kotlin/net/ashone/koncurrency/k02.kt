package net.ashone.koncurrency.example2

import java.lang.Thread.sleep
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import kotlinx.coroutines.GlobalScope

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
        GlobalScope.launch {
            processTasks(tasks)
        }
    }
    println("time: $time ms")
}


fun processTasks(tasks : List<Task>) {
    tasks.forEach {
        println("Execute: ${it.name}")
        it.run()
    }
}
