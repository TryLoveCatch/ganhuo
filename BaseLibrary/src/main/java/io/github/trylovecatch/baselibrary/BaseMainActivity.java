package io.github.trylovecatch.baselibrary;

import android.content.Intent;
import android.view.MotionEvent;

/**
 * Created by lipeng21 on 2017/6/8.
 *
 * 增加了双指滑动，跳转到{@ConsoleActivity}的逻辑
 */

public abstract class BaseMainActivity extends BaseActivity{


    private float pointA = 0;
    private float pointB = 0;
    private final float MOVE_DISTANCE = 50;

    @SuppressWarnings("deprecation")
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
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
}
