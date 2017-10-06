package com.dobbby.mediatordemo;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.dobbby.mediator.Mediator;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_ALBUM = 3274;

    private View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = findViewById(R.id.activity_main);

        findViewById(R.id.btn_when_no_permission).setOnClickListener(this);
        findViewById(R.id.btn_request_permission).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request_permission:
                requestPermission();
                break;
            case R.id.btn_when_no_permission:
                openAlbum();
                break;
            default:
                break;
        }
    }

    private void requestPermission() {
        Mediator.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onPerform(new Mediator.OnPerform() {
                    @Override
                    public void onPerform() {
                        openAlbum();
                        Snackbar.make(mainView, "Accepted, now performing ...", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                })
                .onRejected(new Mediator.OnRejected() {
                    @Override
                    public void onRejected() {
                        Snackbar.make(mainView, "Rejected", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                })
                .delegate();
    }

    private void openAlbum() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(5)
                .thumbnailScale(0.5f)
                .imageEngine(new GlideEngine())
                .theme(R.style.Matisse_Dracula)
                .forResult(REQUEST_CODE_ALBUM);
    }
}
