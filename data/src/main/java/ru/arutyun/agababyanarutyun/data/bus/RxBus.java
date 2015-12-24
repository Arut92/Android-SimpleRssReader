package ru.arutyun.agababyanarutyun.data.bus;


import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {

    private final Subject<Object, Object> mBus = new SerializedSubject<>(PublishSubject.create());

    @Inject
    public RxBus() {}

    public <T extends Object> Subscription register(final Class<T> eventType, Action1<T> onNext) {
        return mBus
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<Object, Boolean>() {
                    @Override
                    public Boolean call(Object arg0) {
                        return eventType.isInstance(arg0);
                    }
                })
                .map(new Func1<Object, T>() {
                    @Override
                    public T call(Object o) {
                        return (T) o ;
                    }
                })
                .subscribe(onNext);
    }

    public void post(Object event) {
        mBus.onNext(event);
    }

}
