package io.gank.tlc.share.info;

import com.google.gson.annotations.SerializedName;

import io.gank.tlc.framework.data.InfoBase;

/**
 * Created by lipeng21 on 2015/11/10.
 */
public class InfoAppBase<T> extends InfoBase{

    @SerializedName("error")
    public boolean error;
    @SerializedName("results")
    public T results;
}
