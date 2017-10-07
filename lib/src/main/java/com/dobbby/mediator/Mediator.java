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

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Created by Dobbby on 2017/10/6.
 * <p>
 * A mediator that helps with Android permissions at run time.
 */
public final class Mediator {
    private static Context application;

    private static WeakReference<OnPerform> onPerform;
    private static WeakReference<OnRejected> onRejected;

    private static OnRejected defaultOnRejected;

    /**
     * Initialize Mediator when the application launched.
     *
     * @param application Application context.
     */
    public static void init(Context application) {
        Mediator.application = application.getApplicationContext();
    }

    /**
     * Set default OnRejected callback. If onRejected method is not set
     * in later requests, this default one will be called.
     *
     * @param defaultOnRejected Do when rejected.
     */
    public static void setDefaultOnRejected(OnRejected defaultOnRejected) {
        Mediator.defaultOnRejected = defaultOnRejected;
    }

    /**
     * Request permission.
     *
     * @param permission Requested permission.
     * @return A Helper instance to set {@link OnPerform} and {@link OnRejected}.
     */
    public static Helper request(@NonNull String permission) {
        return new Helper(permission);
    }

    private static void delegate(@NonNull String permission, @NonNull OnPerform onPerform, OnRejected onRejected) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onPerform.onPerform();
            return;
        }

        Mediator.onPerform = new WeakReference<>(onPerform);
        Mediator.onRejected = new WeakReference<>(onRejected);

        if (application == null) {
            throw new IllegalStateException("Is Mediator's init method called?");
        }

        MediatorActivity.launch(application, permission);
    }

    static void doOnPerform() {
        if (onPerform != null && onPerform.get() != null) {
            onPerform.get().onPerform();
        }
    }

    static void doOnRejected() {
        if (onRejected != null && onRejected.get() != null) {
            onRejected.get().onRejected();
        } else if (defaultOnRejected != null) {
            defaultOnRejected.onRejected();
        }
    }

    public static final class Helper {
        private String permission;
        private OnPerform onPerform;
        private OnRejected onRejected;

        /**
         * Use {@link Mediator#request(String)} to get an instance.
         *
         * @param permission Requested permission.
         */
        private Helper(@NonNull String permission) {
            this.permission = permission;
        }

        /**
         * Set {@link OnPerform}.
         *
         * @param onPerform Continue performing when having permission.
         * @return The Helper instance.
         */
        public Helper onPerform(@NonNull OnPerform onPerform) {
            this.onPerform = onPerform;
            return this;
        }

        /**
         * Set {@link OnRejected}.
         *
         * @param onRejected Do when rejected.
         * @return The Helper instance.
         */
        public Helper onRejected(OnRejected onRejected) {
            this.onRejected = onRejected;
            return this;
        }

        /**
         * Delegate the Mediator to do all permission requests.
         */
        public void delegate() {
            if (permission == null || onPerform == null) {
                throw new IllegalStateException("'permission string' and 'perform method' required");
            }

            Mediator.delegate(permission, onPerform, onRejected);
        }
    }

    /**
     * Callback when permission granted.
     */
    public interface OnPerform {
        void onPerform();
    }

    /**
     * Callback when permission denied.
     */
    public interface OnRejected {
        void onRejected();
    }
}
