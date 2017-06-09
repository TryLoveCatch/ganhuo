package io.github.trylovecatch.baselibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.github.trylovecatch.baselibrary.R;

public class EmptyView extends LinearLayout {
    private String topText;
    private boolean canRefresh=true;
    private View emptyView;
    private CircularProgress pb;
    private TextView topTextView;
    private OnClickListener onClickListener;
    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EmptyView);
        this.topText = ta.getString(R.styleable.EmptyView_topText);
        this.canRefresh = ta.getBoolean(R.styleable.EmptyView_canRefresh, true);
        ta.recycle();
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.empty_view, this);
        emptyView = findViewById(R.id.custom_empty_view_emptyview);

        pb = (CircularProgress) findViewById(R.id.custom_empty_view_loading_pb);
        topTextView = (TextView) findViewById(R.id.custom_empty_view_top_tv);
        
        setText(topText);
        pb.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }
    
    //==========================================

    public boolean isShowing(){
        return this.getVisibility() == View.VISIBLE;
    }

    public void setOnRefreshListener(OnClickListener listener){
        onClickListener = listener;
    }

    public void setText(String topText) {
        topTextView.setText(topText);
    }

    public void setText(int topTextId) {
        topTextView.setText(topTextId);
    }

    public void showError(String pErrorMsg){
        pb.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        setText(pErrorMsg);
        this.setVisibility(View.VISIBLE);
        if(canRefresh) {
            this.setOnClickListener(onClickListener);
        }
    }
    public void showError(int pErrorMsg){
        pb.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        setText(pErrorMsg);
        this.setVisibility(View.VISIBLE);
        if(canRefresh) {
            this.setOnClickListener(onClickListener);
        }
    }

    public void showLoading(){
        pb.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        this.setVisibility(View.VISIBLE);
        this.setOnClickListener(null);
    }

//    public void showNoData() {
//    	pb.setVisibility(View.GONE);
//    	emptyView.setVisibility(View.VISIBLE);
//    	setText(R.string.msg_top_nodata);
//    	this.setVisibility(View.VISIBLE);
//        if(canRefresh) {
//            this.setOnClickListener(onClickListener);
//        }
//    }
//    public void showNoNet() {
//    	pb.setVisibility(View.GONE);
//    	emptyView.setVisibility(View.VISIBLE);
//    	setText(R.string.msg_top_nonet);
//    	this.setVisibility(View.VISIBLE);
//        if(canRefresh) {
//            this.setOnClickListener(onClickListener);
//        }
//    }
//    public void showOnFail() {
//    	pb.setVisibility(View.GONE);
//    	emptyView.setVisibility(View.VISIBLE);
//    	setText(R.string.msg_top_fail);
//    	this.setVisibility(View.VISIBLE);
//        if(canRefresh) {
//            this.setOnClickListener(onClickListener);
//        }
//    }
}
