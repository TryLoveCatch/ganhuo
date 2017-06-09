package io.github.trylovecatch.gank.service;

import android.text.TextUtils;
import io.github.trylovecatch.gank.info.InfoNetBase;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lipeng21 on 2017/6/13.
 */

public class RxjavaServices {

    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<InfoNetBase<T>, T> handleResult() {
        return new ObservableTransformer<InfoNetBase<T>, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<InfoNetBase<T>> upstream) {
                return upstream.flatMap(new Function<InfoNetBase<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull InfoNetBase<T> tInfoNetBase) throws Exception {
                        if (tInfoNetBase.isSuccess()) {
                            return createData(tInfoNetBase.results);
                        } else {
                            if(TextUtils.isEmpty(tInfoNetBase.errorMsg)){
                                return Observable.error(new Exception("获取数据失败啊！！！！"));
                            }else {
                                return Observable.error(new Exception(tInfoNetBase.errorMsg));
                            }
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };

    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> emitter){
                try {
                    emitter.onNext(data);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });

    }
}
