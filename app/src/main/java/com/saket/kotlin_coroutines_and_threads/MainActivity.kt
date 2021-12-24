package com.saket.kotlin_coroutines_and_threads

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.system.measureTimeMillis
import android.util.Log
import android.view.View
import kotlinx.coroutines.*
import java.lang.Runnable

/**
 * The purpose of this sample app is to compare threads and coroutines in kotlin.
 *
 * Thread are good option to improve app's performance. But they are expensive, so they
 * need to be used carefully. Having too many threads compromises the app's behavior and
 * user experience.
 *
 * Coroutines are called lightweight threads. Actually coroutines use threadpool in the
 * background. Which ensures they use less memory and are much faster than threads.
 * Also coroutines can switch execution between threads. Finally, unlike threads,
 * coroutines are non-blocking. Instead they suspend operation on a thread. This allows
 * other threads to continue working until the current function is suspended.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    //Thread run() method is blocking since it runs on the current (main) thread.
    //However, start() runs on a separate thread and consumes less time and is non-blocking.
    /*Threads run within the app process. They share from the memory assigned to the process.
    More threads means more memory allocated to the threads.
    Also suppose, your device is a quad core device, then if you run 4 threads, chances are
    they get executed by the 4 cores simultaneously. However, if you run say 8 threads. Then
    while first 4 threads are processed by the 4 cores, the 5th thread onwards will have to wait
    until one of the cores is free. So even though your app has created 8 threads, it will not be
    able to immediately execute them. Which means they are holding memory while waiting to be
    executed by the processor. After execution, the threads are released from the memory.

    But coming back to the main point. If i run a single thread in the background for say 10 seconds,
    then it does not impact the behavior on the main thread .i.e. user is still able to interact
    with the app UI. But if i run say 10000 threads , then these threads take up memory from the
    app's process, leaving less memory for the main thread and rest of the app to execute. Also they
    use up all the CPU cores. Also the OS is having the overhead of managing these threads.
    In this case, even if you use Thread.start(), the app UI will start to become laggy and may even
    give ANR error. Or may even crash the app.
    */
    fun run_10k_threads(view: View) {
        val time = measureTimeMillis {
            for (i in 1..10000) {
                Thread(Runnable {
                    Log.v("Saket", "Going to sleep on thread $i")
                    Thread.sleep(10)
                    Log.v("Saket", "Waking up on thread $i")
                }).start()  //run() will execute on current thread whereas start() executes on worker thread..
            }
        }
    }

    //Coroutines are generally faster and more lightweight compared to threads..
    fun run_coroutines(view: View) {
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
            /*
            Coroutines use a thread pool where the jobs are executed on separate threads.
            It is also observed that the coroutine may switch execution from one thread before the
            delay() to another thread after the delay. This is very flexible.
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

}