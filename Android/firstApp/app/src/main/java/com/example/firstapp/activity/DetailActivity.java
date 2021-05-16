package com.example.firstapp.activity;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.firstapp.R;

public class DetailActivity extends AppCompatActivity {
    TextureView mTextureView = null;
    RelativeLayout rootRelativeLayout = null;
    SurfaceTexture mSurfaceTexture = null;
    Handler mToolbarHandler;
    Runnable mToolbarRunnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        rootRelativeLayout = findViewById(R.id.detailRootRelativeLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView testTextView = findViewById(R.id.testTextView);
        testTextView.setText(this.getIntent().getStringExtra("contentLink"));

        // overlay,,
        rootRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportActionBar().isShowing()) {
                    getSupportActionBar().hide();
                } else {
                    getSupportActionBar().show();
                }
            }
        });

        mToolbarHandler = new Handler();
        mToolbarRunnable = new Runnable() {
            public void run() {
                rootRelativeLayout.setVisibility(View.INVISIBLE);
            }
        };
    }

    private View.OnTouchListener mVolPlusOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d("test", "TOUCH  " + "Display");
            rootRelativeLayout.setVisibility(View.VISIBLE);
            mToolbarHandler.removeCallbacks(mToolbarRunnable);
            mToolbarHandler.postDelayed(mToolbarRunnable, 10000);
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean show = super.onPrepareOptionsMenu(menu);
        if (!show)
            return show;

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
