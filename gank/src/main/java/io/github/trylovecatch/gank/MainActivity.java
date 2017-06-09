package io.github.trylovecatch.gank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;
import io.github.trylovecatch.baselibrary.BaseActivity;
import io.github.trylovecatch.baselibrary.PublicActivity;
import io.github.trylovecatch.baselibrary.log.Logger;
import io.github.trylovecatch.baselibrary.router.Router;
import io.github.trylovecatch.gank.meizi.MeiziFragment;
import io.github.trylovecatch.gank.service.ApiServeces;
import io.github.trylovecatch.gank.service.RouterServices;
import io.github.trylovecatch.gank.text.TextFragment;
import io.github.trylovecatch.gank.video.VideoFragment;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_root)
    View mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Logger.i(this.toString());
//        Logger.json("{'name': 'xxxx', 'age': 12, 'classes': [1, 2, 3], 'teacher': {'name': 'yyy', age: '44'}}");
    }

    @Override
    public void initViewProperty() {
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.main_btn_meizi)
    public void onClickMeizi(){
        RouterServices.getInstance().getService(MainActivity.this)
                .startPublicActivity(MeiziFragment.class);
    }

    @OnClick(R.id.main_btn_video)
    public void onClickVideo(){
        RouterServices.getInstance().getService(MainActivity.this)
                .startPublicActivity(VideoFragment.class);
    }

    @OnClick(R.id.main_btn_android)
    public void onClickAndroid(){
        RouterServices.getInstance().getService(MainActivity.this)
                .startText(TextFragment.class, ApiServeces.TYPE_ANDROID);
    }

    @OnClick(R.id.main_btn_ios)
    public void onClickIos(){
        RouterServices.getInstance().getService(MainActivity.this)
                .startText(TextFragment.class, ApiServeces.TYPE_IOS);
    }

    @OnClick(R.id.main_btn_expand)
    public void onClickExpand(){
        RouterServices.getInstance().getService(MainActivity.this)
                .startText(TextFragment.class, ApiServeces.TYPE_EXPAND);
    }

    @OnClick(R.id.main_btn_front)
    public void onClickFront(){
        RouterServices.getInstance().getService(MainActivity.this)
                .startText(TextFragment.class, ApiServeces.TYPE_FRONT);
    }

    @OnClick(R.id.main_btn_recommend)
    public void onClickRecommend(){
        RouterServices.getInstance().getService(MainActivity.this)
                .startText(TextFragment.class, ApiServeces.TYPE_RECOMMEND);
    }

    @OnClick(R.id.main_btn_app)
    public void onClickApp(){
        RouterServices.getInstance().getService(MainActivity.this)
                .startText(TextFragment.class, ApiServeces.TYPE_APP);
    }

    @OnClick(R.id.main_btn_all)
    public void onClickAll(){
        RouterServices.getInstance().getService(MainActivity.this)
                .startText(TextFragment.class, ApiServeces.TYPE_ALL);
    }


    @Override
    public void onBackPressed() {
        showExitDialog();
    }


    private void showExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_exit);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                getApplication().onTerminate();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setMessage(R.string.msg_exit);
        AlertDialog tDialog = builder.create();
        tDialog.show();
    }
}
