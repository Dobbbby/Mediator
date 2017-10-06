package com.dobbby.mediatordemo;

import android.app.Application;
import android.widget.Toast;
import com.dobbby.mediator.Mediator;

public class MediatorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Mediator.init(this);
        Mediator.setDefaultOnRejected(new Mediator.OnRejected() {
            @Override
            public void onRejected() {
                Toast.makeText(MediatorApplication.this, "Rejected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
