package com.saket.kotlin_coroutines_and_threads

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.util.Random
import kotlin.system.measureTimeMillis


/**
 * Here i observe Coroutines and Suspension.
 */


/*
Launching coroutine using launch/async, will not block the underlying thread. Even
if the coroutine executes on the main thread. The program will continue to execute
its commands and later execute the coroutine tasks.
 */
fun coroutinesAreNonBlocking() {
    println("Launching coroutine")
    CoroutineScope(Dispatchers.Main).launch {
        repeat(20) {
            delay(100)
            println("Saket Dispatchers.Main launch on thread ${Thread.currentThread()}")
        }
    }
    for (i in 0..10) {
        Thread.sleep(200)
        println("Doing some work on thread: ${Thread.currentThread()} ")
    }
}

/*
With Suspend functions, Coroutines can suspend (pause) execution of a thread blocking (I/O) task
which is currently being executed. Instead, the CoroutineDispatcher will assign tasks from
another coroutine for the CPU to execute. When other coroutine tasks are executed,
then the Dispatcher will resume execution of the blocking task.

In below code, all tasks are to be executed on Main thread (Dispatchers.Main). So each
coroutine has to execute in order. Here first coroutine starts and runs in to the delay
function call.

The delay function simulates type of IO blocking task. Here the CPU is idle for delay
milliseconds. And the thread becomes blocked, as it can only send more instructions
to the CPU after delay finishes. So instead of blocking the main thread, the
CoroutineDispatcher suspends executing task from this coroutine and switches to executing
tasks from another coroutine. This is a more efficient use of the CPU time. The
coroutineDispatcher will keep switching between coroutines so that the thread is
not blocked to execute tasks from another coroutine.
*/
fun ioBlockingTaskWithCoroutines() {
    CoroutineScope(Dispatchers.Main).launch {
        println("Saket Launched coroutine 1 ")
        repeat(10) {
            delay(1000)
            println("Saket Dispatchers.Main launch on thread ${Thread.currentThread()}")
        }/*
        withContext(Dispatchers.Default) {
            println("Saket Dispatchers.Default launch scope - $this")
            println("Saket Dispatchers.Default launch context - ${this.coroutineContext}")
        }
         */
    }
    CoroutineScope(Dispatchers.Main).launch {
        println("Saket Launched coroutine 2")
        repeat(10) {
            delay(1000)
            println("Saket Dispatchers.Main launch2 on thread ${Thread.currentThread()}")
        }
    }
    CoroutineScope(Dispatchers.Main).launch {
        println("Saket Launched coroutine 3")
        repeat(10) {
            //delay(1000)
            println("Saket Dispatchers.Main launch3 on thread ${Thread.currentThread()}")
        }
    }
    CoroutineScope(Dispatchers.Main).launch {
        println("Saket Launched coroutine 4")
        repeat(10) {
            delay(300)
            println("Saket Dispatchers.Main launch4 on thread ${Thread.currentThread()}")
        }
    }
    CoroutineScope(Dispatchers.Main).launch {
        println("Saket Launched coroutine 5")
        repeat(10) {
            //delay(1000)
            println("Saket Dispatchers.Main launch5 on thread ${Thread.currentThread()}")
        }
    }
    CoroutineScope(Dispatchers.Main).launch {
        println("Saket Launched coroutine 6")
        repeat(10) {
            delay(1000)
            println("Saket Dispatchers.Main launch6 on thread ${Thread.currentThread()}")
        }
    }
}

/*
This is an example with CPU intensive task that blocks the Main thread.
In this case, when first coroutine sends task BigInteger.probablePrime(4096, Random())
to the CPU. The CPU is blocked because it becomes busy in executing the task,
so it cannot take any other task. So the thread is blocked here, and the
CoroutineDispatcher cannot switch to another thread.

So while CPU is working on first coroutine task, other coroutines have to wait for
their turn. Next, comes coroutine2, which like coroutine 1 again gives a CPU heavy
task to perform. So the CPU is busy and thread is blocked until CPU executes the
task from coroutine 2. After that coroutine 3, 4 and 5 do not ask CPU to do any
heavy work. So CoroutineDispatcher may suspend and switch between the tasks if
there is any chance to do so. Coroutine 6 task is similar to first 2 coroutines,
so here again, CPU gets busy and it will block the thread until CPU finishes
executing its task. Note that when switching tasks, the CoroutineDispatcher may
choose Coroutine 6. In which case the CPU will have to first execute Coroutine6
and then take tasks from others.

When using Dispatchers.Default, then Coroutine works with a threadpool which has
as many threads as CPU cores. So there is possibility for parallel execution of tasks.
In this case each coroutine can execute on a separate thread and things become more
flexible compared to using Dispatchers.Main. CoroutineDispatcher can also switch tasks
between Coroutines. Also UI is not affected by these operations, since main thread is
not used in this case.
 */
fun cpuBlockingTasksWithCoroutines() {
    CoroutineScope(Dispatchers.Main).launch {
        println("Saket Launched coroutine 1 ")
        BigInteger.probablePrime(4096, Random())
        println("Saket Dispatchers.Main launch on thread ${Thread.currentThread()}")
    }
    CoroutineScope(Dispatchers.Main).launch {
        println("Saket Launched coroutine 2")
        BigInteger.probablePrime(4096, Random())
        println("Saket Dispatchers.Main launch2 on thread ${Thread.currentThread()}")
    }
    CoroutineScope(Dispatchers.Main).launch {
        println("Saket Launched coroutine 3")
        repeat(10) {
            println("Saket Dispatchers.Main launch3 on thread ${Thread.currentThread()}")
        }
    }
    CoroutineScope(Dispatchers.Main).launch {
        println("Saket Launched coroutine 4")
        repeat(10) {
            delay(300)
            println("Saket Dispatchers.Main launch4 on thread ${Thread.currentThread()}")
        }
    }
    CoroutineScope(Dispatchers.Main).launch {
        println("Saket Launched coroutine 5")
        repeat(10) {
            println("Saket Dispatchers.Main launch5 on thread ${Thread.currentThread()}")
        }
    }
    CoroutineScope(Dispatchers.Main).launch {
        println("Saket Launched coroutine 6")
        BigInteger.probablePrime(4096, Random())
        println("Saket Dispatchers.Main launch6 on thread ${Thread.currentThread()}")
    }
}

//Coroutines are generally faster and more lightweight compared to threads..
fun run_coroutines() {
    val time = measureTimeMillis {
        /*
        Coroutines run within a scope. The Coroutine scope defines the lifetime of the coroutine.
        There are some pre-defined scopes:
        ViewModelScope
        LifeCycleScope
        GlobalScope
        MainScope

        But you can also define your own CoroutineScope using CoroutineScope().
         */
        CoroutineScope(context = Dispatchers.IO).launch {
            for (i in 1..100) {
                //Log.v("Saket", "Going to sleep on coroutine $i")
                Log.v("Saket", "Before delay $i - ${Thread.currentThread().name}")
                delay(1000)
                Log.v("Saket", "After delay $i - ${Thread.currentThread().name}")
                //Log.v("Saket", "Waking up on coroutine $i")
            }
        }

        //Below coroutine will execute on the current thread which is the main thread in this case.
/*            runBlocking {
            for (i in 1..1000) {
                launch {
                    //Log.v("Saket", "Going to sleep on coroutine $i")
                    Log.v("Saket", "Thread $i - ${Thread.currentThread().name}")
                    delay(10000)
                    //Log.v("Saket", "Waking up on coroutine $i")
                }
            }
        }*/
    }
}

