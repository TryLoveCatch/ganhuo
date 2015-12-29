package io.gank.tlc.dao;

import com.litesuits.orm.db.annotation.Table;

import io.gank.tlc.share.info.InfoGank;

/**
 * Created by lipeng21 on 2015/11/27.
 */
@Table("msg")
public class InfoDaoText extends InfoGank {

    public static InfoDaoText createInfoDaoText(InfoDaoMeizi pInfo){
        InfoDaoText tInfo = new InfoDaoText();
        tInfo.createdAt = pInfo.createdAt;
        tInfo.desc = pInfo.desc;
        tInfo.objectId = pInfo.objectId;
        tInfo.publishedAt = pInfo.publishedAt;
        tInfo.type = pInfo.type;
        tInfo.updatedAt = pInfo.updatedAt;
        tInfo.url = pInfo.url;
        tInfo.used = pInfo.used;
        tInfo.who = pInfo.who;
        return tInfo;
    }
}
