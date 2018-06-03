package com.anloq.common;

import android.app.Application;
import android.content.Context;

import com.anloq.common.exceptions.GlobalException;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by xpf on 2018/1/19 :)
 * Function:
 */

public class CommonApplication extends Application {

    /**
     * Global application context.
     */
    static Context sContext;

    /**
     * Construct of CommonApplication. Initialize application context.
     */
    public CommonApplication() {
        sContext = this;
    }

    /**
     * Use initialize(Context).
     *
     * @param context Application context.
     */
    public static void initialize(Context context) {
        sContext = context;
        initLogger();
    }

    /**
     * init logger adapter.
     */
    private static void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    /**
     * Get the global application context.
     *
     * @return Application context.
     */
    public static Context getContext() {
        if (sContext == null) {
            throw new GlobalException(GlobalException.APPLICATION_CONTEXT_IS_NULL);
        }
        return sContext;
    }
}
