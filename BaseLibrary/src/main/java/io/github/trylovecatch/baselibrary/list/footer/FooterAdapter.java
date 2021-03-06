package io.github.trylovecatch.baselibrary.list.footer;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == TYPE_FOOTER) {
                        return gridManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if( lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams ) {
            StaggeredGridLayoutManager.LayoutParams  params =(StaggeredGridLayoutManager.LayoutParams) lp;
            if(holder.getItemViewType()==TYPE_FOOTER){
                params.setFullSpan(true);
            }else{
                params.setFullSpan(false);
            }
        }
    }

    public void setIsHasMore(boolean pIsHasMore) {
        this.mIsHasMore = pIsHasMore;
    }

    public void setIsHasFooter(boolean pIsHasFooter) {
        this.mIsHasFooter = pIsHasFooter;
    }
}
