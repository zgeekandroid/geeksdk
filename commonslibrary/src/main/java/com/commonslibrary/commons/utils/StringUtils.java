/*******************************************************************************
 *
 * Copyright (c) 2016 Mickael Gizthon . All rights reserved. Email:2013mzhou@gmail.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.commonslibrary.commons.utils;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * date        :  2016-01-27  13:26
 * author      :  Mickaecle gizthon
 * description :
 */
public class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }
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

    public static String optionValue(Map<String, Object> parameters, String key) {
        if (parameters == null || !parameters.containsKey(key)) {
            return "";
        }
        return String.valueOf(parameters.get(key));
    }
}
