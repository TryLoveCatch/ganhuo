package io.github.trylovecatch.gank.text;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import io.github.trylovecatch.baselibrary.list.IRecyclerBaseView;
import io.github.trylovecatch.baselibrary.list.RecyclerFragment;
import io.github.trylovecatch.baselibrary.retrofit.ApiRetrofit;
import io.github.trylovecatch.gank.R;
import io.github.trylovecatch.gank.service.ApiServeces;
import io.github.trylovecatch.gank.video.VideoViewHolder;

public class TextFragment extends RecyclerFragment<IRecyclerBaseView, TextPresenter> {
    public static final String EXTRA_TYPE = "type";

    //===============界面变量==============
    //===============逻辑变量==============
    private String mTxtType;
    //===============生命周期==============

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            mTxtType = getArguments().getString(EXTRA_TYPE);
        }

        if(TextUtils.isEmpty(mTxtType)){
            mTxtType = ApiServeces.TYPE_ANDROID;
        }

        mPresenter.setType(mTxtType);
    }

    //===============事件接口==============

    @Override
    protected TextPresenter initPresenter() {
        return new TextPresenter();
    }

    @Override
    protected void setHolderView() {
        mAdp.setHolderViews(TextViewHolder.class);
    }

    @Override
    protected int getItemSpace() {
        return getResources().getDimensionPixelSize(R.dimen.dp_3);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        LinearLayoutManager tLayoutManager = new LinearLayoutManager(getActivity());
        return tLayoutManager;
    }

    @Override
    public void initViewProperty() {
        super.initViewProperty();
        setTitle(mTxtType);
    }
    //===============对外方法==============
    //===============私有方法==============
}
