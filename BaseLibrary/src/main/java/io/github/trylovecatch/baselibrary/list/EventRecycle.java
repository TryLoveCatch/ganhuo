package io.github.trylovecatch.baselibrary.list;

import java.util.List;

import android.support.annotation.Nullable;
import io.github.trylovecatch.baselibrary.data.InfoBase;
import io.github.trylovecatch.baselibrary.eventbus.EventBase;

/**
 * Created by lipeng21 on 2017/6/14.
 */

public class EventRecycle extends EventBase{
    public boolean isFromCache = false;
    public List<? extends InfoBase> infos;

    public EventRecycle(boolean pIsSuc, @Nullable String pErrorMsg, boolean pIsFromCache) {
        super(pIsSuc, pErrorMsg);
        isFromCache = pIsFromCache;
    }
}
