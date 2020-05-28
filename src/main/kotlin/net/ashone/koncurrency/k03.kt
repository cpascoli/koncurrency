
package net.ashone.koncurrency.example3


import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


data class Task(val name: String, val duration: Long) {
    suspend fun run () {
        println("  > ${Thread.currentThread().name} is running task $name")
        delay(duration)
        println("  > ${Thread.currentThread().name} is done with $name")
    }
}

fun main()  {

    val tasks = listOf(
        Task("hello", 2000),
        Task("world", 1000)
    )

    tasks.forEach { println(it) }

    val time = measureTimeMillis {

        runBlocking {
            val jobs = processTasks(tasks)
            jobs.joinAll()
            println("tasks completed")
        }
    }


    println("time: $time ms")
}


suspend fun processTasks(tasks : List<Task>) : List<Job> {

    val jobs : List<Job> = tasks.map {
        GlobalScope.launch {
            println("Execute: ${it.name}")
            it.run()
        }
    }
    return jobs
}
