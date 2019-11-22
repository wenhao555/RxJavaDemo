package com.example.rxjavademo;

import androidx.appcompat.app.AppCompatActivity;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Observable observable = Observable.just("is Next One", "is Next Two");
        Observable<String> observable1 = Observable.from(strings);
        observable.subscribe(subscriber);
        observable.subscribe(onNextAction);
        observable.subscribe(onNextAction, onErrorAction);
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
        Observable.interval(2, TimeUnit.SECONDS)
                .subscribe(subscriber);
    }//----------------------------------被观察者-----------------------------//

    Action1<String> onNextAction = new Action1<String>()
    {
        @Override
        public void call(String s)
        {
            Log.e("Rxjava", "call" + s);
        }
    };

    Action1<Throwable> onErrorAction = new Action1<Throwable>()
    {
        @Override
        public void call(Throwable throwable)
        {

        }
    };

    Action0 onCompletedAction = new Action0()
    {
        @Override
        public void call()
        {
            Log.e("Rxjava", "call");
        }
    };


    private String[] strings = {"is Next One", "is Next Two"};
    /**
     * 被观察者
     */
    Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>()
    {
        @Override
        public void call(Subscriber<? super String> subscriber)
        {
            subscriber.onNext("is Next One");
            subscriber.onNext("is Next Two");
            subscriber.onCompleted();
        }
    });
    /**
     * 观察者
     */
    Subscriber subscriber = new Subscriber<String>()
    {
        @Override
        public void onCompleted()
        {
            Log.e("Rxjava", "onCompleted");
/**
 *时间队列完结
 * Rxjava不仅把每个时间单独处理，其还会把它们看成一个队列，当不会有新的onNext发出时，
 *需要触发onCompleted方法作为完成标志
 */
        }

        @Override
        public void onError(Throwable e)
        {
            Log.e("Rxjava", "onError");
/**
 * 事件队列异常
 * 在事件处理过程中出现异常时，改方法被触发同事队列自动终止，不允许再有事件发出
 */
        }

        @Override
        public void onNext(String s)
        {
            Log.e("Rxjava", "onNext" + s);
/**
 * 普通事件
 * 将要处理的事件添加到事件队列中
 */
        }

        @Override
        public void onStart()
        {
            super.onStart();
            /**
             * 会在事件还未发送之前被调用，可以用于做一些准备工作。
             * 例如数据清零或重置。
             * 可选方法默认实现为空
             */
        }
    };

    /**
     * 当有简单功能时可以使用Observer来创建观察者
     */
    Observer<String> observer = new Observer<String>()
    {
        @Override
        public void onCompleted()
        {

        }

        @Override
        public void onError(Throwable e)
        {

        }

        @Override
        public void onNext(String s)
        {

        }
    };


}
