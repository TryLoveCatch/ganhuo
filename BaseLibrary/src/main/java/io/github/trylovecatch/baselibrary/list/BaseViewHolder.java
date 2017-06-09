package io.github.trylovecatch.baselibrary.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import butterknife.ButterKnife;
import io.github.trylovecatch.baselibrary.data.InfoBase;

/**
 * Created by lipeng21 on 2015/11/9.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder{
    protected Context mContext;

    public BaseViewHolder(Context pContext){
        super(null);
    }
    public BaseViewHolder(Context pContext, int pLayoutId){
        super(LayoutInflater.from(pContext).inflate(pLayoutId, null));
        this.mContext = pContext;
        ButterKnife.bind(this, itemView);
    }

    abstract protected void bindData(InfoBase pInfo, int pPosition);

}
