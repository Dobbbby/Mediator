/*
 * Copyright 2017 Dobbby
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dobbby.mediator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Dobbby on 2017/10/6.
 * <p>
 * A simple fake Activity that delegates Mediator's works.
 */
public final class MediatorActivity extends Activity {
    private static final String TAG = "MediatorActivity";

    private static final String ARG_PERMISSION = "permission";
    private static final int REQUEST_CODE_PERMISSION = 17162;

    public static void launch(Context context, String permission) {
        Intent intent = new Intent(context, MediatorActivity.class);

        intent.putExtra(ARG_PERMISSION, permission);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission(getIntent().getStringExtra(ARG_PERMISSION));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                doOnPermissionResult(grantResults);
                break;
            default:
                break;
        }

        finish();
    }

    private void checkPermission(String permission) {
        if (permission == null || permission.isEmpty()) {
            finish();
            return;
        }

        int permissionResult = ContextCompat.checkSelfPermission(this, permission);

        if (permissionResult == PackageManager.PERMISSION_GRANTED) {
            Mediator.doOnPerform();
            finish();
            return;
        }

        ActivityCompat.requestPermissions(
                this,
                new String[]{permission},
                REQUEST_CODE_PERMISSION
        );
    }

    private void doOnPermissionResult(int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Mediator.doOnPerform();
        } else {
            Mediator.doOnRejected();
        }
    }
}
