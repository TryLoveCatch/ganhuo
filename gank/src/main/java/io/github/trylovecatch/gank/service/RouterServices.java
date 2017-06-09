package io.github.trylovecatch.gank.service;

import java.util.Map;

import android.content.Context;
import io.github.trylovecatch.baselibrary.BaseFragment;
import io.github.trylovecatch.baselibrary.PublicActivity;
import io.github.trylovecatch.baselibrary.router.BaseRouterServices;
import io.github.trylovecatch.baselibrary.router.Router;
import io.github.trylovecatch.baselibrary.router.router.IntentParam;
import io.github.trylovecatch.baselibrary.router.router.IntentUri;
import io.github.trylovecatch.gank.text.TextFragment;
import io.github.trylovecatch.gank.video.VideoPlayFragment;

/**
 * Created by lipeng21 on 2017/6/13.
 */

public class RouterServices  {
    private volatile static RouterServices mInstance;

    private RouterServices(){

    }

    public static RouterServices getInstance(){
        if(mInstance==null){
            synchronized (RouterServices.class){
                if(mInstance==null){
                    mInstance = new RouterServices();
                }
            }
        }
        return mInstance;
    }

    private Router mRouter;

    public MyServices getService(Context pContext){
        if(mRouter == null){
            mRouter = new Router(pContext);
        } else{
            mRouter.setContext(pContext);
        }
        return mRouter.create(MyServices.class);
    }


    public interface MyServices extends BaseRouterServices {
        @IntentUri("tlc://io.github.trylovecatch.baselibrary.PublicActivity")
        void startVideoPlay(
                @IntentParam(PublicActivity.INTENT_FRAGMENT_NAME) Class< ? extends BaseFragment> pFragmentClass,
                @IntentParam(VideoPlayFragment.EXTRA_URL) String pUrl);
        @IntentUri("tlc://io.github.trylovecatch.baselibrary.PublicActivity")
        void startText(
                @IntentParam(PublicActivity.INTENT_FRAGMENT_NAME) Class< ? extends BaseFragment> pFragmentClass,
                @IntentParam(TextFragment.EXTRA_TYPE) String pType);
    }

}
