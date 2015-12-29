package io.gank.tlc.bin.detail;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.gank.tlc.share.info.InfoAppBase;
import io.gank.tlc.share.info.InfoGank;

/**
 * Created by lipeng21 on 2015/11/10.
 */
public class InfoDetail extends InfoAppBase<InfoDetail.Data>{

    public static class Data{
        @SerializedName("iOS")
        public ArrayList<InfoGank> iosList;
        @SerializedName("Android")
        public ArrayList<InfoGank> androidList;
        @SerializedName("瞎推荐")
        public ArrayList<InfoGank> recommendList;
        @SerializedName("拓展资源")
        public ArrayList<InfoGank> expandList;
        @SerializedName("福利")
        public ArrayList<InfoGank> picList;
        @SerializedName("休息视频")
        public ArrayList<InfoGank> videoList;
        @SerializedName("App")
        public ArrayList<InfoGank> appList;
    }
}
