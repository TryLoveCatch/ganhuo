package io.github.trylovecatch.gank.meizi;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.BindView;
import io.github.trylovecatch.baselibrary.data.InfoBase;
import io.github.trylovecatch.baselibrary.list.BaseViewHolder;
import io.github.trylovecatch.gank.R;
import io.github.trylovecatch.gank.info.InfoGank;

/**
 * Created by lipeng21 on 2015/11/10.
 */
public class MeiziViewHolder extends BaseViewHolder{
    @BindView(R.id.meizi_item_rlt)
    RelativeLayout mRlt;
    @BindView(R.id.meizi_item_img)
    ImageView mImg;

    public MeiziViewHolder(Context pContext){
        super(pContext, R.layout.meizi_item);
    }

    @Override
    protected void bindData(InfoBase pInfo, final int pPosition) {
        final InfoGank tInfo = (InfoGank)pInfo;

        mImg.setImageResource(R.mipmap.ic_launcher);

        Glide.with(mContext)
                .asBitmap()
                .apply(options)
                .load(tInfo.url)
                .into(target);

        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EventBus.post(new EventMeiziClicked(pPosition, mImg));
            }
        });
    }

    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher);

    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap resource, Transition transition) {
            ViewGroup.LayoutParams tParams = mImg.getLayoutParams();
            tParams.height = Math.round(tParams.width * 1.0f * resource.getHeight() / resource.getWidth());
            mImg.setImageBitmap(resource);
        }
    };

}
