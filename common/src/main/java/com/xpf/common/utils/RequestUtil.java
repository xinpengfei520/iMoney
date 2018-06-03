package com.anloq.common.utils;

import com.anloq.common.logger.AnloqLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xpf on 2017/5/11 :)
 * Function:网络请求的工具类
 */

public class RequestUtil {

    private static final String TAG = RequestUtil.class.getSimpleName();

    /**
     * 获取Json的name
     *
     * @param json
     * @return
     */
    public static String getName(String json) {
        JSONObject jsonObject = null;
        String name = "";
        try {
            jsonObject = new JSONObject(json);
            name = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 获取网络请求的code码
     */
    public static String getCode(String json) {
        JSONObject jsonObject = null;
        String code = "";
        try {
            jsonObject = new JSONObject(json);
            code = jsonObject.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (code) {
            case "1001":
                ToastUtil.show("短信验证码错误");
                break;
            case "1002":
                ToastUtil.show("http请求参数错误");
                break;
            case "1003":
                ToastUtil.show("Token过期，需重新登陆");
                break;
            case "1004":
                ToastUtil.show("用户不存在");
                break;
            case "1005":
                ToastUtil.show("密码错误");
                break;
            case "1006":
                ToastUtil.show("发送短信验证失败");
                break;
            case "1007":
                ToastUtil.show("缓存失效");
                break;
            case "1008":
                AnloqLog.w(TAG, "资源不存在，即没有更新的数据了...");
                break;
            case "1009":
                ToastUtil.show("创建住户失败");
                break;
            case "1010":
                ToastUtil.show("创建住户房间信息失败");
                break;
            case "1011":
                ToastUtil.show("创建虚拟钥匙失败");
                break;
            case "1012":
                AnloqLog.w(TAG, "获取业主信息失败");
                break;
            case "1013":
                ToastUtil.show("头像超过大小限制");
                break;
            case "1014":
                ToastUtil.show("头像保存失败");
                break;
            case "1015":
                ToastUtil.show("业主不存在");
                break;
            case "1016":
                ToastUtil.show("无授权权限");
                break;
            case "1017":
                ToastUtil.show("ERROR_SAVE_FEEDBACK");
                break;
            case "1018":
                ToastUtil.show("ERROR_DATABASE_OPERATE");
                break;
            case "1019":
                ToastUtil.show("您已经注册过，不能重复注册");
                break;
            case "2001":
                AnloqLog.e(TAG, "设备未激活！！！");
                break;
            case "2002":
                ToastUtil.show("设备已失效");
                break;
            case "2003":
                ToastUtil.show("上传截图失败");
                break;
            case "2050":
                AnloqLog.e(TAG, "ERROR_MESSAGE_SENDFAIL");
                break;
            case "10000":
                ToastUtil.show("ERROR_HACKER");
                break;
            default:
                break;
        }
        return code;
    }

}
