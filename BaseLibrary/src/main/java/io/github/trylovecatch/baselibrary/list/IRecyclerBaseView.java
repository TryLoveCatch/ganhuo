package io.github.trylovecatch.baselibrary.list;

import io.github.trylovecatch.baselibrary.mvp.IView;

/**
 * Created by lipeng21 on 2017/6/13.
 */

public interface IRecyclerBaseView extends IView{
    void fillView(EventRecycle pEvent);
}
