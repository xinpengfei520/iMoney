package com.xpf.p2p.jpush;

import cn.jpush.android.service.JCommonService;

/**
 * Created by x-sir on 2019/05/07 :)
 * Function:如果你使用的 JCore 是 2.0.0 及以上的版本，需要额外在 AndroidManifest 中配置一个 Service，
 * 以在更多手机平台上获得更稳定的支持，这个 Service 要继承 JCommonService
 */
public class JPushService extends JCommonService {

    public JPushService() {

    }
}
