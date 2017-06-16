package io.github.trylovecatch.gank.meizi;

import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import io.github.trylovecatch.baselibrary.list.IRecyclerBaseView;
import io.github.trylovecatch.baselibrary.list.RecyclerFragment;
import io.github.trylovecatch.gank.R;

public class MeiziFragment extends RecyclerFragment<IRecyclerBaseView, MeiziPresenter> {
    
    //===============界面变量==============
    //===============逻辑变量==============
    //===============生命周期==============
    //===============事件接口==============

    @Override
    protected MeiziPresenter initPresenter() {
        return new MeiziPresenter();
    }

    @Override
    protected void setHolderView() {
        mAdp.setHolderViews(MeiziViewHolder.class);
    }

    @Override
    protected int getItemSpace() {
        return getResources().getDimensionPixelSize(R.dimen.dp_3);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        StaggeredGridLayoutManager tLayoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
        tLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        return tLayoutManager;
    }

    @Override
    public void initViewProperty() {
        super.initViewProperty();
        setTitle("福利");
    }

    //===============对外方法==============
    //===============私有方法==============
}
