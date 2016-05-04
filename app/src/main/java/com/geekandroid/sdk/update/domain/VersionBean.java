package com.geekandroid.sdk.update.domain;

/**
 * Created by lenovo on 2016/4/25.
 */
public class VersionBean {

    /**
     * code : 200
     * name :
     * message :
     * request_time : 2016-04-25 17:53:10
     */

    private StatusBean status;
    private TotalBean total;
    /**
     * app_is_update : 1
     * app_version_new : 2
     * app_is_force : 0
     * app_network_url : http://www.maicaim.com//App/PurchaseAPP.apk
     * app_update_description :
     */

    private BackinfoBean backinfo;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public TotalBean getTotal() {
        return total;
    }

    public void setTotal(TotalBean total) {
        this.total = total;
    }

    public BackinfoBean getBackinfo() {
        return backinfo;
    }

    public void setBackinfo(BackinfoBean backinfo) {
        this.backinfo = backinfo;
    }

    public static class StatusBean {
        private int code;
        private String name;
        private String message;
        private String request_time;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getRequest_time() {
            return request_time;
        }

        public void setRequest_time(String request_time) {
            this.request_time = request_time;
        }
    }

    public static class TotalBean {
    }

    public static class BackinfoBean {
        private int app_is_update;
        private String app_version_new;
        private int app_is_force;
        private String app_network_url;
        private String app_update_description;

        public int getApp_is_update() {
            return app_is_update;
        }

        public void setApp_is_update(int app_is_update) {
            this.app_is_update = app_is_update;
        }

        public String getApp_version_new() {
            return app_version_new;
        }

        public void setApp_version_new(String app_version_new) {
            this.app_version_new = app_version_new;
        }

        public int getApp_is_force() {
            return app_is_force;
        }

        public void setApp_is_force(int app_is_force) {
            this.app_is_force = app_is_force;
        }

        public String getApp_network_url() {
            return app_network_url;
        }

        public void setApp_network_url(String app_network_url) {
            this.app_network_url = app_network_url;
        }

        public String getApp_update_description() {
            return app_update_description;
        }

        public void setApp_update_description(String app_update_description) {
            this.app_update_description = app_update_description;
        }
    }
}
