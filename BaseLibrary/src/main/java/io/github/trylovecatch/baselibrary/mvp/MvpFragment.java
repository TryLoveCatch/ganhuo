package io.github.trylovecatch.baselibrary.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.github.trylovecatch.baselibrary.BaseFragment;
import io.github.trylovecatch.baselibrary.rxjava.rxlifecycle.ILifecycleSubject;
import io.github.trylovecatch.baselibrary.rxjava.rxlifecycle.LifecycleEvent;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by lipeng21 on 2017/6/9.
 */

public abstract class MvpFragment<V extends IView, T extends BasePresenter<V>> extends BaseFragment
        implements ILifecycleSubject {

    protected final BehaviorSubject<LifecycleEvent> lifeSubject = BehaviorSubject.create();
    protected T mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lifeSubject.onNext(LifecycleEvent.ATTCH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifeSubject.onNext(LifecycleEvent.CREATE);
        mPresenter = initPresenter();
        if(mPresenter!=null){
            mPresenter.attachView((V)this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, int layoutResId) {
        lifeSubject.onNext(LifecycleEvent.CREATE_VIEW);
        return super.onCreateView(inflater, container, savedInstanceState, layoutResId);
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
    public void onDestroyView() {
        super.onDestroyView();
        lifeSubject.onNext(LifecycleEvent.DESTORY_VIEW);
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
    public void onDetach() {
        super.onDetach();
        lifeSubject.onNext(LifecycleEvent.DETACH);
    }

    @Override
    public Subject getSubject(){
        return lifeSubject;
    }

    protected abstract T initPresenter();

}
