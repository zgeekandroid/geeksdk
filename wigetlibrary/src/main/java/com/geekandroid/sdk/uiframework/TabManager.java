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

package com.geekandroid.sdk.uiframework;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gizthon on 16/9/8.
 * 对FragmentTabhost 进行封装
 */
public class TabManager {
    private int tabHostResourceId;
    private int tabContentResourceId;

    private FragmentActivity activity;
    private Fragment fragment;
    private View view;
    private FragmentTabHost tabHost;

    private List<Class<?>> tabFragments = new ArrayList<>();
    private List<String> tabTexts = new ArrayList<>();
    private List<View> indicators = new ArrayList<>();

    private TabManager(Fragment context, View view, @IdRes int tabHostResourceId, @IdRes int tabContentResourceId) {
        this.tabContentResourceId = tabContentResourceId;
        this.tabHostResourceId = tabHostResourceId;
        this.fragment = context;
        this.view = view;
        init();
    }

    private TabManager(FragmentActivity context, @IdRes int tabHostResourceId, @IdRes int tabContentResourceId) {
        this.tabContentResourceId = tabContentResourceId;
        this.tabHostResourceId = tabHostResourceId;
        this.activity = context;
        init();
    }

    public void init() {

        if (tabHostResourceId == -1) {
            tabHostResourceId = android.R.id.tabhost;
        }
        if (tabContentResourceId == -1) {
            tabContentResourceId = com.geekandroid.sdk.R.id.tab_content;
        }

        if (isFragment()) {
            tabHost = (FragmentTabHost) this.view.findViewById(tabHostResourceId);
            tabHost.setup(fragment.getContext(), fragment.getChildFragmentManager(), tabContentResourceId);
        } else {
            tabHost = (FragmentTabHost) this.activity.findViewById(tabHostResourceId);
            tabHost.setup(activity, activity.getSupportFragmentManager(), tabContentResourceId);
        }

        //default not  divider
        tabHost.getTabWidget().setDividerDrawable(null);
    }

    public TabManager addTab(Class<?> tabFragment, String tabText, View indicator) {
        if (tabFragment == null || tabText == null || indicator == null) {
            throw new IllegalArgumentException("tabFragment == null || tabText == null || indicator == null");
        }

        if (tabHost == null) {
            throw new IllegalArgumentException("tabHost == null");
        }

        tabFragments.add(tabFragment);
        tabTexts.add(tabText);
        indicators.add(indicator);

        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabText).setIndicator(indicator);
        tabHost.addTab(tabSpec, tabFragment, null);

        return this;
    }

    public TabManager addTab(List<Class<?>> tabFragments, List<String> tabTexts, List<View> indicators) {

        if (tabFragments == null || tabTexts == null || indicators == null) {
            throw new IllegalArgumentException("tabFragments == null || tabTexts == null || indicators == null");
        }

        if (tabFragments.size() != tabTexts.size() || tabTexts.size() != indicators.size() || tabFragments.size() != indicators.size()) {
            throw new IllegalArgumentException("tabFragments.size() tabTexts.size() indicators.size() 不相等 ");
        }

        int tabSize = tabFragments.size();
        for (int i = 0; i < tabSize; i++) {
            addTab(tabFragments.get(i), tabTexts.get(i), indicators.get(i));
        }

        return this;
    }

    public boolean isFragment() {
        return activity == null && fragment != null && view != null;
    }

    public Fragment getCurrentFragment() {
        if (tabHost == null) {
            return null;
        }
        return getTableFragment(tabHost.getCurrentTab());
    }

    public int getCurrentTab() {
        if (tabHost == null) {
            return 0;
        }
        return tabHost.getCurrentTab();
    }

    public void setCurrentTab(int index) {
        if (tabHost == null) {
            return;
        }
        tabHost.setCurrentTab(index);
    }

    public FragmentTabHost getTabHost() {
        return tabHost;
    }


    public Fragment getTableFragment(int index) {
        try {
            if (isFragment()) {
                if (fragment.getChildFragmentManager().getFragments() == null || fragment.getChildFragmentManager().getFragments().isEmpty()) {
                    return null;
                }
                if (fragment.getChildFragmentManager().getFragments().size() <= index) {
                    return null;
                }
                return fragment.getChildFragmentManager().getFragments().get(index);
            } else {
                if (activity.getSupportFragmentManager().getFragments() == null || activity.getSupportFragmentManager().getFragments().isEmpty()) {
                    return null;
                }
                if (activity.getSupportFragmentManager().getFragments().size() <= index) {
                    return null;
                }
                return activity.getSupportFragmentManager().getFragments().get(index);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


   public static class Build {
        int tabHostResourceId = -1;
        int tabContentResourceId = -1;

        public Build findViewById(@IdRes int resId) {
            this.tabHostResourceId = resId;
            return this;
        }

        public Build setUp(@IdRes int resId) {
            this.tabContentResourceId = resId;
            return this;
        }

        public TabManager build(FragmentActivity activity) {
            return new TabManager(activity, tabHostResourceId, tabContentResourceId);
        }

        public TabManager build(Fragment fragment, View view) {
            return new TabManager(fragment, view, tabHostResourceId, tabContentResourceId);
        }
    }

}
