package io.github.trylovecatch.baselibrary.rxjava;

import io.github.trylovecatch.baselibrary.application.BaseApplication;
import io.github.trylovecatch.baselibrary.utils.UtilNet;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by lipeng21 on 2017/6/13.
 */

public abstract class RxErrorObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {
        _onNext(t);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        if (!UtilNet.getInstance().isNetAvailable(BaseApplication.mContext)) {
            _onError("网络不可用");
        } else {
//            _onError("请求失败，请稍后再试...");
            _onError(e.getMessage());
        }
    }
//
    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);

}
