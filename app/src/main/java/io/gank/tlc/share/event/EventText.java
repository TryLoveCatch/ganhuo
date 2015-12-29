package io.gank.tlc.share.event;

import java.util.List;

import io.gank.tlc.dao.InfoDaoMeizi;
import io.gank.tlc.framework.event.EventBase;

/**
 * Created by lipeng21 on 2015/11/26.
 */
public class EventText extends EventBase {
    public List<InfoDaoMeizi> data;

    public EventText(List<InfoDaoMeizi> data){
        super();
        this.data = data;
    }

    public EventText(boolean pResultSuc, String pResultMsg){
        super(pResultSuc, pResultMsg);
    }
}
