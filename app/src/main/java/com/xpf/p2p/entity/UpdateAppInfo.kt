package com.xpf.p2p.entity

/**
 * Created by x-sir on 2017/12/22.
 * Function:app 更新的 Bean
 */
class UpdateAppInfo {
    var code: Int = 0
    var message: String? = null
    var data: DataBean? = null

    class DataBean {
        var buildBuildVersion: String? = null
        var downloadURL: String? = null
        var buildVersionNo: Int = 0
        var buildVersion: String? = null
        var buildShortcutUrl: String? = null
        var buildUpdateDescription: String? = null
    }
}
