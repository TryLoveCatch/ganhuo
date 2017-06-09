package io.github.trylovecatch.gank.text;

import java.text.SimpleDateFormat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import io.github.trylovecatch.baselibrary.data.InfoBase;
import io.github.trylovecatch.baselibrary.list.BaseViewHolder;
import io.github.trylovecatch.baselibrary.utils.UtilScreen;
import io.github.trylovecatch.gank.MainActivity;
import io.github.trylovecatch.gank.R;
import io.github.trylovecatch.gank.info.InfoGank;
import io.github.trylovecatch.gank.service.ApiServeces;
import io.github.trylovecatch.gank.service.RouterServices;

/**
 * Created by lipeng21 on 2015/11/10.
 */
public class TextViewHolder extends BaseViewHolder{
    @BindView(R.id.text_item_txt_name)
    TextView mTxtName;
    @BindView(R.id.text_item_txt_time)
    TextView mTxtTime;
    @BindView(R.id.text_item_txt_desc)
    TextView mTxtDesc;
    @BindView(R.id.text_item_img)
    ImageView mImg;

    private SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd");
    private String mImgUrl;

    public TextViewHolder(Context pContext){
        super(pContext, R.layout.text_item);

        ViewGroup.LayoutParams tParams = mImg.getLayoutParams();
        tParams.width = UtilScreen.getScreenWidth();
        tParams.height = Math.round(tParams.width * 1.0f / 2);
        mImg.setLayoutParams(tParams);
    }

    @Override
    protected void bindData(InfoBase pInfo, final int pPosition) {
        final InfoGank tInfo = (InfoGank)pInfo;

        mTxtName.setText(tInfo.who);
        mTxtTime.setText(mSdf.format(tInfo.publishedAt));
        mTxtDesc.setText(tInfo.desc);

        if(tInfo.images!=null) {
            mImgUrl = tInfo.images[0];
            mImg.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(mImgUrl)
                    .apply(options)
                    .into(mImg);

            /**
             * 下面这个动态调整管理ImageView的高度
             */
//            Glide.with(mContext)
//                    .asBitmap()
//                    .load(mImgUrl)
//                    .into(target);
        }else if(ApiServeces.TYPE_MEIZI.equals(tInfo.type)){
            //针对All的情况下
            mImgUrl = tInfo.url;
            mImg.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(mImgUrl)
                    .apply(options)
                    .into(mImg);
        }else{
            mImg.setVisibility(View.GONE);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterServices.getInstance().getService(mContext)
                        .startWebActivity(tInfo.url);

            }
        });
    }


    private RequestOptions options = new RequestOptions()
//            .centerCrop()
            .centerInside()
            .placeholder(R.mipmap.ic_launcher);
    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap resource, Transition transition) {

            ViewGroup.LayoutParams tParams = mImg.getLayoutParams();
            tParams.width = UtilScreen.getScreenWidth();
            tParams.height = Math.round(tParams.width * 1.0f * resource.getHeight() / resource.getWidth());
            mImg.setLayoutParams(tParams);

            // 重新调用一遍，这样就可以展示gif了
            // 因为上面为了获取宽高比，强制asBitmap了
            Glide.with(mContext)
                    .load(mImgUrl)
                    .apply(options)
                    .into(mImg);
        }
    };
}
