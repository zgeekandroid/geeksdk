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

package com.geekandroid.sdk;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * date        :  2016-04-02  10:26
 * author      :  Mickaecle gizthon
 * description :
 */
public class AutoWebView extends WebView {
    public AutoWebView(Context context) {
        super(context.getApplicationContext());
    }

    public AutoWebView(Context context, AttributeSet attrs) {
        super(context.getApplicationContext(), attrs);
    }

    public AutoWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context.getApplicationContext(), attrs, defStyleAttr);
    }

    public void init() {
        setWebViewClient(new WebViewClient());
        getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

    }

    public void loadHtml(String body) {
        body = regStr(body);
        loadData(getHtmlData(body), "text/html; charset=utf-8", "utf-8");
    }

    public String regStr(String html) {
        html = html.replace("&quot;", "\"");
        html = html.replace("&amp;", "&");
        html = html.replace("&lt;", "<");
        html = html.replace("&gt;", ">");
        html = html.replace("&nbsp;", " ");
        return html;
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
}
