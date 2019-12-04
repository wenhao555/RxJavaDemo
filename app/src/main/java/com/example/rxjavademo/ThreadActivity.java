package com.example.rxjavademo;

import androidx.appcompat.app.AppCompatActivity;
import rx.Observable;
import rx.schedulers.Schedulers;

import android.os.Bundle;

import java.util.concurrent.Callable;

/**
 * 线程控制
 */
public class ThreadActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        Observable.fromCallable(new Callable<Integer>()
        {
            @Override
            public Integer call() throws Exception
            {
                return null;
            }
        })
                .subscribeOn(Schedulers.immediate())//直接在当前线程运行，是timeout、timeInterval、timestamp默认调度器
                .subscribeOn(Schedulers.io())//（读写文件、读写数据库、网络信息交互）io比newThread更有效率
                .subscribeOn(Schedulers.newThread())//总是启用新县城，并在新县城执行操作
                .subscribeOn(Schedulers.computation())//计算所使用的的Scheduler，如图形计算，不要把I/O放在这里浪费Cpu，是buffer、debounce、delay
                //、interval、sample、skip操作符的默认调度器
                .subscribeOn(Schedulers.trampoline())//想在当前线程执行任务时，可以用此方法将它加入队列，shirepeat和retry的调度器
        ;
    }
}
