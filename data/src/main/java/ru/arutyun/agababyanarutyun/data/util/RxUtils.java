package ru.arutyun.agababyanarutyun.data.util;

import android.util.Log;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;


public final class RxUtils {
    private static final String TAG = RxUtils.class.getCanonicalName();

    private RxUtils() {}

    public static  <E> Observable<E> makeObservable(final Callable<E> func) {
        return Observable.create(
                new Observable.OnSubscribe<E>() {
                    @Override
                    public void call(Subscriber<? super E> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch(Exception ex) {
                            Log.e(TAG, ex.getMessage());
                        }
                    }
                });
    }

}
