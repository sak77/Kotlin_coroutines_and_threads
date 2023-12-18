This is first in series of projects focusing on Coroutines in Android. 

There is another project called ThreadpoolSample which can also be reviewed. 
But since its not super important for Coroutines, so this project can be considered as the first.

This app focuses on Blocking threads and Suspending Coroutines.

Threads hold sequence of instructions to be executed by CPU. There can be multiple threads 
running at a given time, to do work in parallel. However, threads may have tasks that can block 
the thread. 

A thread can get blocked due to either a CPU intensive operation or an IO blocking operation 
(eg. waiting for a response from remote service).

Parallelism vs Concurrency -
Parallel tasks run at the same time and can be completed independently of one another. 
Concurrent tasks are in progress at the same time. Which means, one task can start 
executing and then while it is waiting for a response, another task starts. So there are 
multiple tasks in _progress_ at a given time.

Threads are expensive, so they must be used carefully. Having too many threads
can eat into the app's process memory, which can affect its user experience.

Kotlin has concept of suspension wherein a suspend modifier is added to a
function that performs some thread blocking task. 

With Coroutines, when adding suspend modifier to a function, the CoroutineDispatcher 
is able to temporarily pause (suspend) operation of the function if it blocks the thread. 
Doing so, allows the CoroutineDispatcher to switch to executing tasks from another Coroutine 
instead. When the other tasks are executed and the CPU again becomes free, the CoroutineDispatcher 
will resume the suspended function. This allows for more efficient use of CPU time.

A suspended function does not block the thread from which it is called. Which means the 
calling thread is free to execute other operations while the suspend function does its work.

A suspend function allows to write asynchronous code in a sequential manner, which improves 
readability of the code when compared to callbacks based approach.

Suspend functions can call other suspend functions like withContext(). withContext() can move 
the execution of code in its scope to another Dispatcher. This allows to move code execution off 
the main thread and switch between thread or threadpools to execute the tasks.

Also, suspend functions are the backbone of Coroutines. Coroutines can call suspend
functions to perform async tasks in suspension.

But how do suspend functions work? How do they achieve this kind of behavior?
That will be explored in this project. Read in this sequence - 
MainActivity.kt - overview of blocking threads and suspend functions. Also touches upon some 
Coroutine classes to explain how Coroutines work with Suspension.
Threads.kt - Blocking nature of threads and how Coroutines are more efficient.
Suspension.kt - Suspend modifier and its benefits.
Coroutines.kt - How Coroutines work with suspension.
