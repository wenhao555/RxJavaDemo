package com.example.rxjavademo.operation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjavademo.R;
import com.example.rxjavademo.model.Swordsman;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Main3Activity extends AppCompatActivity
{
    private final String TAG = getClass().getName();


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
                        Log.e(TAG, "timerOb:" + aLong.intValue());
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

    //------------------------------------过滤操作符-------------------------------------//
    private void filterOb()
    {
        Observable.just(1, 2, 3, 4).filter(new Func1<Integer, Boolean>()
        {
            @Override
            public Boolean call(Integer integer)
            {
                return integer > 2;//将大于二的数值提交给订阅者
            }
        }).subscribe(new Action1<Integer>()
        {
            @Override
            public void call(Integer integer)
            {
                Log.e(TAG, "filter:" + integer);//输出拼接结果
            }
        });
    }

    private void elementAtOb()
    {
        Observable.just(1, 2, 2, 4)
//                .elementAt(2)//下标从0开始
                .elementAtOrDefault(0, 3)//返回指定位置的值，如果超出下标则返回默认值不抛出异常
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        Log.e(TAG, "elementAt:" + integer);//输出拼接结果
                    }
                });
    }

    private void distinctOb()
    {
        Observable.just(3, 3, 3)
                .distinctUntilChanged()
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        Log.e(TAG, "distinct:" + integer);//输出拼接结果
                    }
                });
    }

    private void skitOb()
    {
        Observable.just(1, 2, 3, 4, 5, 6)
//                .skip(2)//从第3位去
//                .take(2)//取前两位
//                .skipLast(2)//取倒数第二位前的数据
                .takeLast(2)//取后两位
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        Log.e(TAG, "skit:" + integer);//输出拼接结果
                    }
                });
    }

    private void ignoreElements()
    {
        Observable.just(1, 2, 3, 4, 5)
                .ignoreElements()//忽略所有的操作结果只要C和ONe
                .subscribe(new Observer<Integer>()
                {
                    @Override
                    public void onCompleted()
                    {
                        Log.e(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.e(TAG, "onError");
                    }

                    @Override
                    public void onNext(Integer integer)
                    {
                        Log.e(TAG, "onNext" + integer);
                    }
                });
    }

    private void throttleFirst()
    {
        Observable.create(new Observable.OnSubscribe<Integer>()
        {//创建一个被观察者
            @Override
            public void call(Subscriber<? super Integer> subscriber)
            {
                for (int i = 0; i < 10; i++)
                {
                    subscriber.onNext(i);//继续发送下一个
                    try
                    {
                        Thread.sleep(100);//睡100ms，如果补睡眠则只有第一个数据会被发射
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();//完成
            }
        }).throttleFirst(200, TimeUnit.MICROSECONDS)//每200ms
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        Log.e(TAG, "throttleFirst" + integer);
                    }
                });
    }

    private void throttleWithTimeOut()
    {
        Observable.create(new Observable.OnSubscribe<Integer>()
        {
            @Override
            public void call(Subscriber<? super Integer> subscriber)
            {
                for (int i = 0; i < 10; i++)
                {
                    subscriber.onNext(i);
                    int sleep = 100;
                    if (i % 3 == 0)
                    {
                        sleep = 300;
                    }
                    try
                    {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).throttleWithTimeout(200, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        Log.e(TAG, "throttleWithTimeOut" + integer);
                    }
                });
    }

    //-------------------------------------------------------组合操作符-------------------------------//3
    private void startWith()
    {
        Observable.just(3, 4, 5)
                .startWith(1, 2)//在最前面插入1 2
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        Log.e(TAG, "startWith" + integer);
                    }
                });
    }

    private void merge()
    {
        Observable<Integer> observable = Observable.just(1, 2, 3).subscribeOn(Schedulers.io());
        Observable<Integer> observable1 = Observable.just(3, 4);
        Observable.merge(observable, observable1)//无序合并
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        Log.e(TAG, "merge" + integer);
                    }
                });
    }

    private void concat()
    {
        Observable<Integer> observable = Observable.just(1, 2, 3).subscribeOn(Schedulers.io());
        Observable<Integer> observable1 = Observable.just(3, 4);
        Observable.concat(observable, observable1)//有序合并
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        Log.e(TAG, "concat" + integer);
                    }
                });
    }

    private void zipOb()
    {
        Observable<Integer> observable = Observable.just(1, 2, 3);
        Observable<String> observable1 = Observable.just("a", "b", "c");
        Observable.zip(observable, observable1, new Func2<Integer, String, String>()
        {
            @Override
            public String call(Integer integer, String s)
            {
                return integer + s;
            }
        }).subscribe(new Action1<String>()
        {
            @Override
            public void call(String s)
            {
                Log.e(TAG, "zipOb::" + s);

            }
        });

    }

    private void combineLastest()
    {
        Observable<Integer> observable = Observable.just(1, 2, 3, 5, 6, 7);
        Observable<String> observable1 = Observable.just("a", "b", "c");
        Observable.combineLatest(observable, observable1, new Func2<Integer, String, String>()
        {//该方法将第一个Ob发射的最后一个数据与第二个Ob发射的每个数据相结合
            @Override
            public String call(Integer integer, String s)
            {
                return integer + s;
            }
        }).subscribe(new Action1<String>()
        {
            @Override
            public void call(String s)
            {
                Log.e(TAG, "combineLastest:" + s);
            }
        });
    }

    //-------------------------------------------------------辅助操作符-----------------------------------------//
    private void delayOb()
    {
        Observable.create(new Observable.OnSubscribe<Long>()
        {
            @Override
            public void call(Subscriber<? super Long> subscriber)
            {
                Long currentTime = System.currentTimeMillis() / 1000;
                subscriber.onNext(currentTime);
            }
        }).delay(3, TimeUnit.SECONDS)//几秒后发送
                .subscribe(new Action1<Long>()
                {
                    @Override
                    public void call(Long aLong)
                    {
                        Log.e(TAG, "delayOb:" + (System.currentTimeMillis() / 1000 - aLong));//当前时间减去发送时间
                    }
                });
    }

    /**
     * doOnEach          为Observable注册一个回调，当Observable每发射一项数据时就会调用它一次，包括onNext onError，onComplete
     * doOnNext          只有执行onNext时才被调用
     * doOnSubscriber    当观察者订阅Observable时被调用
     * doUnOnSubscriber  取消订阅Observable时会被调用，Observable通过OnError或者onCompleted结束时，会取消订阅虽有的Subscriber。
     * doOnCompleted     当Observable正常终止调用OnComplete时会调用
     * doOnError         当Observable异常终止调用onError时会被调用
     * doOnTerminate     当Observable终止 前 就会调用
     * finally           当终止 后 就会被调用
     */
    private void doOb()
    {
        Observable.just(1, 2)
                .doOnNext(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        Log.e(TAG, "integer:" + integer);
                    }
                }).subscribe(new Subscriber<Integer>()
        {
            @Override
            public void onCompleted()
            {
                Log.e(TAG, "onCompleted:");
            }

            @Override
            public void onError(Throwable e)
            {
                Log.e(TAG, "Throwable:");
            }

            @Override
            public void onNext(Integer integer)
            {
                Log.e(TAG, "onNext:" + integer);
            }
        });

    }

    private void subscribeOn()
    {
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>()//创建一个被观察者
        {
            @Override
            public void call(Subscriber<? super Integer> subscriber)
            {
                Log.e(TAG, "Observable：" + Thread.currentThread().getName());//获取当前线程的名字
                subscriber.onNext(1);
                subscriber.onCompleted();
            }
        });
        observable.subscribeOn(Schedulers.newThread())//表示运行在新开的线程
                .observeOn(AndroidSchedulers.mainThread())//表示运行在主线程
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        Log.e(TAG, "Observ：" + Thread.currentThread().getName());//获取当前线程的名字
                    }
                });
    }

    private void timeoutOb()
    {
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>()
        {
            @Override
            public void call(Subscriber<? super Integer> subscriber)
            {
                for (int i = 0; i < 4; i++)
                {
                    try
                    {
                        Thread.sleep(i * 100);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        }).timeout(200, TimeUnit.MILLISECONDS, Observable.just(10, 11));//执行新一个Observable
        observable.subscribe(new Action1<Integer>()
        {
            @Override
            public void call(Integer integer)
            {
                Log.e(TAG, "timeoutOb:" + integer);
            }
        });
    }

    //------------------------------------------错误处理操作符--------------------------------------//

    /**
     * onErrorReturn：Observable遇到错误时返回原来的Observable行为的备用Observable，
     * 备用Observable会忽略原有Observable的onError调用，不会将错误传递给观察者。
     * 作为替代，它会发射一个特殊的项并调用观察者的onCompleted。
     * onErrorRsumeNext：Observable遇到错误时返回原有Observable行为的备用Observable
     * 它会发射一个特殊的项并调用观察者的Observable的数据。
     * onExceptionResumeNext：如果收到的Throwable不是一个Exception，它会将错误传递给观察者的onError方法
     */
    private void catchOb()
    {
        Observable.create(new Observable.OnSubscribe<Integer>()
        {
            @Override
            public void call(Subscriber<? super Integer> subscriber)
            {
                for (int i = 0; i < 5; i++)
                {
                    if (i > 2)
                    {//当i大于2的时候发送错误
                        subscriber.onError(new Throwable("Throwable"));
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        }).onErrorReturn(new Func1<Throwable, Integer>()
        {
            @Override
            public Integer call(Throwable throwable)
            {
                return 6;//接收到onError的时候发送一个备用信息给Observable
            }
        }).subscribe(new Observer<Integer>()
        {
            @Override
            public void onCompleted()
            {
                Log.e(TAG, "onCompleted:");
            }

            @Override
            public void onError(Throwable e)
            {
                Log.e(TAG, "onError:" + e.getMessage());//得到报错信息
            }

            @Override
            public void onNext(Integer integer)
            {
                Log.e(TAG, "onNext:" + integer);
            }
        });

    }

    /**
     * retry
     * retryWhen
     */
    private void retryOb()
    {
        Observable.create(new Observable.OnSubscribe<Integer>()
        {
            @Override
            public void call(Subscriber<? super Integer> subscriber)
            {
                for (int i = 0; i < 5; i++)
                {
                    if (i == 1)
                    {
                        subscriber.onError(new Throwable("Throwable"));
                    } else
                    {
                        subscriber.onNext(i);
                    }
                }
                subscriber.onCompleted();
            }
        }).retry(2)//设置重新订阅的次数，实际订阅次数为该次数+1，因为最开始就订阅了一次
                .subscribe(new Observer<Integer>()
                {

                    @Override
                    public void onCompleted()
                    {
                        Log.e(TAG, "onCompleted:");
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.e(TAG, "onError:" + e.getMessage());//得到报错信息
                    }

                    @Override
                    public void onNext(Integer integer)
                    {
                        Log.e(TAG, "onNext:" + integer);
                    }
                });
    }

    //--------------------------------------条件操作符和布尔操作符-----------------------------------------//
    private void allOb()
    {
        Observable.just(1, 2, 3, 4)
                .all(new Func1<Integer, Boolean>()
                {
                    @Override
                    public Boolean call(Integer integer)
                    {
                        Log.e(TAG, "call:" + integer);//输出所有小于3的数
                        return integer < 3;//小于3的数
                    }
                }).subscribe(new Subscriber<Boolean>()
        {
            @Override
            public void onCompleted()
            {
                Log.e(TAG, "onCompleted:");
            }

            @Override
            public void onError(Throwable e)
            {
                Log.e(TAG, "onError:" + e.getMessage());//得到报错信息
            }

            @Override
            public void onNext(Boolean aBoolean)
            {
                Log.e(TAG, "onNext:" + aBoolean);
            }
        });
    }

    private void contains_isEmptyOb()
    {
        Observable.just(1, 2, 3, 4, 5)
                .contains(1)//查看是否包含某一个数据 现在是查看是否包含1
                .subscribe(new Action1<Boolean>()
                {
                    @Override
                    public void call(Boolean aBoolean)
                    {
                        Log.e(TAG, "callContains:" + aBoolean);//包含则返回true 不包含则返回false
                    }
                });
        Observable.just(1, 2, 3, 4)
                .isEmpty()//用来判断是否发射过数据
                .subscribe(new Action1<Boolean>()
                {
                    @Override
                    public void call(Boolean aBoolean)
                    {
                        Log.e(TAG, "callisEmpty:" + aBoolean);//包含则返回true 不包含则返回false
                    }
                });
    }

    //----------------------------------条件操作符--------------------------------//
    private void amb()
    {
        Observable.amb(//只会发射首先发射出来的数据，由于第一个Observable延迟两秒发射，所以不会发射
                Observable
                        .just(1, 2)//第一个被观察者发射数据
                        .delay(2, TimeUnit.SECONDS), Observable.just(3, 4, 5))
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        Log.e(TAG, "amb:" + integer);
                    }
                });
    }

    private void defaultEmptyOb()
    {
        Observable.create(new Observable.OnSubscribe<Integer>()
        {
            @Override
            public void call(Subscriber<? super Integer> subscriber)
            {
                subscriber.onCompleted();
            }
        }).defaultIfEmpty(3)//由于原始Observable没有发射数据则会发射一个指定数据3
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        Log.e(TAG, "defaultIfEmpty:" + integer);
                    }
                });
    }

    //----------------------------------------------------转换操作符-----------------------------------------//
    private void toList()
    {
        Observable.just(1, 2, 3, 4)
                .toList()//将所有数据转换成List集合
                .subscribe(new Action1<List<Integer>>()
                {
                    @Override
                    public void call(List<Integer> integers)
                    {
                        Log.e(TAG, "toList:" + integers);
                    }
                });
    }

    private void toSortedList()
    {
        Observable.just(3, 7, 6, 1)
                .toSortedList()
                .subscribe(new Action1<List<Integer>>()
                {
                    @Override
                    public void call(List<Integer> integers)
                    {
                        Log.e(TAG, "toList:" + integers);
                    }
                });
    }

    private void toMapOb()
    {
        Swordsman swordsman1 = new Swordsman("1111", "aaa");
        Swordsman swordsman2 = new Swordsman("2222", "SS");
        Swordsman swordsman3 = new Swordsman("3333", "X");
        Observable.just(swordsman1, swordsman2, swordsman3)
                .toMap(new Func1<Swordsman, String>()
                {
                    @Override
                    public String call(Swordsman swordsman)
                    {
                        return swordsman.getLevel();
                    }
                }).subscribe(new Action1<Map<String, Swordsman>>()
        {
            @Override
            public void call(Map<String, Swordsman> stringSwordsmanMap)
            {
                Log.e(TAG, "toMapOb:" + stringSwordsmanMap.get("SS").getName());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Log.e(TAG, "start:");
        toMapOb();
    }
}