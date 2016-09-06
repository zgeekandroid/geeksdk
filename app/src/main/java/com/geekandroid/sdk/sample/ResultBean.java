package com.geekandroid.sdk.sample;

/**
 * Created by gizthon on 16/9/6.
 */
public class ResultBean {

    /**
     * code : 107
     * name : user_id
     * message : 用户名错误
     * request_time : 2016-09-06 20:43:17
     */

    private StatusBean status;
    /**
     * h_userid : null
     */

    private TotalBean total;
    /**
     * agent_id : null
     * agent_name : null
     * user_id : null
     * user_name : null
     * mobile_phone : null
     * mail : null
     * state : null
     * consignee : null
     * consignee_address : null
     * consignee_phone : null
     * longitude : null
     * latitude : null
     * money : null
     * h_integral : 0
     * qq : null
     * wx : null
     * name : null
     * constacts_name : null
     * user_type : 0
     * user_card : null
     * sex : null
     * head_image_thum : null
     * head_image_orig : null
     * is_nocheck_paypassword : 0
     * is_receive_message : 0
     * isseted_paypassword : 0
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
        private Object h_userid;

        public Object getH_userid() {
            return h_userid;
        }

        public void setH_userid(Object h_userid) {
            this.h_userid = h_userid;
        }
    }

    public static class BackinfoBean {
        private Object agent_id;
        private Object agent_name;
        private Object user_id;
        private Object user_name;
        private Object mobile_phone;
        private Object mail;
        private Object state;
        private Object consignee;
        private Object consignee_address;
        private Object consignee_phone;
        private Object longitude;
        private Object latitude;
        private Object money;
        private int h_integral;
        private Object qq;
        private Object wx;
        private Object name;
        private Object constacts_name;
        private int user_type;
        private Object user_card;
        private Object sex;
        private Object head_image_thum;
        private Object head_image_orig;
        private int is_nocheck_paypassword;
        private int is_receive_message;
        private int isseted_paypassword;

        @Override
        public String toString() {
            return "BackinfoBean{" +
                    "agent_id=" + agent_id +
                    ", agent_name=" + agent_name +
                    ", user_id=" + user_id +
                    ", user_name=" + user_name +
                    ", mobile_phone=" + mobile_phone +
                    ", mail=" + mail +
                    ", state=" + state +
                    ", consignee=" + consignee +
                    ", consignee_address=" + consignee_address +
                    ", consignee_phone=" + consignee_phone +
                    ", longitude=" + longitude +
                    ", latitude=" + latitude +
                    ", money=" + money +
                    ", h_integral=" + h_integral +
                    ", qq=" + qq +
                    ", wx=" + wx +
                    ", name=" + name +
                    ", constacts_name=" + constacts_name +
                    ", user_type=" + user_type +
                    ", user_card=" + user_card +
                    ", sex=" + sex +
                    ", head_image_thum=" + head_image_thum +
                    ", head_image_orig=" + head_image_orig +
                    ", is_nocheck_paypassword=" + is_nocheck_paypassword +
                    ", is_receive_message=" + is_receive_message +
                    ", isseted_paypassword=" + isseted_paypassword +
                    '}';
        }

        public Object getAgent_id() {
            return agent_id;
        }

        public void setAgent_id(Object agent_id) {
            this.agent_id = agent_id;
        }

        public Object getAgent_name() {
            return agent_name;
        }

        public void setAgent_name(Object agent_name) {
            this.agent_name = agent_name;
        }

        public Object getUser_id() {
            return user_id;
        }

        public void setUser_id(Object user_id) {
            this.user_id = user_id;
        }

        public Object getUser_name() {
            return user_name;
        }

        public void setUser_name(Object user_name) {
            this.user_name = user_name;
        }

        public Object getMobile_phone() {
            return mobile_phone;
        }

        public void setMobile_phone(Object mobile_phone) {
            this.mobile_phone = mobile_phone;
        }

        public Object getMail() {
            return mail;
        }

        public void setMail(Object mail) {
            this.mail = mail;
        }

        public Object getState() {
            return state;
        }

        public void setState(Object state) {
            this.state = state;
        }

        public Object getConsignee() {
            return consignee;
        }

        public void setConsignee(Object consignee) {
            this.consignee = consignee;
        }

        public Object getConsignee_address() {
            return consignee_address;
        }

        public void setConsignee_address(Object consignee_address) {
            this.consignee_address = consignee_address;
        }

        public Object getConsignee_phone() {
            return consignee_phone;
        }

        public void setConsignee_phone(Object consignee_phone) {
            this.consignee_phone = consignee_phone;
        }

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(Object longitude) {
            this.longitude = longitude;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(Object latitude) {
            this.latitude = latitude;
        }

        public Object getMoney() {
            return money;
        }

        public void setMoney(Object money) {
            this.money = money;
        }

        public int getH_integral() {
            return h_integral;
        }

        public void setH_integral(int h_integral) {
            this.h_integral = h_integral;
        }

        public Object getQq() {
            return qq;
        }

        public void setQq(Object qq) {
            this.qq = qq;
        }

        public Object getWx() {
            return wx;
        }

        public void setWx(Object wx) {
            this.wx = wx;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getConstacts_name() {
            return constacts_name;
        }

        public void setConstacts_name(Object constacts_name) {
            this.constacts_name = constacts_name;
        }

        public int getUser_type() {
            return user_type;
        }

        public void setUser_type(int user_type) {
            this.user_type = user_type;
        }

        public Object getUser_card() {
            return user_card;
        }

        public void setUser_card(Object user_card) {
            this.user_card = user_card;
        }

        public Object getSex() {
            return sex;
        }

        public void setSex(Object sex) {
            this.sex = sex;
        }

        public Object getHead_image_thum() {
            return head_image_thum;
        }

        public void setHead_image_thum(Object head_image_thum) {
            this.head_image_thum = head_image_thum;
        }

        public Object getHead_image_orig() {
            return head_image_orig;
        }

        public void setHead_image_orig(Object head_image_orig) {
            this.head_image_orig = head_image_orig;
        }

        public int getIs_nocheck_paypassword() {
            return is_nocheck_paypassword;
        }

        public void setIs_nocheck_paypassword(int is_nocheck_paypassword) {
            this.is_nocheck_paypassword = is_nocheck_paypassword;
        }

        public int getIs_receive_message() {
            return is_receive_message;
        }

        public void setIs_receive_message(int is_receive_message) {
            this.is_receive_message = is_receive_message;
        }

        public int getIsseted_paypassword() {
            return isseted_paypassword;
        }

        public void setIsseted_paypassword(int isseted_paypassword) {
            this.isseted_paypassword = isseted_paypassword;
        }
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "status=" + status +
                ", total=" + total +
                ", backinfo=" + backinfo +
                '}';
    }
}
