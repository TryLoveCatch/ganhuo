package io.gank.tlc.framework.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import butterknife.ButterKnife;
import io.gank.tlc.framework.data.InfoBase;

/**
 * Created by lipeng21 on 2015/11/9.
 */
public abstract class BaseHolderView extends RecyclerView.ViewHolder{
    protected Context mContext;

    public BaseHolderView(Context pContext){
        super(null);
    }
    public BaseHolderView(Context pContext, int pLayoutId){
        super(LayoutInflater.from(pContext).inflate(pLayoutId, null));
        this.mContext = pContext;
        ButterKnife.bind(this, itemView);
    }

    abstract protected void bindData(InfoBase pInfo, int pPosition);

}
