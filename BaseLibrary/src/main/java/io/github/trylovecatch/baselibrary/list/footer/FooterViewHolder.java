package io.github.trylovecatch.baselibrary.list.footer;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import io.github.trylovecatch.baselibrary.R;
import io.github.trylovecatch.baselibrary.R2;
import io.github.trylovecatch.baselibrary.data.InfoBase;
import io.github.trylovecatch.baselibrary.list.BaseViewHolder;

/**
 * Created by lipeng21 on 2017/6/14.
 */

public class FooterViewHolder extends BaseViewHolder {

    @BindView(R2.id.footer_txt)
    TextView mTxt;
    @BindView(R2.id.footer_prg)
    ProgressBar mPrg;

    public FooterViewHolder(Context pContext) {
        super(pContext, R.layout.recycler_footer);
    }

    @Override
    protected void bindData(InfoBase pInfo, int pPosition) {

    }

    protected void bindData(boolean pIsHasMore){
        mTxt.setVisibility(View.VISIBLE);
        if(pIsHasMore){
            mPrg.setVisibility(View.VISIBLE);
            mTxt.setText(R.string.progress_loading);
        }else{
            mPrg.setVisibility(View.GONE);
            mTxt.setText(R.string.no_more_data);
        }
    }
}
