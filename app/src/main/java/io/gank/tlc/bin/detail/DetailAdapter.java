package io.gank.tlc.bin.detail;

import android.content.Context;

import io.gank.tlc.framework.view.BaseAdapter;

/**
 * Created by lipeng21 on 2015/12/29.
 */
public class DetailAdapter extends BaseAdapter{

    public DetailAdapter(Context pContext) {
        super(pContext);
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position) instanceof InfoDetailTitle){
            return 0;
        }else{
            return 1;
        }
    }

}
