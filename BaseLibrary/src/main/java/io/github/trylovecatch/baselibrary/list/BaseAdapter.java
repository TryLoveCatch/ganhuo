package io.github.trylovecatch.baselibrary.list;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import io.github.trylovecatch.baselibrary.data.InfoBase;

/**
 * Created by lipeng21 on 2015/11/9.
 */
public class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder>{

    protected Context mContext;
    private Class<? extends BaseViewHolder>[] mViewHolders;
    private List<? extends InfoBase> mArrData;

    public BaseAdapter(Context pContext){
        this.mContext = pContext;
    }

    public BaseAdapter setHolderViews(Class<? extends BaseViewHolder>... pHolderViews){
        this.mViewHolders = pHolderViews;
        return this;
    }

    public BaseAdapter setData(List<? extends InfoBase> pArrDats){
        this.mArrData = pArrDats;
        return this;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            return (BaseViewHolder) this.mViewHolders[viewType].getConstructor(Context.class)
                    .newInstance(this.mContext);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bindData(getItem(position), position);
    }

    @Override
    public int getItemCount() {
        return mArrData != null ? mArrData.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    public InfoBase getItem(int pPosition){
        return mArrData!=null && mArrData.size() > 0 ? mArrData.get(pPosition) : null;
    }
}
