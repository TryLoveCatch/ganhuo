package io.gank.tlc;

import android.app.Activity;
import android.os.Bundle;

public class BlackActivity extends Activity{
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.finish();
    }

}
