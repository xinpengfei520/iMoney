package com.xpf.p2p.jpush;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;

import com.growingio.android.sdk.utils.LogUtil;

import java.util.Locale;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;

/**
 * 处理 tag alias 相关的逻辑
 */
public class TagAliasOperatorHelper {

    private static final String TAG = "TagAliasOperatorHelper";

    public static int sequence = 1;
    /**
     * 增加
     */
    public static final int ACTION_ADD = 1;
    /**
     * 覆盖
     */
    public static final int ACTION_SET = 2;
    /**
     * 删除部分
     */
    public static final int ACTION_DELETE = 3;
    /**
     * 删除所有
     */
    public static final int ACTION_CLEAN = 4;
    /**
     * 查询
     */
    public static final int ACTION_GET = 5;

    public static final int ACTION_CHECK = 6;

    public static final int DELAY_SEND_ACTION = 1;

    public static final int DELAY_SET_MOBILE_NUMBER_ACTION = 2;

    private Context context;

    private static TagAliasOperatorHelper mInstance;

    private TagAliasOperatorHelper() {

    }

    public static TagAliasOperatorHelper getInstance() {
        if (mInstance == null) {
            synchronized (TagAliasOperatorHelper.class) {
                if (mInstance == null) {
                    mInstance = new TagAliasOperatorHelper();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        if (context != null) {
            this.context = context.getApplicationContext();
        }
    }

    private SparseArray<Object> setActionCache = new SparseArray<Object>();

    public Object get(int sequence) {
        return setActionCache.get(sequence);
    }

    public Object remove(int sequence) {
        return setActionCache.get(sequence);
    }

    public void put(int sequence, Object tagAliasBean) {
        setActionCache.put(sequence, tagAliasBean);
    }

    @SuppressLint("HandlerLeak")
    private Handler delaySendHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DELAY_SEND_ACTION:
                    if (msg.obj instanceof TagAliasBean) {
                        LogUtil.i(TAG, "on delay time");
                        sequence++;
                        TagAliasBean tagAliasBean = (TagAliasBean) msg.obj;
                        setActionCache.put(sequence, tagAliasBean);
                        if (context != null) {
                            handleAction(context, sequence, tagAliasBean);
                        } else {
                            LogUtil.e(TAG, "#unexcepted - context was null");
                        }
                    } else {
                        LogUtil.e(TAG, "#unexcepted - msg obj was incorrect");
                    }
                    break;
                case DELAY_SET_MOBILE_NUMBER_ACTION:
                    if (msg.obj instanceof String) {
                        LogUtil.i(TAG, "retry set mobile number");
                        sequence++;
                        String mobileNumber = (String) msg.obj;
                        setActionCache.put(sequence, mobileNumber);
                        if (context != null) {
                            handleAction(context, sequence, mobileNumber);
                        } else {
                            LogUtil.e(TAG, "#unexcepted - context was null");
                        }
                    } else {
                        LogUtil.e(TAG, "#unexcepted - msg obj was incorrect");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void handleAction(Context context, int sequence, String mobileNumber) {
        put(sequence, mobileNumber);
        LogUtil.d(TAG, "sequence:" + sequence + ",mobileNumber:" + mobileNumber);
        JPushInterface.setMobileNumber(context, sequence, mobileNumber);
    }

    /**
     * 处理设置tag
     */
    public void handleAction(Context context, int sequence, TagAliasBean tagAliasBean) {
        init(context);
        if (tagAliasBean == null) {
            LogUtil.e(TAG, "tagAliasBean was null");
            return;
        }
        put(sequence, tagAliasBean);
        if (tagAliasBean.isAliasAction) {
            switch (tagAliasBean.action) {
                case ACTION_GET:
                    JPushInterface.getAlias(context, sequence);
                    break;
                case ACTION_DELETE:
                    JPushInterface.deleteAlias(context, sequence);
                    break;
                case ACTION_SET:
                    JPushInterface.setAlias(context, sequence, tagAliasBean.alias);
                    break;
                default:
                    LogUtil.e(TAG, "unSupport alias action type");
                    break;
            }
        } else {
            switch (tagAliasBean.action) {
                case ACTION_ADD:
                    JPushInterface.addTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_SET:
                    JPushInterface.setTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_DELETE:
                    JPushInterface.deleteTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_CHECK:
                    // 一次只能 check 一个 tag
                    String tag = (String) tagAliasBean.tags.toArray()[0];
                    JPushInterface.checkTagBindState(context, sequence, tag);
                    break;
                case ACTION_GET:
                    JPushInterface.getAllTags(context, sequence);
                    break;
                case ACTION_CLEAN:
                    JPushInterface.cleanTags(context, sequence);
                    break;
                default:
                    LogUtil.e(TAG, "unSupport tag action type");
                    break;
            }
        }
    }

    private boolean RetryActionIfNeeded(int errorCode, TagAliasBean tagAliasBean) {
        if (!ExampleUtil.isConnected(context)) {
            LogUtil.e(TAG, "no network");
            return false;
        }
        // 返回的错误码为 6002 超时, 6014 服务器繁忙,都建议延迟重试
        if (errorCode == 6002 || errorCode == 6014) {
            LogUtil.d(TAG, "need retry");
            if (tagAliasBean != null) {
                Message message = new Message();
                message.what = DELAY_SEND_ACTION;
                message.obj = tagAliasBean;
                delaySendHandler.sendMessageDelayed(message, 1000 * 60);
                String logs = getRetryStr(tagAliasBean.isAliasAction, tagAliasBean.action, errorCode);
                ExampleUtil.showToast(logs, context);
                return true;
            }
        }
        return false;
    }

    private boolean RetrySetMobileNumberActionIfNeeded(int errorCode, String mobileNumber) {
        if (!ExampleUtil.isConnected(context)) {
            LogUtil.e(TAG, "no network");
            return false;
        }
        // 返回的错误码为 6002 超时,6024 服务器内部错误,建议稍后重试
        if (errorCode == 6002 || errorCode == 6024) {
            LogUtil.d(TAG, "need retry");
            Message message = new Message();
            message.what = DELAY_SET_MOBILE_NUMBER_ACTION;
            message.obj = mobileNumber;
            delaySendHandler.sendMessageDelayed(message, 1000 * 60);
            String str = "Failed to set mobile number due to %s. Try again after 60s.";
            str = String.format(Locale.ENGLISH, str, (errorCode == 6002 ? "timeout" : "server internal error”"));
            ExampleUtil.showToast(str, context);
            return true;
        }
        return false;

    }

    private String getRetryStr(boolean isAliasAction, int actionType, int errorCode) {
        String str = "Failed to %s %s due to %s. Try again after 60s.";
        str = String.format(Locale.ENGLISH, str, getActionStr(actionType), (isAliasAction ? "alias" : " tags"), (errorCode == 6002 ? "timeout" : "server too busy"));
        return str;
    }

    private String getActionStr(int actionType) {
        switch (actionType) {
            case ACTION_ADD:
                return "add";
            case ACTION_SET:
                return "set";
            case ACTION_DELETE:
                return "delete";
            case ACTION_GET:
                return "get";
            case ACTION_CLEAN:
                return "clean";
            case ACTION_CHECK:
                return "check";
            default:
                break;
        }

        return "unknown operation";
    }

    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        LogUtil.i(TAG, "action - onTagOperatorResult, sequence:" + sequence + ",tags:" + jPushMessage.getTags());
        LogUtil.i(TAG, "tags size:" + jPushMessage.getTags().size());
        init(context);
        // 根据 sequence 从之前操作缓存中获取缓存记录
        TagAliasBean tagAliasBean = (TagAliasBean) setActionCache.get(sequence);
        if (tagAliasBean == null) {
            ExampleUtil.showToast("获取缓存记录失败", context);
            return;
        }
        if (jPushMessage.getErrorCode() == 0) {
            LogUtil.i(TAG, "action - modify tag Success,sequence:" + sequence);
            setActionCache.remove(sequence);
            String logs = getActionStr(tagAliasBean.action) + " tags success";
            LogUtil.i(TAG, logs);
            ExampleUtil.showToast(logs, context);
        } else {
            String logs = "Failed to " + getActionStr(tagAliasBean.action) + " tags";
            if (jPushMessage.getErrorCode() == 6018) {
                // tag 数量超过限制,需要先清除一部分再 add
                logs += ", tags is exceed limit need to clean";
            }
            logs += ", errorCode:" + jPushMessage.getErrorCode();
            LogUtil.e(TAG, logs);
            if (!RetryActionIfNeeded(jPushMessage.getErrorCode(), tagAliasBean)) {
                ExampleUtil.showToast(logs, context);
            }
        }
    }

    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        LogUtil.i(TAG, "action - onCheckTagOperatorResult, sequence:" + sequence + ",checktag:" + jPushMessage.getCheckTag());
        init(context);
        // 根据 sequence 从之前操作缓存中获取缓存记录
        TagAliasBean tagAliasBean = (TagAliasBean) setActionCache.get(sequence);
        if (tagAliasBean == null) {
            ExampleUtil.showToast("获取缓存记录失败", context);
            return;
        }
        if (jPushMessage.getErrorCode() == 0) {
            LogUtil.i(TAG, "tagBean:" + tagAliasBean);
            setActionCache.remove(sequence);
            String logs = getActionStr(tagAliasBean.action) + " tag " + jPushMessage.getCheckTag() + " bind state success,state:" + jPushMessage.getTagCheckStateResult();
            LogUtil.i(TAG, logs);
            ExampleUtil.showToast(logs, context);
        } else {
            String logs = "Failed to " + getActionStr(tagAliasBean.action) + " tags, errorCode:" + jPushMessage.getErrorCode();
            LogUtil.e(TAG, logs);
            if (!RetryActionIfNeeded(jPushMessage.getErrorCode(), tagAliasBean)) {
                ExampleUtil.showToast(logs, context);
            }
        }
    }

    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        LogUtil.i(TAG, "action - onAliasOperatorResult, sequence:" + sequence + ",alias:" + jPushMessage.getAlias());
        init(context);
        // 根据 sequence 从之前操作缓存中获取缓存记录
        TagAliasBean tagAliasBean = (TagAliasBean) setActionCache.get(sequence);
        if (tagAliasBean == null) {
            ExampleUtil.showToast("获取缓存记录失败", context);
            return;
        }
        if (jPushMessage.getErrorCode() == 0) {
            LogUtil.i(TAG, "action - modify alias Success,sequence:" + sequence);
            setActionCache.remove(sequence);
            String logs = getActionStr(tagAliasBean.action) + " alias success";
            LogUtil.i(TAG, logs);
            //ExampleUtil.showToast(logs, context);
        } else {
            String logs = "Failed to " + getActionStr(tagAliasBean.action) + " alias, errorCode:" + jPushMessage.getErrorCode();
            LogUtil.e(TAG, logs);
            if (!RetryActionIfNeeded(jPushMessage.getErrorCode(), tagAliasBean)) {
                ExampleUtil.showToast(logs, context);
            }
        }
    }

    /**
     * 设置手机号码回调
     *
     * @param context
     * @param jPushMessage
     */
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        LogUtil.i(TAG, "action - onMobileNumberOperatorResult, sequence:" + sequence + ",mobileNumber:" + jPushMessage.getMobileNumber());
        init(context);
        if (jPushMessage.getErrorCode() == 0) {
            LogUtil.i(TAG, "action - set mobile number Success,sequence:" + sequence);
            setActionCache.remove(sequence);
        } else {
            String logs = "Failed to set mobile number, errorCode:" + jPushMessage.getErrorCode();
            LogUtil.e(TAG, logs);
            if (!RetrySetMobileNumberActionIfNeeded(jPushMessage.getErrorCode(), jPushMessage.getMobileNumber())) {
                ExampleUtil.showToast(logs, context);
            }
        }
    }

    public static class TagAliasBean {
        public int action;
        public Set<String> tags;
        public String alias;
        public boolean isAliasAction;

        @Override
        public String toString() {
            return "TagAliasBean{" +
                    "action=" + action +
                    ", tags=" + tags +
                    ", alias='" + alias + '\'' +
                    ", isAliasAction=" + isAliasAction +
                    '}';
        }
    }

}
