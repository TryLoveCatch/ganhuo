package io.gank.tlc.share.event;

import io.gank.tlc.framework.event.EventBase;

/**
 * Created by lipeng21 on 2015/11/26.
 */
public class EventVideoClicked extends EventBase{
    public int position;
    public EventVideoClicked(int pPosition){
        super();
        this.position = pPosition;
    }
}
