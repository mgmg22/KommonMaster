package com.luckyaf.kommon.component


import android.arch.lifecycle.MutableLiveData
import java.util.concurrent.atomic.AtomicBoolean
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.annotation.MainThread
import android.util.Log
/**
 * 类描述：
 * @author Created by luckyAF on 2019-08-21
 *
 */
class SingleLiveEvent<T> :MutableLiveData<T>(){
    private val tag = "SingleLiveEvent"

    private val mPending = AtomicBoolean(false)

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        if (hasActiveObservers()) {
            Log.w(tag, "Multiple observers registered but only one will be notified of changes.")
        }
        // Observe the internal MutableLiveData
        super.observe(owner, Observer<T>{
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
     fun call() {
        value = null
    }


}