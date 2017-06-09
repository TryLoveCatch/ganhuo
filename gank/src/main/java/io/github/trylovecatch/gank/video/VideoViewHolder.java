package io.github.trylovecatch.gank.video;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import io.github.trylovecatch.baselibrary.data.InfoBase;
import io.github.trylovecatch.baselibrary.list.BaseViewHolder;
import io.github.trylovecatch.gank.R;
import io.github.trylovecatch.gank.info.InfoGank;
import io.github.trylovecatch.gank.service.RouterServices;

/**
 * Created by lipeng21 on 2015/11/10.
 */
public class VideoViewHolder extends BaseViewHolder{
    @BindView(R.id.video_item_txt_name)
    TextView mTxtName;
    @BindView(R.id.video_item_txt_time)
    TextView mTxtTime;
    @BindView(R.id.video_item_txt_desc)
    TextView mTxtDesc;


    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd");

    public VideoViewHolder(Context pContext){
        super(pContext, R.layout.video_item);


    }

    @Override
    protected void bindData(InfoBase pInfo, final int pPosition) {
        final InfoGank tInfo = (InfoGank)pInfo;

        mTxtName.setText(tInfo.who);
        mTxtTime.setText(mSdf.format(tInfo.publishedAt));
        mTxtDesc.setText(tInfo.desc);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterServices.getInstance().getService(mContext)
                        .startVideoPlay(VideoPlayFragment.class, tInfo.url);
            }
        });
    }

}
