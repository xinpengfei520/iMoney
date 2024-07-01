package com.xpf.p2p.entity;

/**
 * Created by x-sir on 2017/12/22.
 * Function:app 更新的 Bean
 */
public class UpdateAppInfo {

    /**
     * code : 0
     * message :
     * data : {"buildBuildVersion":"2","downloadURL":"https://www.pgyer.com/app/installUpdate/e2383fdf01ad574f22af68c78b5b025a?sig=0cHidf2Gm0TN75zQq5624MBRdKJ06MqKk9y%2F0sV3BfrJ4juvyahoSzsjZIvpHOcL","buildVersionNo":"1","buildVersion":"1.0","buildShortcutUrl":"https://www.pgyer.com/healthyantai","buildUpdateDescription":"改服务地址"}
     */

    private int code;
    private String message;

    /**
     * buildBuildVersion : 2
     * downloadURL : https://www.pgyer.com/app/installUpdate/e2383fdf01ad574f22af68c78b5b025a?sig=0cHidf2Gm0TN75zQq5624MBRdKJ06MqKk9y%2F0sV3BfrJ4juvyahoSzsjZIvpHOcL
     * buildVersionNo : 1
     * buildVersion : 1.0
     * buildShortcutUrl : https://www.pgyer.com/healthyantai
     * buildUpdateDescription : 改服务地址
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String buildBuildVersion;
        private String downloadURL;
        private int buildVersionNo;
        private String buildVersion;
        private String buildShortcutUrl;
        private String buildUpdateDescription;

        public String getBuildBuildVersion() {
            return buildBuildVersion;
        }

        public void setBuildBuildVersion(String buildBuildVersion) {
            this.buildBuildVersion = buildBuildVersion;
        }

        public String getDownloadURL() {
            return downloadURL;
        }

        public void setDownloadURL(String downloadURL) {
            this.downloadURL = downloadURL;
        }

        public int getBuildVersionNo() {
            return buildVersionNo;
        }

        public void setBuildVersionNo(int buildVersionNo) {
            this.buildVersionNo = buildVersionNo;
        }

        public String getBuildVersion() {
            return buildVersion;
        }

        public void setBuildVersion(String buildVersion) {
            this.buildVersion = buildVersion;
        }

        public String getBuildShortcutUrl() {
            return buildShortcutUrl;
        }

        public void setBuildShortcutUrl(String buildShortcutUrl) {
            this.buildShortcutUrl = buildShortcutUrl;
        }

        public String getBuildUpdateDescription() {
            return buildUpdateDescription;
        }

        public void setBuildUpdateDescription(String buildUpdateDescription) {
            this.buildUpdateDescription = buildUpdateDescription;
        }
    }
}
