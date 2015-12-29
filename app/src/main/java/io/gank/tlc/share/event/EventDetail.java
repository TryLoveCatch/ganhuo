package io.gank.tlc.share.event;

import io.gank.tlc.bin.detail.InfoDetail;
import io.gank.tlc.framework.event.EventBase;

/**
 * Created by lipeng21 on 2015/11/26.
 */
public class EventDetail extends EventBase {
    public InfoDetail data;

    public EventDetail(InfoDetail data){
        super();
        this.data = data;
    }

    public EventDetail(boolean pResultSuc, String pResultMsg){
        super(pResultSuc, pResultMsg);
    }
}
