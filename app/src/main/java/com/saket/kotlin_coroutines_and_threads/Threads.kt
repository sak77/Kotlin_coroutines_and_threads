package com.saket.kotlin_coroutines_and_threads

import android.util.Log
import kotlin.system.measureTimeMillis


/*
Threads are a sequence of instructions which are executed by the CPU.

They are blocking in nature. Which means if CPU is executing some tasks on a thread. And
maybe the task is maybe IO blocking in nature. Then the CPU will have to wait
until it gets a response. Then the CPU is waiting and no further instructions on the thread or
maybe other threads is being executed. This is blocking nature of Threads.

On contrary, in case of Coroutines, with use of suspend functions, the CoroutineDispatcher
can simply suspend (temporarily pause) a blocking operation, switch to tasks from another
Coroutine and resume the suspended function once CPU is free again.
 */

/*
Threads are expensive. Threads run within the app process. They share from the memory
assigned to the process. More threads means more memory allocated to the threads.
Also, setting up and tearing down threads each time will also have some overhead.
Instead, the thread-pool is seen as a better alternative. Coroutines work with a
thread-pool of shared threads, which can be re-used. Thus providing a lightweight alternative
when compared to threads.


10,000 threads vs 10,000 coroutines-
10,000 threads will engage all processors, there will be cpu overhead and memory
 consumption when creating/tearing down threads and also threads which are waiting to be
  executed will take up space in the memory. Since these threads run individually, the overall
  CPU and memory utilization is not very efficient. On top of that, due to blocking nature of
  threads, if any of the threads encounters a IO blocking task, then CPU cannot work on anything
  else while waiting for response and this leads to waste of CPU time and resources.

  On the other hand, Coroutines, run in a co-operative manner. With help of shared thread-pool,
  suspension and switching between Coroutines, it ensures much more efficient utilization of CPU
  time and resources. The re-usable threadpool will ensure that we dont need to have 10000 actual
  threads in the memory. As soon as a thread executes a task, it can be reused for next task.
  Less overhead, since threads are not being constantly created and destroyed.
*/

//Thread run() method is blocking since it runs on the current (main) thread.
//However, start() runs on a separate thread and consumes less time and is non-blocking.
fun run_10k_threads() {
    val time = measureTimeMillis {
        for (i in 1..10000) {
            Thread(Runnable {
                Log.v("Saket", "Going to sleep on thread $i")
                Thread.sleep(10)
                Log.v("Saket", "Waking up on thread $i")
            }).start()  //run() will execute on current thread whereas start()
            // executes on worker thread..
        }
    }
}
