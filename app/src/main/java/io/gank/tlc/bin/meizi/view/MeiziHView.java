package io.gank.tlc.bin.meizi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;

import butterknife.Bind;
import io.gank.tlc.R;
import io.gank.tlc.dao.InfoDaoMeizi;
import io.gank.tlc.framework.data.InfoBase;
import io.gank.tlc.framework.event.EventBus;
import io.gank.tlc.framework.view.BaseHolderView;
import io.gank.tlc.share.event.EventMeiziClicked;

/**
 * Created by lipeng21 on 2015/11/10.
 */
public class MeiziHView extends BaseHolderView{
    @Bind(R.id.meizi_item_rlt)
    RelativeLayout mRlt;
    @Bind(R.id.meizi_item_img)
    ImageView mImg;

    public MeiziHView(Context pContext){
        super(pContext, R.layout.meizi_item);
//        mImg.setOriginalSize(50, 50);
    }

    @Override
    protected void bindData(InfoBase pInfo, final int pPosition) {
        final InfoDaoMeizi tInfo = (InfoDaoMeizi)pInfo;

//        Glide.with(mContext)
//                .load(tInfo.url)
//                .centerCrop()
//                .crossFade()
//                .into(mImg)
//                .getSize(new SizeReadyCallback() {
//                    @Override
//                    public void onSizeReady(int width, int height) {
//                        if(!mRlt.isShown()){
//                            mRlt.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });

        Glide.with(mContext)
                .load(tInfo.url)
                .asBitmap()
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ViewGroup.LayoutParams tParams = mImg.getLayoutParams();
                        tParams.height = Math.round(tParams.width * 1.0f * resource.getHeight() / resource.getWidth());
                        mImg.setImageBitmap(resource);
                    }
                })
                .getSize(new SizeReadyCallback() {
                    @Override
                    public void onSizeReady(int width, int height) {
                        if(!mRlt.isShown()){
                            mRlt.setVisibility(View.VISIBLE);
                        }
                    }
                });

        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.post(new EventMeiziClicked(pPosition, mImg));
            }
        });
    }

}
