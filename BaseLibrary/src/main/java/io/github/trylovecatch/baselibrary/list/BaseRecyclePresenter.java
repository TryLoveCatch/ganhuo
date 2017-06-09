package io.github.trylovecatch.baselibrary.list;

import io.github.trylovecatch.baselibrary.mvp.BasePresenter;

/**
 * Created by lipeng21 on 2017/6/13.
 */

public abstract class BaseRecyclePresenter<T extends IRecyclerBaseView> extends BasePresenter<T>{

    public abstract void loadData(boolean pIsForceRefresh, int pPage, int pPageSize);
}
