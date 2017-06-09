package io.github.trylovecatch.baselibrary.mvp;

import android.os.Bundle;
import io.github.trylovecatch.baselibrary.BaseActivity;
import io.github.trylovecatch.baselibrary.rxjava.rxlifecycle.ILifecycleSubject;
import io.github.trylovecatch.baselibrary.rxjava.rxlifecycle.LifecycleEvent;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by lipeng21 on 2017/6/9.
 */

public abstract class MvpActivity<V extends IView, T extends BasePresenter<V>> extends BaseActivity
        implements ILifecycleSubject {

    protected final BehaviorSubject<LifecycleEvent> lifeSubject = BehaviorSubject.create();
    protected T mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState, int layoutResId) {
        lifeSubject.onNext(LifecycleEvent.CREATE);
        mPresenter = initPresenter();
        if(mPresenter!=null){
            mPresenter.attachView((V)this);
        }
        super.onCreate(savedInstanceState, layoutResId);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifeSubject.onNext(LifecycleEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifeSubject.onNext(LifecycleEvent.RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        lifeSubject.onNext(LifecycleEvent.PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        lifeSubject.onNext(LifecycleEvent.STOP);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifeSubject.onNext(LifecycleEvent.DESTORY);
        if(mPresenter!=null){
            mPresenter.detachView();
        }
    }

    @Override
    public Subject getSubject(){
        return lifeSubject;
    }

    protected abstract T initPresenter();
}
