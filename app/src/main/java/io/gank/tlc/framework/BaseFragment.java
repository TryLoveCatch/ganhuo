package io.gank.tlc.framework;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.gank.tlc.R;
import io.gank.tlc.share.view.EmptyView;
import io.gank.tlc.util.T;
import rx.Subscription;

/**
 * Fragment基类<br>
 * @Package com.jiaoyang.video.framework
 * @ClassName BaseFragment
 * @author TryLoveCatch
 * @date 2014年5月21日 下午10:24:10
 */
public abstract class BaseFragment extends Fragment implements IUI {

    private ArrayList<Subscription> mSubscriptions = new ArrayList<>();

    private View mRoot;

    @Nullable
    @Bind(R.id.emptyView)
    protected EmptyView mEmptyView;

    /**
     * * 做了4件事:<br>
     * 1、生成rootView<br>
     * 2、初始化Views<br>
     * 3、调用initViewProperty<br>
     * 4、调用initData
     * 
     * @Title: onCreateView
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @param layoutResId
     * @return View
     * @date Apr 18, 2014 11:24:57 AM
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, int layoutResId) {
        super.onCreateView(inflater, container, savedInstanceState);

        mRoot = inflater.inflate(layoutResId, container, false);
//        rootView.setBackgroundResource(R.color.bg);
//        if(UtilDust.isNight()){
//        	mRoot.setBackgroundResource(R.drawable.bg_night);
//        }else{
//        	mRoot.setBackgroundResource(R.drawable.bg_day);
//        }
        mRoot.setBackgroundResource(R.color.white);

        ButterKnife.bind(this, mRoot);

        initData();
        initViewProperty();

        return mRoot;
    }
    
    public void removeBackground(){
        mRoot.setBackgroundResource(R.color.transparent);
    }
    
    public void setBackgroud(int pId){
    	mRoot.setBackgroundResource(pId);
    }

    @Override
    public void onDestroyView() {
        for(Subscription tSub : mSubscriptions){
            tSub.unsubscribe();
        }
        super.onDestroyView();
    }

    public void replaceFragment(Class<?> fregmentClass, Bundle arguments) {
        Log.d("replace fragment. class={}", fregmentClass.getName());

        Fragment fragment = Fragment.instantiate(getActivity(), fregmentClass.getName(), arguments);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    protected void openFragment(Fragment fromFragment, Class<?> fregmentClass, Bundle arguments) {
        Log.d("open fragment. class={}", fregmentClass.getName());

        Fragment fragment = Fragment.instantiate(getActivity(), fregmentClass.getName(), arguments);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
        transaction.hide(fromFragment);
        transaction.add(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    protected void replaceFragment(Fragment fromFragment, Class<?> fregmentClass, int contentId, Bundle arguments) {
        Log.d("open fragment. class={}", fregmentClass.getName());

        Fragment fragment = Fragment.instantiate(getActivity(), fregmentClass.getName(), arguments);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
        transaction.replace(contentId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    protected void openFragment(Fragment fromFragment, Class<?> fregmentClass, int contentId, Bundle arguments) {
        Log.d("open fragment. class={}", fregmentClass.getName());

        Fragment fragment = Fragment.instantiate(getActivity(), fregmentClass.getName(), arguments);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
        transaction.hide(fromFragment);
        transaction.add(contentId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    protected void addSubscription(Subscription pSub) {
        mSubscriptions.add(pSub);
    }

    protected void setTitle(int resId) {
        getActivity().setTitle(resId);
    }

    protected void setTitle(CharSequence title) {
        getActivity().setTitle(title);
    }

    public void showToast(String text) {
        T.showShort(getActivity(), text);
    }

    public void showToast(int resId) {
        T.showShort(getActivity(), resId);
    }

    /**
     * 接收返回键按下事件
     * 
     * @Title: onBackKeyDown
     * @return boolean false:back键事件未处理，向下传递。 true：消费掉该事件。
     * @date 2014-3-10 上午11:15:33
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 设置禁止finish activity手势，用于存在viewpager等手势冲突的activity
     * 
     * @Title: setForbidFinishActivityGesture
     * @param paramBoolean
     * @return void
     * @date 2014-5-20 下午4:44:14
     */
    protected void setForbidFinishActivityGesture(boolean paramBoolean) {
        if (!(getActivity() instanceof BaseActivity))
            return;
        ((BaseActivity) getActivity()).setForbidFinishActivityGesture(paramBoolean);
    }

    /**
     * 设置禁止启动Activity动画
     * 
     * @Title: setForbidStartActivityAnimation
     * @param paramBoolean
     * @return void
     * @date 2014-5-20 下午4:44:26
     */
    public void setForbidStartActivityAnimation(boolean paramBoolean) {
        if (!(getActivity() instanceof BaseActivity))
            return;
        ((BaseActivity) getActivity()).setForbidStartActivityAnimation(paramBoolean);
    }
    
    
    
    //自己增加
    protected final static int TYPE_NO_NET = 1;
    protected final static int TYPE_NO_DATA = 2;
    protected final static int TYPE_ERROR = 3;
    protected final static int TYPE_LOADING = 4;

    protected void hideEmptyView(View pView){
        mEmptyView.setVisibility(View.GONE);
        pView.setVisibility(View.VISIBLE);
    }

    protected void showEmptyView(View pView, int pType){
        if(mEmptyView==null) return;
        switch(pType){
            case TYPE_NO_NET:
                mEmptyView.showNoNet();
                break;
            case TYPE_NO_DATA:
                mEmptyView.showNoData();
                break;
            case TYPE_ERROR:
                mEmptyView.showOnFail();
                break;
            case TYPE_LOADING:
                mEmptyView.showLoading();
                break;
        }
        pView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }


    
    protected boolean isListValid(List<?> plist){
    	return plist!=null && plist.size() > 0;
    }
    private ProgressDialog mProgress;
    protected void showProgress(String tMsg) {
		if(mProgress==null){
//			mProgress = DialogUtil.createProgressDialog(getActivity(), null,
//					tMsg, null, false);
		}
		mProgress.setMessage(tMsg);
		mProgress.show();
	}
    protected void hideProgress(){
    	if(mProgress!=null && mProgress.isShowing()){
    		mProgress.dismiss();
    	}
    }
}
