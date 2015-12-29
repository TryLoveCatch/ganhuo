package io.gank.tlc.share.event;

import android.view.View;

import io.gank.tlc.framework.event.EventBase;

/**
 * Created by lipeng21 on 2015/11/26.
 */
public class EventHomeClicked extends EventBase{
    public int position;
    public View view;
    public EventHomeClicked(int pPosition, View pView){
        super();
        this.position = pPosition;
        this.view = pView;
    }
}
