package io.gank.tlc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.OnClick;
import io.gank.tlc.framework.BaseActivity;
import io.gank.tlc.util.UtilManager;

public class ConsoleActivity extends BaseActivity {
    @Bind(R.id.btnErrorLog)
    Button showLog;

    // private static boolean togetherVideoSwitch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState, R.layout.console);
        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        setForbidFinishActivityGesture(true);
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
    }

    private int startY = 0;

    /*
     * (non-Javadoc) 手势finish
     * 
     * @see android.app.Activity#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startY = (int) event.getY();
        }

        if (event.getPointerCount() == 2 && event.getActionMasked() == MotionEvent.ACTION_POINTER_1_UP) {
            if (startY - event.getY() > 100) {
                finish();
                return true;
            }
        }

        return super.dispatchTouchEvent(event);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void initViewProperty() {
    }


    @OnClick(R.id.btnErrorLog)
    void showErrorLog() {
        openLogList();
    }

    private boolean logListOpened = false;
    private boolean logDetailOpened = false;
    private ViewGroup root = null;
    private LinearLayout layout = null;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void openLogList() {
        final Context ctx = this;
        File logDir = UtilManager.getInstance().mUtilFile.getDirCrash();
        final File[] logs = logDir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                // TODO Auto-generated method stub
                return pathname.getName().endsWith(".log");
            }

        });

        if (logs == null || logs.length == 0) {
            showToast("暂无日志");
            return;
        }

        Arrays.sort(logs, new LastModifiedFileComparator());
        root = ((ViewGroup) getRootView(this));

        final ScrollView scrollViewLayout = new ScrollView(ctx);
        LinearLayout.LayoutParams paramsScrollView =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
        paramsScrollView.height = LinearLayout.LayoutParams.MATCH_PARENT;
        paramsScrollView.width = LinearLayout.LayoutParams.MATCH_PARENT;
        scrollViewLayout.setLayoutParams(paramsScrollView);
        scrollViewLayout.setBackgroundColor(Color.BLUE);

        layout = new LinearLayout(ctx);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.VERTICAL);

        for (final File log : logs) {
            TextView t = new TextView(ctx);
            t.setText(log.getName());
            t.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            t.setTextSize(20);
            t.setTextColor(Color.WHITE);
            layout.addView(t);

            t.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (logDetailOpened) {
                        layout.removeViewAt(0);
                        logDetailOpened = false;
                    }
                    HorizontalScrollView scrollView = new HorizontalScrollView(ctx);
                    LinearLayout.LayoutParams params =
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                    scrollView.setLayoutParams(params);
                    scrollView.setBackgroundColor(Color.BLUE);
                    TextView t = new TextView(ctx);
                    t.setHorizontallyScrolling(true);
                    t.setTextColor(Color.WHITE);
                    t.setSingleLine(false);
                    try {
                        t.setText(readStream(new java.io.FileInputStream(log)));
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    scrollView.addView(t);
                    layout.addView(scrollView, 0);
                    scrollViewLayout.scrollTo(0, 0);
                    logDetailOpened = true;
                }

            });
            t.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View arg0) {
                    // TODO Auto-generated method stub
                    ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    try {
                        cmb.setText(readStream(new java.io.FileInputStream(log)));
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    showToast("Log已复制到剪切板");

                    return true;
                }
            });
        }

        scrollViewLayout.addView(layout);
        root.addView(scrollViewLayout, 0);
        logListOpened = true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (logDetailOpened) {
            layout.removeViewAt(0);
            logDetailOpened = false;
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        if (logListOpened) {
            root.removeViewAt(0);
            logListOpened = false;
            return;
        }
        super.onBackPressed();
    }

    /**
     * 文件按时间排序
     * 
     * @Package com.youku.paike.utils
     * @ClassName: LastModifiedFileComparator
     * @author Beethoven
     * @mail zhanghuitao@youku.com
     * @date 2013-3-13 下午5:13:32
     */
    private class LastModifiedFileComparator implements Comparator<File> {
        @Override
        public int compare(File file1, File file2) {
            long result = file2.lastModified() - file1.lastModified();
            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private String readStream(InputStream in) {
        String result = null;

        if (in != null) {
            StringWriter writer = new StringWriter();
            try {
                result = inputStream2String(in);
            } catch (IOException e) {
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public void initData() {
    }
    
    private View getRootView(Activity activity){
        return ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
    }

    private String inputStream2String(InputStream in) throws IOException {
        return new String(inputStream2Bytes(in));
    }

    private byte[] inputStream2Bytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1;) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }
}
