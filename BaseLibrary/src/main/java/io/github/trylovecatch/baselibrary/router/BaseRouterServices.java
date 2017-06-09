package io.github.trylovecatch.baselibrary.router;

import io.github.trylovecatch.baselibrary.BaseFragment;
import io.github.trylovecatch.baselibrary.PublicActivity;
import io.github.trylovecatch.baselibrary.router.router.IntentParam;
import io.github.trylovecatch.baselibrary.router.router.IntentUri;
import io.github.trylovecatch.baselibrary.web.WebBaseActivity;

/**
 * Created by lipeng21 on 2017/6/14.
 */

public interface BaseRouterServices {

    @IntentUri("tlc://io.github.trylovecatch.baselibrary.PublicActivity")
    void startPublicActivity(@IntentParam(PublicActivity.INTENT_FRAGMENT_NAME) Class< ? extends BaseFragment> pFragmentClass);
    @IntentUri("tlc://io.github.trylovecatch.baselibrary.web.WebBaseActivity")
    void startWebActivity(@IntentParam(WebBaseActivity.EXTRA_URL) String pUrl);
}
