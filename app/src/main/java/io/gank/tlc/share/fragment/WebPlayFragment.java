package io.gank.tlc.share.fragment;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import butterknife.Bind;
import io.gank.tlc.R;
import io.gank.tlc.framework.BaseFragment;
import io.gank.tlc.util.Constant;
import io.gank.tlc.util.L;
import io.gank.tlc.util.T;
import io.gank.tlc.util.UtilManager;
import io.gank.tlc.util.UtilString;

public class WebPlayFragment extends BaseFragment{
    
    //===============界面变量==============
    @Bind(R.id.web_progress_view)
    View mViewProgress;
    RelativeLayout.LayoutParams mParamsProgress;
    @Bind(R.id.web_view)
    WebView mWebView;
    //===============逻辑变量==============
    private Handler mHandler = new Handler();
    private String mUrl;
    private String mRealPlayUrl;
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
        webSettings.setAllowFileAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setLoadWithOverviewMode(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 7_1 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Version/7.0 Mobile/11D5145e Safari/9537.53");

        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mWebView.addJavascriptInterface(new PlayeJavaScriptInterface(), "lp_play");
//        mWebView.addJavascriptInterface(new GetRealPlayUrlJavaScriptInterface(), "lp_getRealPlayUrl");


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
        public void onPageFinished(final WebView view, String url) {
            super.onPageFinished(view, url);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String js = "javascript: var v=document.getElementsByTagName('video')[0]; "
                            + "v.addEventListener('playing', function() { window.lp_play.onStartPlay(); }, true); ";
                    view.loadUrl(js);
//                    view.loadUrl("javascript:window.lp_getRealPlayUrl.showSource(document.getElementsByTagName('video')[0].src);");
                    view.loadUrl("javascript:(function() { var v = document.getElementsByTagName('video')[0]; v.play();})()");
                }
            }, 200);

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
        public void onShowCustomView(View view, CustomViewCallback callback) {

            if (myCallback != null) {
                myCallback.onCustomViewHidden();
                myCallback = null ;
                Log.e("Media", "myCallback.onCustomViewHidden()...");
                return;
            }

            ViewGroup parent = (ViewGroup) mWebView.getParent();
            parent.setBackgroundColor(Color.BLACK);
            parent.removeView(mWebView);
            parent.addView(view);
            myView = view;
            myCallback = callback;
        }


        private View myView = null;
        private CustomViewCallback myCallback = null;


        public void onHideCustomView() {
            if (myView != null) {

                if (myCallback != null) {
                    myCallback.onCustomViewHidden();
                    myCallback = null ;
                }

                ViewGroup parent = (ViewGroup) myView.getParent();
                parent.removeView( myView);
                parent.addView(mWebView);
                myView = null;
            }
        }
    }


    private final class PlayeJavaScriptInterface{

        @JavascriptInterface
        public void onStartPlay(){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    T.showLong(getContext(), "视频开始播放...");
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mWebView.loadUrl("javascript: (function() { var v=document.getElementsByTagName('video')[0]; v.webkitEnterFullscreen();})()");
                }
            });

        }
        @JavascriptInterface
        public void onStopPlay(){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    T.showLong(getContext(), "视频结束...");
                }
            });
        }
    }

    private final class GetRealPlayUrlJavaScriptInterface {
        @JavascriptInterface
        public void showSource(String pUrl) {
            L.e(pUrl);
            mRealPlayUrl = pUrl;
        }
    }
}
