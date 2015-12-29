package io.gank.tlc.share.event;

import java.util.List;

import io.gank.tlc.dao.InfoDaoVideo;
import io.gank.tlc.framework.event.EventBase;

/**
 * Created by lipeng21 on 2015/11/26.
 */
public class EventVideo extends EventBase {
    public List<InfoDaoVideo> data;

    public EventVideo(List<InfoDaoVideo> data){
        super();
        this.data = data;
    }

    public EventVideo(boolean pResultSuc, String pResultMsg){
        super(pResultSuc, pResultMsg);
    }
}
