package io.gank.tlc.bin.detail.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import io.gank.tlc.R;
import io.gank.tlc.bin.net.Apis;
import io.gank.tlc.framework.data.InfoBase;
import io.gank.tlc.framework.event.EventBus;
import io.gank.tlc.framework.view.BaseHolderView;
import io.gank.tlc.share.event.EventDetailClicked;
import io.gank.tlc.share.info.InfoGank;

/**
 * Created by lipeng21 on 2015/11/10.
 */
public class DetailHView extends BaseHolderView{
    @Bind(R.id.text_item_txt_name)
    TextView mTxtName;
    @Bind(R.id.text_item_txt_time)
    TextView mTxtTime;
    @Bind(R.id.text_item_txt_desc)
    TextView mTxtDesc;

    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd");

    public DetailHView(Context pContext){
        super(pContext, R.layout.text_item);
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
                EventBus.post(new EventDetailClicked(tInfo.url, tInfo.type.equals(Apis.type_video)));
            }
        });


    }

}
