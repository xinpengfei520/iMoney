package com.xpf.common.exceptions;

/**
 * Created by xpf on 2017/2/24:)
 * Function:This is where all the global exceptions declared.
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Application context is null.
     */
    public static final String APPLICATION_CONTEXT_IS_NULL = "Application context is null. Maybe you neither configured your application name with \"CommonApplication\" in your AndroidManifest.xml, nor called CommonApplication.initialize(Context) method.";
    public static final String APPLICATION_HANDLER_IS_NULL = "Application handler is null. Maybe you neither configured your application name with \"CommonApplication\" in your AndroidManifest.xml, nor called CommonApplication.initialize(Context) method.";
    public static final String APPLICATION_MAINTHREAD_IS_NULL = "Application mainThread is null. Maybe you neither configured your application name with \"CommonApplication\" in your AndroidManifest.xml, nor called CommonApplication.initialize(Context) method.";
    public static final String APPLICATION_MAINTHREADID_IS_NULL = "Application mainThreadId is null. Maybe you neither configured your application name with \"CommonApplication\" in your AndroidManifest.xml, nor called CommonApplication.initialize(Context) method.";

    /**
     * Constructor of GlobalException.
     *
     * @param errorMessage the description of this exception.
     */
    public GlobalException(String errorMessage) {
        super(errorMessage);
    }
}
