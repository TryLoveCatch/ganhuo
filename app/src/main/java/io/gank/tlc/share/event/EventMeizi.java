package io.gank.tlc.share.event;

import java.util.List;

import io.gank.tlc.dao.InfoDaoMeizi;
import io.gank.tlc.framework.event.EventBase;

/**
 * Created by lipeng21 on 2015/11/26.
 */
public class EventMeizi extends EventBase {
    public List<InfoDaoMeizi> data;

    public EventMeizi(List<InfoDaoMeizi> data){
        super();
        this.data = data;
    }

    public EventMeizi(boolean pResultSuc, String pResultMsg){
        super(pResultSuc, pResultMsg);
    }
}
