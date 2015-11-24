package io.gank.tlc.bin.photo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.gank.tlc.dao.InfoDaoMeizi;
import io.gank.tlc.share.view.pinchImageView.PinchImageView;
import io.gank.tlc.share.view.pinchImageView.PinchImageViewPager;

/**
 * Created by lipeng21 on 2015/11/16.
 */
public class PhotoPagerAdapter extends PagerAdapter{

    private Context mContext;
    private LinkedList<PinchImageView> mViewCache = new LinkedList<PinchImageView>();
    private PinchImageViewPager mViewPager;

    private List<InfoDaoMeizi> mArrData = new ArrayList<>();

    public PhotoPagerAdapter(Context pContext, PinchImageViewPager pViewPager){
        super();
        mContext = pContext;
        mViewPager = pViewPager;
    }

    public void setData(List<InfoDaoMeizi> pData){
        mArrData = pData;
    }

    @Override
    public int getCount() {
        return mArrData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final PinchImageView piv;
        if (mViewCache.size() > 0) {
            piv = mViewCache.remove();
            piv.reset();
        } else {
            piv = new PinchImageView(mContext);
            piv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            piv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        Glide.with(mContext)
                .load(mArrData.get(position).url)
                .into(piv);


        container.addView(piv);

        return piv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        PinchImageView piv = (PinchImageView) object;
        container.removeView(piv);
        mViewCache.add(piv);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mViewPager.setMainPinchImageView((PinchImageView) object);
    }
}
