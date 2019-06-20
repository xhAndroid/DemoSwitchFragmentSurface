package com.appman.sfs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

import com.appman.view.SwitchLayoutView;

public class MainActivity extends AppCompatActivity {

    private SwitchLayoutView switchLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchLayoutView = findViewById(R.id.slv);
        SurfaceView surfaceView = new SurfaceView(this);
        switchLayoutView.addSmallView(surfaceView);

        findViewById(R.id.v_change_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLayoutView.switchLayout();
            }
        });
    }


}
