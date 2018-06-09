package com.xpf.http.core;

import com.xpf.http.core.request.RequestMethod;

/**
 * Created by xpf on 2017/11/8 :)
 * Function:
 */

public class Okla {

    /**
     * get a request method instance.
     *
     * @return
     */
    public static RequestMethod request() {
        return new RequestMethod();
    }
}
