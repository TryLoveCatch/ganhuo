package io.gank.tlc.bin.photo;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import io.gank.tlc.R;
import io.gank.tlc.framework.BaseActivity;
import io.gank.tlc.framework.BaseFragment;
import io.gank.tlc.share.view.pinchImageView.PinchImageView;
import io.gank.tlc.util.Constant;
import io.gank.tlc.util.UtilString;

public class PhotoFragment extends BaseFragment{
    
    //===============界面变量==============
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.photo_img)
    PinchImageView mImg;
    //===============逻辑变量==============
    private String mUrl;
    //===============生命周期==============
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.photo);
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
    
    @Override
    public void onPause() {
        super.onPause();
    }
    
    @Override
    public void onStop() {
        super.onStop();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    //===============事件接口==============
    @Override
    public void initViewProperty() {
        ((BaseActivity)getActivity()).setSupportActionBar(mToolbar);

        Glide.with(this)
                .load(mUrl)
                .into(mImg);
    }

    @Override
    public void initData() {
        Bundle tData = getArguments();
        if(tData!=null){
            mUrl = tData.getString(Constant.EXTRA_URL);
        }

        if(UtilString.isBlank(mUrl)){
            getActivity().finish();
        }
    }
    //===============对外方法==============
    //===============私有方法==============
}
