package io.github.trylovecatch.gank.video;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import io.github.trylovecatch.baselibrary.list.IRecyclerBaseView;
import io.github.trylovecatch.baselibrary.list.RecyclerFragment;
import io.github.trylovecatch.gank.R;
import io.github.trylovecatch.gank.meizi.MeiziViewHolder;

public class VideoFragment extends RecyclerFragment<IRecyclerBaseView, VideoPresenter> {
    
    //===============界面变量==============
    //===============逻辑变量==============
    //===============生命周期==============
    //===============事件接口==============

    @Override
    protected VideoPresenter initPresenter() {
        return new VideoPresenter();
    }

    @Override
    protected void setHolderView() {
        mAdp.setHolderViews(VideoViewHolder.class);
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

    //===============对外方法==============
    //===============私有方法==============
}
