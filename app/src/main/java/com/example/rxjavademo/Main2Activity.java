package com.example.rxjavademo;

import androidx.appcompat.app.AppCompatActivity;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

import android.os.Bundle;

import java.util.Observable;
import java.util.concurrent.Callable;

public class Main2Activity extends AppCompatActivity
{
    private PublishSubject publishSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        publishSubject = PublishSubject.create();
    }

    private void initSubject()
    {
        publishSubject.fromCallable(new Callable<String>()
        {
            @Override
            public String call() throws Exception
            {
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>()
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
                });
    }
}
