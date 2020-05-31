
package net.ashone.koncurrency.example3


import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


data class Task(val name: String, val duration: Long) {
    suspend fun run () {
        println("  > ${Thread.currentThread().name} is running task - $name")
        delay(duration)
        println("  > ${Thread.currentThread().name} is done with - $name")
    }
}

fun main()  {

    val tasks =  MutableList(size = 10) { index ->
        Task("hello world ($index)", 1000)
    }

    val time = measureTimeMillis {

       runBlocking {

           println("${Thread.currentThread().name} - starting ${tasks.size} tasks")

            val jobs : List<Job> = tasks.map { task ->
               GlobalScope.launch {
                   task.run()
               }
            }

            jobs.joinAll()
            println("${Thread.currentThread().name} - all tasks completed")
        }
    }

    println("time: $time ms")
}
