package io.github.trylovecatch.baselibrary.list.footer;

import android.content.Context;
import android.view.ViewGroup;
import io.github.trylovecatch.baselibrary.list.BaseAdapter;
import io.github.trylovecatch.baselibrary.list.BaseViewHolder;

/**
 * Created by lipeng21 on 2017/6/14.
 */

public class FooterAdapter extends BaseAdapter {
    private final static int TYPE_FOOTER = Integer.MAX_VALUE;
    private boolean mIsHasMore;
    private boolean mIsHasFooter;

    public FooterAdapter(Context pContext) {
        super(pContext);
        mIsHasMore = true;
        mIsHasFooter = true;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_FOOTER){
            return new FooterViewHolder(mContext);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(holder instanceof FooterViewHolder){
            ((FooterViewHolder)holder).bindData(mIsHasMore);
        }else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mIsHasFooter && position != 0 && position == (getItemCount() - 1)){
            return TYPE_FOOTER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        int tCount = super.getItemCount();
        if(mIsHasFooter && tCount != 0){
            return tCount + 1;
        }
        return tCount;
    }

    public void setIsHasMore(boolean pIsHasMore) {
        this.mIsHasMore = pIsHasMore;
    }

    public void setIsHasFooter(boolean pIsHasFooter) {
        this.mIsHasFooter = pIsHasFooter;
    }
}
