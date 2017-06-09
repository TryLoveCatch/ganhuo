package io.github.trylovecatch.gank.info;

import java.util.Date;

import android.support.annotation.Nullable;

/**
 * Created by lipeng21 on 2015/11/9.
 */
public class InfoGank extends InfoAppBase {

    public String _id;
    public Date createdAt;
    public String desc;
    @Nullable
    public String[] images;
    public Date publishedAt;
    public String source;
    public String type;
    public String url;
    public boolean used;
    public String who;
    @Nullable
    public Date updatedAt;
}
