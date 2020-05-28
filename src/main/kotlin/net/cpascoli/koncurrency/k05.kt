package net.ashone.koncurrency.example5

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


fun main()  {

    val words = listOf("hello", "world")
//    words.forEach { println(it) }

    val uppercase = createActor { msg : Message ->
        val uppercased = msg.input.toUpperCase()
        msg.response.complete(uppercased)
    }

    val reverse = createActor { msg : Message ->
        val reversed = msg.input.reversed()
        uppercase.send( Message(reversed, msg.response) )
    }


    val start = GlobalScope.actor<Message> {

        // internal state
        var words = mutableListOf<String>()

        for (msg in channel) {
            words.add(msg.input)
            val joined = words.joinToString(" ")
            reverse.send(Message(joined, msg.response))
        }
    }


    val time = measureTimeMillis {
        runBlocking {
            val messages = words.map { word ->
                val message = Message(word, CompletableDeferred())
                start.send(message)
                message
            }

            val results : List<String> = messages.map { msg ->
                msg.response.await()
            }

            results.forEachIndexed { idx, result ->
                println("${words[idx]} -> $result")
            }
        }
    }

    println("time: $time ms")
}




/**
 *  An actor is an entity made up of a combination of
 *  - a coroutine
 *  - the state that is confined and encapsulated into this coroutine
 *  - a channel to communicate with other coroutines
 */
fun <T> createActor(block: suspend (T)->Unit): SendChannel<T> {
    return GlobalScope.actor {
        for (msg in channel) {
            block(msg)
        }
    }
}


class Message(val input: String, val response: CompletableDeferred<String>)  // a request with reply