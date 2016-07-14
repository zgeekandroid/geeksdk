package com.commonslibrary.commons.utils;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * date        :  2016-01-27  13:26
 * author      :  Mickaecle gizthon
 * description :
 */
public class StringUtils {
    /**
     * 格式化下载速度字符串
     *
     * @param length 下载文件长度
     * @param count  已经下载的长度
     * @return
     */
    public static String formatDownloadSpeed(int length, int count) {
        return formatMB(count) + "/" + formatMB(length);
    }

    public static String formatKB(long length) {
        double unit = 1024;
        double kb = length / unit;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(kb) + "KB";
    }

    public static String formatMB(long length) {
        double unit = 1024;
        double mb = length / unit / unit;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(mb) + "MB";
    }


    public static int formatKBInt(long length) {
        double unit = 1024;
        double kb = length / unit ;

        return (int) kb;
    }

    public static List<String> stringsToList(final String[] src) {
        if (src == null || src.length == 0) {
            return null;
        }
        return Arrays.asList(src);
    }


    public static Map<String, Object> getUrlParams(String param) {
        Map<String, Object> map = new HashMap<>();
        if ("".equals(param) || null == param) {
            return map;
        }
        String[] params = param.split("&");

        for (String string : params) {
            String[] p = string.split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }

        return map;
    }

    public static String formatUrl(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.lastIndexOf("&"));
        }
        return s;
    }

    public static String formatValue(Map<String, Object> parameters, String key) {
        if (parameters == null || !parameters.containsKey(key)) {
            return "";
        }
        return String.valueOf(parameters.get(key));
    }
}
