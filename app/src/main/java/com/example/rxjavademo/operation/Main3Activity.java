package com.example.rxjavademo.operation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjavademo.R;
import com.example.rxjavademo.model.Swordsman;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Main3Activity extends AppCompatActivity
{
    private final String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Log.e(TAG, "start:");
        grouupBy();
    }

    private void intervalOb()
    {
        Observable.interval(3, TimeUnit.SECONDS)//每三秒输出一次数据 数据依次递增
                .subscribe(new Action1<Long>()
                {
                    @Override
                    public void call(Long aLong)
                    {
                        Log.e(TAG, "interval:" + aLong.intValue());
                    }
                });
    }

    private void rangeOb()
    {
        Observable.range(5, 5)//起始是5 递增5次 输出为5~9
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        Log.e(TAG, "range:" + integer.intValue());
                    }
                });
    }

    private void repeatOb()
    {
        Observable.range(0, 5)
                .repeat(2)//重复次数，现在是重复两次
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        Log.e(TAG, "repeat:" + integer.intValue());
                    }
                });
    }

    private void timerOb()
    {
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>()
                {
                    @Override
                    public void onCompleted()
                    {
                        Log.e(TAG, "complete:");
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.e(TAG, "Throwable:");
                    }

                    @Override
                    public void onNext(Long aLong)
                    {
                        Log.e(TAG, "interval:" + aLong.intValue());
                    }
                });
    }

    //-----------------------------变换操作符----------------------------//
    String Host = "www.baidu.com";

    private void mapOb()
    {
        Observable.just("www.qq.com").map(new Func1<String, String>()
        {//拼接成新的字符串
            @Override
            public String call(String s)
            {
                return Host + "//" + s;
            }
        }).subscribe(new Action1<String>()
        {
            @Override
            public void call(String s)
            {//输出新的字符串
                Log.e(TAG, "map:" + s);
            }
        });
    }

    private void flatOb()
    {
        final List<String> mList = new ArrayList<>();
        mList.add("111111");
        mList.add("222222");
        mList.add("333333");
        mList.add("444444");
        mList.add("555555");
        mList.add("666666");
        mList.add("777777");
        mList.add("888888");
        mList.add("999999");
        Observable.from(mList).flatMap(new Func1<String, Observable<?>>()
        {
            @Override
            public Observable<?> call(String s)
            {//将Host循环拼接11111,2222,33333,。循环次数为size
                return Observable.just(Host + s);
            }
        }).cast(String.class)//强制转换的数据类型
                .subscribe(new Action1<String>()
                {
                    @Override
                    public void call(String s)
                    {
                        Log.e(TAG, "flag:" + s);//输出拼接结果

                    }
                });
    }

    private void caoncatOb()
    {
        final List<String> mList = new ArrayList<>();
        mList.add("111111");
        mList.add("222222");
        mList.add("333333");
        Observable.from(mList).concatMap(new Func1<String, Observable<?>>()
        {
            @Override
            public Observable<?> call(String s)
            {
                return Observable.just(Host + s);
            }
        }).cast(String.class)
                .subscribe(new Action1<String>()
                {
                    @Override
                    public void call(String s)
                    {
                        Log.e(TAG, "caoncat:" + s);//输出拼接结果
                    }
                });
    }

    private void bufferOb()
    {
        Observable.just(1, 2, 3, 5, 6, 7)
                .buffer(3)//缓存为3  每次发送三个
                .subscribe(new Action1<List<Integer>>()
                {
                    @Override
                    public void call(List<Integer> integers)
                    {
                        for (Integer i : integers)
                        {
                            Log.e(TAG, "buffer:" + i);//输出拼接结果

                        }
                        Log.e(TAG, "------------------------");//输出拼接结果
                    }
                });
    }

    private void windowOb()
    {
        Observable.just(1, 2, 3, 5, 6, 7)
                .window(3)
                .subscribe(new Action1<Observable<Integer>>()
                {
                    @Override
                    public void call(Observable<Integer> integerObservable)
                    {
                        integerObservable.subscribe(new Action1<Integer>()
                        {
                            @Override
                            public void call(Integer integer)
                            {
                                Log.e(TAG, "window:" + integer);//输出拼接结果
                            }
                        });
                    }
                });
    }

    private void grouupBy()
    {
        Swordsman s1 = new Swordsman("1", "A");
        Swordsman s2 = new Swordsman("2", "SS");
        Swordsman s3 = new Swordsman("3", "S");
        Swordsman s4 = new Swordsman("4", "S");
        Swordsman s5 = new Swordsman("5", "A");
        Swordsman s6 = new Swordsman("6", "SS");
        Swordsman s7 = new Swordsman("7", "S");
        Swordsman s8 = new Swordsman("8", "A");
        Observable<GroupedObservable<String, Swordsman>> groupedObservableObservable
                = Observable.just(s1, s2, s3, s4, s5, s6, s7, s8)
                .groupBy(new Func1<Swordsman, String>()
                {
                    @Override
                    public String call(Swordsman swordsman)
                    {
                        return swordsman.getLevel();//将相同等级的进行分组
                    }
                });
        Observable.concat(groupedObservableObservable)
                .subscribe(new Action1<Swordsman>()
                {
                    @Override
                    public void call(Swordsman swordsman)
                    {
                        Log.e(TAG, "grouupBy:" + swordsman.getName() + "/-----/" + swordsman.getLevel());//输出拼接结果  ;
                    }
                });
    }
}