package com.xpf.common.cons;

/**
 * Created by xpf on 2017/12/9 :)
 * GitHub:xinpengfei520
 * Function:提供一个枚举类:将当前联网以后的状态以及可能返回的数据,封装在枚举类中
 */

public enum ResultState {

    ERROR(2), EMPTY(3), SUCCESS(4);

    int state;
    private String content;

    ResultState(int state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
