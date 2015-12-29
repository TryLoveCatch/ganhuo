package io.gank.tlc.share.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import butterknife.Bind;
import io.gank.tlc.R;
import io.gank.tlc.framework.BaseFragment;
import io.gank.tlc.util.Constant;
import io.gank.tlc.util.UtilManager;
import io.gank.tlc.util.UtilString;

public class WebBaseFragment extends BaseFragment{
    
    //===============界面变量==============
    @Bind(R.id.web_progress_view)
    View mViewProgress;
    RelativeLayout.LayoutParams mParamsProgress;
    @Bind(R.id.web_view)
    WebView mWebView;
    //===============逻辑变量==============
    private String mUrl;
    private int mWidth;
    //===============生命周期==============
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.web);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        loadData();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }
    
    @Override
    public void onStop() {
        super.onStop();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    //===============事件接口==============
    @Override
    public void initViewProperty() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setSaveFormData(false);
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webSettings.setLoadWithOverviewMode(false);
//        webSettings.setUseWideViewPort(true);

        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());

        mParamsProgress = (RelativeLayout.LayoutParams)mViewProgress.getLayoutParams();

        loadData();
    }

    @Override
    public void initData() {
        Bundle tData = getArguments();
        if(tData!=null){
            mUrl = tData.getString(Constant.EXTRA_URL);
        }

        if(UtilString.isBlank(mUrl)){
            getActivity().finish();
        }

        mWidth = UtilManager.getInstance().mUtilPhone.getScreenWidth();
    }

    @Override
    public boolean onBackPressed() {
//        if(mWebView.getUrl().equals(mUrl)){
//
//        }else {
//            mWebView.goBack();
//        }
        if(mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }
        return super.onBackPressed();
    }

    //===============对外方法==============
    //===============私有方法==============
    private void loadData(){
        mWebView.loadUrl(mUrl);
    }
    
    private void fillView(){
        
    }

    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mViewProgress.setVisibility(View.INVISIBLE);
        }

    }

    private class MyWebChromeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mViewProgress.setVisibility(View.VISIBLE);
            mParamsProgress.width = mWidth / 100 * newProgress;
            mViewProgress.setLayoutParams(mParamsProgress);
        }
    }
}
