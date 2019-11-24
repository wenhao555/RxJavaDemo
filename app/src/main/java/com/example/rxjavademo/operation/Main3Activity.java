package com.example.rxjavademo.operation;

import androidx.appcompat.app.AppCompatActivity;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func0;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjavademo.R;

import java.util.concurrent.TimeUnit;

public class Main3Activity extends AppCompatActivity {
    private final String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Log.e(TAG, "start:");
//        intervalOb();
        timerOb();
    }

    private void intervalOb() {
        Observable.interval(3, TimeUnit.SECONDS)//每三秒输出一次数据 数据依次递增
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.e(TAG, "interval:" + aLong.intValue());
                    }
                });
    }

    private void rangeOb() {
        Observable.range(5, 5)//起始是5 递增5次 输出为5~9
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG, "range:" + integer.intValue());
                    }
                });
    }

    private void repeatOb() {
        Observable.range(0, 5)
                .repeat(2)//重复次数，现在是重复两次
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG, "repeat:" + integer.intValue());
                    }
                });
    }

    private void timerOb() {
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "complete:");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Throwable:");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e(TAG, "interval:" + aLong.intValue());
                    }
                });
    }
}
