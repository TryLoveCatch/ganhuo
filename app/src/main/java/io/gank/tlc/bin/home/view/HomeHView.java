package io.gank.tlc.bin.home.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import butterknife.Bind;
import io.gank.tlc.R;
import io.gank.tlc.bin.photo.PhotoActivity;
import io.gank.tlc.dao.InfoDaoMeizi;
import io.gank.tlc.framework.data.InfoBase;
import io.gank.tlc.framework.view.BaseHolderView;
import io.gank.tlc.share.info.InfoGank;
import io.gank.tlc.util.Constant;

/**
 * Created by lipeng21 on 2015/11/10.
 */
public class HomeHView extends BaseHolderView{
    @Bind(R.id.home_item_img)
    ImageView mImg;

    public HomeHView(Context pContext){
        super(pContext, R.layout.home_litem);
    }

    @Override
    protected void bindData(InfoBase pInfo, final int pPosition) {
        final InfoDaoMeizi tInfo = (InfoDaoMeizi)pInfo;

//        Glide.with(mContext)
//                .load(tInfo.url)
//                .centerCrop()
//                .into(mImg);

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
                });

        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPictureActivity(tInfo, mImg, pPosition);
            }
        });
    }

    private void startPictureActivity(InfoGank pInfo, View pView, int pPosition) {
        Intent tIntent = new Intent(mContext, PhotoActivity.class);
        tIntent.putExtra(Constant.EXTRA_POSITION, pPosition);

        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)mContext, pView,
                        mContext.getString(R.string.transition_share_photo));
        try {
            ActivityCompat.startActivity((Activity)mContext, tIntent, optionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            mContext.startActivity(tIntent);
        }
    }

}
