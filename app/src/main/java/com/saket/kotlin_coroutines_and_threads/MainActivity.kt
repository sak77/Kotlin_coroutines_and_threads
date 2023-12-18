package com.saket.kotlin_coroutines_and_threads

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.system.measureTimeMillis
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.saket.kotlin_coroutines_and_threads.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.lang.Runnable
import java.math.BigInteger
import java.util.Random
import kotlin.coroutines.CoroutineContext

/**
 * Threads can be blocked in 2 ways - CPU intensive task or IO task.
 * For CPU intensive tasks, the thread becomes blocked because CPU is busy performing
 * some operation. On multi-core machines, the way to work around this situation would
 * be to have a thread pool which can then dispatch tasks on separate threads to other
 * processors. On single processor machine, once CPU is busy then other tasks will have
 * to wait until its free.
 * For IO tasks, CPU is only waiting for some external response. In this case CPU time
 * is wasted.
 * Blocking task on main thread will freeze the UI
 * Blocking task on worker thread. The UI may not be affected, but blocked thread
 * cannot be used to perform any other operation, which means CPU resource/time wastage.
 * Enter Coroutines and Suspension.
 * Suspension allows for tasks on IO blocked thread to be temporarily paused (suspended).
 * Instead the CoroutineDispatcher will switch to tasks from other Coroutines to the processor.
 * Once processor is free again, the CoroutineDispatcher will resume execution of suspended
 * function.
 * For CPU blocked threads, one can to use Dispatchers.Default CoroutineContext, which provides
 * a thread-pool with as many threads as CPUs. It is considered as most efficient way to
 * work with CPU intensive tasks. In this case, if one thread becomes blocked, the dispatcher
 * can use other threads to execute execute tasks in parallel. Remember, for IO blocked threads,
 * the dispatcher may still switch between them if required.
 * For IO blocking tasks, Coroutines prefer to use Dispatchers.IO CoroutineContext. This provides
 * a thread-pool with up to 64 tasks or max number of cores if its value is higher.
 * Under the hood, both Dispatchers.Default and Dispatchers.IO share the same thread-pool.
 * This thread-pool is setup for each process. So it is possible that when one sets up
 * Dispatcher.IO, then the first few threads will be the same ones as used for Dispatchers.Default.
 * On top of it, the new threads are created in the shared thread-pool.
 * Each Suspend function executes in a linear fashion. So it allows to write async code in a
 * linear fashion without any callbacks. This makes it easy to read.
 * Coroutines are called lightweight threads. This is mainly because they can work with thread-pools,
 * which reduces the overhead required to create and destroy threads.
 *
 * In this example, i will look at different blocking operations and how they behave with threads
 * and suspending functions with Coroutines.
 *
 * NOTE: Start at Threads.kt, then Suspension.kt and finally Coroutines.kt
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentThread = Thread.currentThread()
        currentThread.isDaemon
        binding.button.setOnClickListener {
            //val bigInteger = findBigPrimeOnMainThread()
            //binding.resultText.text = bigInteger.toString()

            //run_10k_threads()

            //invokeSuspendFunction()

            //coroutinesAreNonBlocking()

        }
    }

    /* Executed on main thread. Blocks UI. */
    fun findBigPrimeOnMainThread(): BigInteger {
        println("Saket findBigPrime is on thread ${Thread.currentThread()}")
        return BigInteger.probablePrime(4096, Random())
    }
}