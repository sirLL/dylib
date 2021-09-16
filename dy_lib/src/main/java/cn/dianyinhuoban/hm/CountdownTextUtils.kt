package cn.dianyinhuoban.hm

import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class CountdownTextUtils(lifecycleOwner: LifecycleOwner?) : LifecycleObserver {
    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    private val mCompositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    fun startCountdown(textView: TextView?, hintText: String) {
        mCompositeDisposable.add(
            Observable.interval(1, TimeUnit.SECONDS)
                .take(60)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val time = 59 - it
                    textView?.let { tv ->
                        if (time <= 0) {
                            tv.isEnabled = true
                            tv.text = hintText
                        } else {
                            tv.text = time.toString()
                            tv.isEnabled = false
                        }
                    }
                }, {
                    textView?.isEnabled = true
                    textView?.text = hintText
                })
        )
    }

    fun startCountdownDDHHMM(
        endTime: Long,
        dayView: TextView?,
        hourView: TextView?,
        minuteView: TextView?
    ) {
        val MINUTE = 60
        val HOUR = 60 * 60
        val DAY = 60 * 60 * 24

        val nowTime = Calendar.getInstance().timeInMillis
        if (nowTime >= endTime) {
            dayView?.text = "00"
            hourView?.text = "00"
            minuteView?.text = "00"
        } else {
            val totalSecond = (endTime - nowTime) / 1000
            if (totalSecond <= 0) {
                dayView?.text = "00"
                hourView?.text = "00"
                minuteView?.text = "00"
                return
            }
            mCompositeDisposable.add(
                Observable.interval(1, TimeUnit.SECONDS)
                    .take(totalSecond)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        val nowSecond = totalSecond - it - 1
                        if (nowSecond > 0) {
                            val day = nowSecond / DAY
                            val hour = nowSecond % DAY / HOUR
                            val second = nowSecond % MINUTE
                            val minute = if (second > 0) {
                                nowSecond % HOUR / MINUTE + 1
                            } else {
                                nowSecond % HOUR / MINUTE
                            }
                            val dayStr = if (day >= 10) {
                                day.toString()
                            } else {
                                "0${day}"
                            }
                            val hourStr = if (hour >= 10) {
                                hour.toString()
                            } else {
                                "0${hour}"
                            }
                            val minuteStr = if (minute >= 10) {
                                minute.toString()
                            } else {
                                "0${minute}"
                            }

                            dayView?.text = dayStr
                            hourView?.text = hourStr
                            minuteView?.text = minuteStr
                        } else {
                            dayView?.text = "00"
                            hourView?.text = "00"
                            minuteView?.text = "00"
                        }
                    }, {
                        dayView?.text = "--"
                        hourView?.text = "--"
                        minuteView?.text = "--"
                    })
            )
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        clear()
        owner.lifecycle.removeObserver(this)
    }

    fun clear() {
        mCompositeDisposable.clear()
    }

}