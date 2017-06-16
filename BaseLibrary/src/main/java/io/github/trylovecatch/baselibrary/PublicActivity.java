package io.github.trylovecatch.baselibrary;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

/**
 * 公用FragmentActivity
 * @ClassName SharedFragmentActivity
 * @author TryLoveCatch
 * @date 2014年5月21日 下午10:25:41
 */
public class PublicActivity extends BaseActivity {

    public static final String EXTRA_FRAGMENT_NAME = "fragment_name";
    public static final String EXTRA_IS_HAS_TOOLBAR = "isHasToolbar";

    /**
     * 启动一个fragment
     * 
     * @Title: startFragmentActivity
     * @param fragmentClass
     * @param extras
     * @return void
     * @date Apr 22, 2014 12:27:37 AM
     */
    public static void startFragmentActivity(Context context, Class<? extends BaseFragment> fragmentClass, Bundle extras) {
        Intent intent = new Intent(context, PublicActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentClass);
        if (null != extras)
            intent.putExtras(extras);
        context.startActivity(intent);
    }

    /**
     * 启动一个用于回调信息的fragment
     * @Title: startFragmentActivityForResult
     * @param fragmentClass
     * @param requestCode
     * @param extras
     * @return void
     * @date 2014-4-30 下午1:29:28
     */
    public static void startFragmentActivityForResult(BaseFragment fragment, Class<? extends BaseFragment> fragmentClass, int requestCode, Bundle extras) {
        Intent intent = new Intent(fragment.getActivity(), PublicActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentClass);
        if (null != extras)
            intent.putExtras(extras);
        fragment.getActivity().startActivityForResult(intent, requestCode);
    }


//    public static void startFragmentActivity(Activity context, Class<? extends BaseFragment> fragmentClass, Bundle extras, View view){
//
//        Intent intent = new Intent(context, SharedFragmentActivity.class);
//        intent.putExtra(EXTRA_FRAGMENT_NAME, fragmentClass);
//        if (null != extras)
//            intent.putExtras(extras);
//
//        ActivityOptionsCompat optionsCompat =
//                ActivityOptionsCompat.makeSceneTransitionAnimation(context, view,
//                        context.getString(R.string.transition_share_photo));
//        try {
//            ActivityCompat.startActivity(context, intent, optionsCompat.toBundle());
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            context.startActivity(intent);
//        }
//    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean isHasToolbar = getIntent().getBooleanExtra(EXTRA_IS_HAS_TOOLBAR, true);
        super.onCreate(savedInstanceState, isHasToolbar ? R.layout.content_frame_toolbar : R.layout.content_frame);

        Class<? extends BaseFragment> fragmentClass = (Class<? extends BaseFragment>)getIntent().getSerializableExtra(
                EXTRA_FRAGMENT_NAME);
        if (fragmentClass != null) {
            setContentFragment(fragmentClass, getIntent().getExtras());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	FragmentManager fm = getSupportFragmentManager();
        BaseFragment outer = (BaseFragment)fm.findFragmentById(R.id.content_frame);
        if(outer != null){
        	outer.onActivityResult(requestCode, resultCode, data);
        	return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public void initViewProperty() {
        // TODO Auto-generated method stub
        if(mToolbar!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
