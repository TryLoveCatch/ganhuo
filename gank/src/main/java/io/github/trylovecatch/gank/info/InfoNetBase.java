package io.github.trylovecatch.gank.info;

import java.io.Serializable;

import io.github.trylovecatch.baselibrary.data.InfoBase;

/**
 * Created by lipeng21 on 2017/6/13.
 */

public class InfoNetBase<T> implements Serializable {

    public boolean error;
    public String errorMsg;
    public T results;

    public boolean isSuccess(){
        return !error;
    }
}
