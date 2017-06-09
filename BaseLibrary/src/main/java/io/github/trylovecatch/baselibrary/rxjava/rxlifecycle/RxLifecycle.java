package io.github.trylovecatch.baselibrary.rxjava.rxlifecycle;

import android.support.annotation.NonNull;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.Subject;

/**
 * Created by lipeng21 on 2017/6/12.
 */

public class RxLifecycle {
    private final Subject<LifecycleEvent> mLifeSubject;
    private RxLifecycle(@NonNull Subject<LifecycleEvent> pLifeSubject){
        this.mLifeSubject = pLifeSubject;
    }

    public static RxLifecycle bind(@NonNull ILifecycleSubject ILifeSubject){
        return new RxLifecycle(ILifeSubject.getSubject());
    }

    public ObservableTransformer toObservable(LifecycleEvent pEvent){
        return new BindLifecycleTransformer(mLifeSubject, pEvent);
    }
}
