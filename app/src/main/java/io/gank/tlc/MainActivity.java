package io.gank.tlc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import io.gank.tlc.bin.home.HomeFragment;
import io.gank.tlc.framework.BaseActivity;
import io.gank.tlc.framework.IToolbarAndFab;

public class MainActivity extends BaseActivity {

    //===============界面变量==============
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;

    //===============逻辑变量==============
    //===============生命周期==============

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
    }



    //===============事件接口==============
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    @Override
    public void initViewProperty() {
        setSupportActionBar(mToolbar);
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment tFrg = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if(tFrg instanceof IToolbarAndFab){
                    ((IToolbarAndFab)tFrg).onToolbarClicked(mToolbar);
                }
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Fragment tFrg = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if(tFrg instanceof IToolbarAndFab){
                    ((IToolbarAndFab)tFrg).onFabClicked(mFab);
                }
            }
        });

        setContentFragment(HomeFragment.class);
    }

    @Override
    public void initData() {

    }

    //===============对外方法==============
    //===============私有方法==============
    private void loadData(){

    }

    private void fillView(){

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
