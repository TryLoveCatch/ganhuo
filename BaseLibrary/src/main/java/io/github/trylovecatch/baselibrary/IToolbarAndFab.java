package io.github.trylovecatch.baselibrary;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;

/**
 * fragment实现这个接口
 * 以便于响应Activity里面的事件
 *
 * Created by lipeng21 on 2015/11/12.
 */
public interface IToolbarAndFab {
    void onToolbarClicked(Toolbar pToolbar);
    void onFabClicked(FloatingActionButton pFab);
}
