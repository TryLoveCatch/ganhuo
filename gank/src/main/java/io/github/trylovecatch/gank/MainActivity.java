package io.github.trylovecatch.gank;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;
import io.github.trylovecatch.baselibrary.BaseActivity;
import io.github.trylovecatch.baselibrary.ConsoleActivity;
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getApplication().onTerminate();
    }

    @Override
    public void initViewProperty() {
        setTitle("主界面");
        setForbidFinishActivityGesture(true);
        setForbidStartActivityAnimation(true);
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


    private float pointA = 0;
    private float pointB = 0;
    private final float MOVE_DISTANCE = 50;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        if (BuildConfig.DEBUG) {
            int pointerCount = event.getPointerCount();
            int action = event.getAction();
            int actionMasked = event.getActionMasked();

            if (pointerCount == 2 && action == MotionEvent.ACTION_POINTER_2_DOWN) {
                pointA = event.getY(0);
                pointB = event.getY(1);
            }

            if (pointerCount == 2 && actionMasked == MotionEvent.ACTION_POINTER_1_UP) {
                float changeA = pointA - event.getY(0);
                float changeB = pointB - event.getY(1);

                if (changeA <= -MOVE_DISTANCE && changeB <= -MOVE_DISTANCE) {
                    startActivity(new Intent(this, ConsoleActivity.class));
                    RouterServices.getInstance().getService(this).startConsoleActivity();
                    return true;
                }

                pointA = 0;
                pointB = 0;
            }

            try{
                return super.dispatchTouchEvent(event);
            }catch(Exception e){
                e.printStackTrace();
                return true;
            }
        }
        return super.dispatchTouchEvent(event);
    }


    private void showExitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_exit);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                /**
                 * 先结束这个Activity
                 * 然后再onDestroy里面杀死进程，退出Application
                 *
                 * 这样解决闪屏的问题
                 */
                finish();
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
