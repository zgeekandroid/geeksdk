package com.commonslibrary.commons.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtils {
    /**
     * url regex
     */
    private static final String REGEX_URL = "^(http|https?|ftp|file|www)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    /**
     * email regex
     */
    private static final String REGEX_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

    private ValidatorUtils() {
    }

    /**
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     */
    public static boolean isMobilePhone(String mobilePhone) {
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][2345678]\\d{9}";
        return !TextUtils.isEmpty(mobilePhone) && mobilePhone.matches(telRegex);
    }

    /**
     * 判断是否合法的链接
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        if (TextUtils.isEmpty(url))
            return false;

        Pattern patt = Pattern.compile(REGEX_URL);
        Matcher matcher = patt.matcher(url);
        return matcher.matches();
    }


    //判断email格式是否正确
    public static boolean isEmail(String email) {
        Pattern p = Pattern.compile(REGEX_EMAIL);
        Matcher m = p.matcher(email);
        return m.matches();
    }

}
