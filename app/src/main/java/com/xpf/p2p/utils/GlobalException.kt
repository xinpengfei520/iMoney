package com.xpf.p2p.utils

/**
 * Created by xpf on 2017/2/24:)
 * Function:This is where all the global exceptions declared.
 */
class GlobalException(errorMessage: String) : RuntimeException(errorMessage) {

    companion object {
        private const val serialVersionUID = 1L

        const val APPLICATION_CONTEXT_IS_NULL = "Application context is null. Maybe you neither configured your application name with \"CommonApplication\" in your AndroidManifest.xml, nor called CommonApplication.initialize(Context) method."
        const val APPLICATION_HANDLER_IS_NULL = "Application handler is null. Maybe you neither configured your application name with \"CommonApplication\" in your AndroidManifest.xml, nor called CommonApplication.initialize(Context) method."
        const val APPLICATION_MAINTHREAD_IS_NULL = "Application mainThread is null. Maybe you neither configured your application name with \"CommonApplication\" in your AndroidManifest.xml, nor called CommonApplication.initialize(Context) method."
        const val APPLICATION_MAINTHREADID_IS_NULL = "Application mainThreadId is null. Maybe you neither configured your application name with \"CommonApplication\" in your AndroidManifest.xml, nor called CommonApplication.initialize(Context) method."
    }
}
