package io.gank.tlc.dao;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.util.Date;

import io.gank.tlc.framework.data.InfoBase;

/**
 * Created by lipeng21 on 2015/11/27.
 */
@Table("video")
public class InfoDaoVideo extends InfoBase{
    @PrimaryKey(AssignType.BY_MYSELF)
    @Column("objectId")
    public String objectId;
    @Column("name")
    public String name;
    @Column("desc")
    public String desc;
    @Column("picUrl")
    public String picUrl;
    @Column("videoUrl")
    public String videoUrl;
    @Column("createTime")
    public Date createTime;
}
