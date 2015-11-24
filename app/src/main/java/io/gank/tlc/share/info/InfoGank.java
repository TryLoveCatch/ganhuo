package io.gank.tlc.share.info;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

import java.util.Date;

import io.gank.tlc.framework.data.InfoBase;

/**
 * Created by lipeng21 on 2015/11/9.
 */
public class InfoGank extends InfoBase{

    @Column("who")
    public String who;
    @Column("publishedAt")
    public Date publishedAt;
    @Column("desc")
    public String desc;
    @Column("type")
    public String type;
    @Column("url")
    public String url;
    @Column("used")
    public boolean used;
    @PrimaryKey(AssignType.BY_MYSELF)
    @Column("objectId")
    public String objectId;
    @Column("createdAt")
    public Date createdAt;
    @Column("updatedAt")
    public Date updatedAt;
}
