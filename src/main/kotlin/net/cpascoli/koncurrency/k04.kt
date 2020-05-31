
package net.ashone.koncurrency.example4


import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


data class Task(val name: String, val duration: Long) {
    suspend fun run () : String {
        println("  > ${Thread.currentThread().name} is running task $name")
        delay(duration)
        println("  > ${Thread.currentThread().name} is done with task $name")
        return name.reversed()
    }
}

fun main()  {

    val tasks = listOf(
        Task("hello", 2000),
        Task("world", 500)
    )

    tasks.forEach { println(it) }

    val time = measureTimeMillis {

        runBlocking {

            val jobs : List<Deferred<String>> = tasks.map { task ->
                GlobalScope.async (start = CoroutineStart.LAZY) {
                    println("invoking task ${task.name}")
                    task.run()
                }
            }

            jobs.forEachIndexed { idx, job ->
                job.start()
                println("job ${tasks[idx].name} started")
            }

            val results : List<String> = jobs.map {
                it.await()
            }

            results.forEachIndexed { idx, result ->
                println("${tasks[idx].name} result -> $result")
            }
        }
    }

    println("time: $time ms")
}
