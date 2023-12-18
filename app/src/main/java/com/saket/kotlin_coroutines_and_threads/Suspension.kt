package com.saket.kotlin_coroutines_and_threads

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import java.util.Random

/**
 * Introduction to Suspend modifier.
 */

/*
Suspend functions can only be called by Coroutines or other
suspend functions.
 */
fun invokeSuspendFunction() {
    CoroutineScope(Dispatchers.Main).launch {
        findBigPrimeSuspend()
    }
}

/*
Merely adding suspend modifier to a function name does not
make it implement suspension convention. In this case one gets a warning
of redundant 'suspend' modifier and the operation is performed
on the calling thread .i.e. main thread in this case.
 */
private suspend fun findBigPrimeSuspend() {
    BigInteger.probablePrime(4096, Random())
}

/*
Adding suspend modifier provides 2 benefits.
First, it allows to call other suspend functions like withContext.

Because, Suspend functions can only be called by other suspend
functions or Coroutines. In this case, since findBigPrime is
a suspend function, it can then invoke suspend function withContext().

Here Dispatchers.Default is passed to withContext method. This executes
the block of code on a separate worker thread. So now we get async operation
with suspend keyword.

If one uses Dispatchers.Main instead, then the task is performed on the
main thread, which again causes UI freeze.

The other benefit, is to be able to write async code in a linear fashion
without relying on callbacks. This improves code readability.
*/
private suspend fun findBigPrimeSuspend2() {
    return withContext(Dispatchers.Default) {
        println("Saket findBigPrime is on thread ${Thread.currentThread()}")
        //blocking task
        val result = BigInteger.probablePrime(4096, Random())
        //few more blocking tasks
        repeat(5) {
            delay(1000)
            println("Saket doing some work on thread ${Thread.currentThread()}")
        }
    }
}
