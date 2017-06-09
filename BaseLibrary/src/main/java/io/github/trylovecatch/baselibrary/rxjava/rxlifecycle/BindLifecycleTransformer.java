package io.github.trylovecatch.baselibrary.rxjava.rxlifecycle;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.Subject;

/**
 * Created by lipeng21 on 2017/6/12.
 */

public class BindLifecycleTransformer<T> implements ObservableTransformer<T, T> {
    private final Subject<LifecycleEvent> mLifeSubject;
    private LifecycleEvent mLifecycleEvent;

    public BindLifecycleTransformer(@NonNull Subject<LifecycleEvent> pLifeSubject,
                                    LifecycleEvent pLifecycleEvent){
        this.mLifeSubject = pLifeSubject;
        this.mLifecycleEvent = pLifecycleEvent;
    }

    @Override
    public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
        return upstream.takeUntil(mLifeSubject.skipWhile(new Predicate<LifecycleEvent>() {
            @Override
            public boolean test(@NonNull LifecycleEvent lifecycleEvent) throws Exception {
                boolean tResult = !lifecycleEvent.equals(mLifecycleEvent);
                return tResult;
            }
        }));
    }
}
