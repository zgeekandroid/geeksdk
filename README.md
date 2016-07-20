# geeksdk
geeksdk is fast dev framework in andriod ...

>我们不重复造轮子，我们仅仅是让轮子更好使

## imageloaderlibrary 图片加载
imageloaderlibrary 将imageloader 进行再次封装得到的一个简单易用的通用库.
同时支持，对默认图片进行处理。比如 `setCircleUrl（url）` 会使得 图片加载失败的时候，默认的图片也会有圆角展示。同样的圆角正方形也是一样的道理。   
使用方式如下：
``` java
        ImageLoaderView normal = (ImageLoaderView) view.findViewById(R.id.normal);
        ImageLoaderView circle = (ImageLoaderView) view.findViewById(R.id.circle);
        ImageLoaderView round = (ImageLoaderView) view.findViewById(R.id.round);
   
        String url = "http://img4q.duitang.com/uploads/item/201411/04/20141104225919_ZR3h5.thumb.224_0.jpeg";
        normal.setUrl(url);
        circle.setCircleUrl(url);
        round.setRoundUrl(url);
```

#### 设置加载失败的默认图片
 ``` java
        ImageLoaderManager.getInstance().setResIdOnFailUri(resId);
 ```
 只需要在使用 `setUrl()` 之前使用 这句代码就可以了。一般，全局都会默认设置一个图片。
 
#### 配置
1.依赖包

``` xml
     compile 'com.zgeekandroid.sdk:imageloaderlibrary:1.0.1'
     compile 'com.zgeekandroid.sdk:commonslibrary:1.0.0'
     compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
```
2.初始化(一般在application中配置)
``` java
ImageLoaderManager.getInstance().init(this);
```


## locationlibrary 地图定位包
地图定位主要是将百度地图定位模块进行再次封装。同时添加了，百度权限请求，兼容Android 6.0 权限
使用方式如下：
``` java
BDLocationImpl.getInstance().start(new RequestCallBack<Location>() {
            @Override
            public void onSuccess(Location result) {
               //如果需要停止，否则不用停止
                BDLocationImpl.getInstance().stop();
            }

            @Override
            public void onFailure(String errorMessage, Exception exception) {
                 //如果需要停止，否则不用停止
                BDLocationImpl.getInstance().stop();
            }
        });
```
#### 配置
1.依赖
``` xml
            compile 'com.zgeekandroid.sdk:locationlibrary:1.0.1'
            compile 'com.zgeekandroid.sdk:commonslibrary:1.0.0'
            compile 'com.tbruyelle.rxpermissions:rxpermissions:0.7.0@aar'
 ```


2.初始化(一般在application中配置)
``` java
     BDLocationImpl.getInstance().init(this);
```
3.manifest 配置
``` xml
 <meta-data
            android:name="com.baidu.lbsapi.API_KEY"
            android:value="填写你的key" />
        <!-- //key:开发者申请的key-->
        <service
            android:name="com.baidu.location.f"
            android:enabled="true"
            android:process=":remote" />
```
4.由于用到了lambda表达式，所以需要引用lambda相关包

在项目中的build.gralde中添加如下代码:
``` xml
buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath 'me.tatarka:gradle-retrolambda:3.2.3'
    }
}

// Required because retrolambda is on maven central
repositories {
    mavenCentral()
}
apply plugin: 'me.tatarka.retrolambda'
```
在build.gralde  的 android 配置下 添加jdk 1.8 的兼容
``` xml
 compileOptions {
        sourceCompatibility 1.8
        targetCompatibility 1.8
    }
```

## jpushlibrary 极光推送
jpushlibrary主要是将极光推送模块进行再次封装。已经打成aar的包，方便替换和编译
使用方式如下：

``` java
//设置别名
  JPushImpl.getInstance().setAlias("alias", new IPushCallBack() {
                     @Override
                     public void gotResult(int i, String s, Set<String> set) {
                         logI(" got Result : i = " + i + " ,s = " + s + " ,set = " + set);
                     }
                 });

  //启动jpush
   JPushImpl.getInstance().resumePush();

  //停止jpush
   JPushImpl.getInstance().stopPush();
```
#### 配置
1.依赖
``` xml
        compile 'com.zgeekandroid.sdk:jpushlibrary:1.0.0'
```

2.初始化(一般在Application中)
 ``` java
    JPushImpl.getInstance().setDebugMode(SystemConfig.isDebug());
    JPushImpl.getInstance().init(this);
 ```
3.基类配置（一般配置在baseActivity中）
``` java
 @Override
    protected void onResume() {
        super.onResume();
        JPushImpl.getInstance().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushImpl.getInstance().onPause(this);
    }
```
4.manifest 配置(一定要按照这个顺序，可直接复制粘贴)
>1.`JPushOpenActivity` 替换成你需要打开的那个`activity`，具体配置在`BroadcastReceiver`中
>2.`JPushReceiver`替换成你需要接收广播的那个`BroadcastReceiver`
>3.`com.geekandroid.sdk.sample`替换成你的包名
>4.`JPUSH_APPKEY` 标签的值 替换成你自己的`appkey`

```xml
<manifest>
<application >

 <!-- For test only 测试状态通知栏，需要打开的Activity -->

    <activity android:name=".JPushOpenActivity" android:exported="false">
        <intent-filter>
            <action android:name="jpush.testAction" />
            <category android:name="jpush.testCategory" />
        </intent-filter>
    </activity>

    <!-- Rich push 核心功能 since 2.0.6-->
    <activity
        android:name="cn.jpush.android.ui.PopWinActivity"
        android:theme="@style/MyDialogStyle"
        android:exported="false">
    </activity>

    <!-- Required SDK核心功能-->
    <activity
        android:name="cn.jpush.android.ui.PushActivity"
        android:configChanges="orientation|keyboardHidden"

        android:exported="false">

        <intent-filter>
            <action android:name="cn.jpush.android.ui.PushActivity" />
            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="com.geekandroid.sdk.sample" />
        </intent-filter>
    </activity>
    <!-- Required  SDK核心功能-->
    <service
        android:name="cn.jpush.android.service.DownloadService"
        android:enabled="true"
        android:exported="false" >
    </service>


    <!-- Required SDK 核心功能-->
    <!-- 可配置android:process参数将PushService放在其他进程中 -->
    <service
        android:name="cn.jpush.android.service.PushService"
        android:enabled="true"
        android:exported="false">
        <intent-filter>
            <action android:name="cn.jpush.android.intent.REGISTER" />
            <action android:name="cn.jpush.android.intent.REPORT" />
            <action android:name="cn.jpush.android.intent.PushService" />
            <action android:name="cn.jpush.android.intent.PUSH_TIME" />
        </intent-filter>
    </service>

    <!-- since 1.8.0 option 可选项。用于同一设备中不同应用的JPush服务相互拉起的功能。 -->
    <!-- 若不启用该功能可删除该组件，将不拉起其他应用也不能被其他应用拉起 -->
    <service
        android:name="cn.jpush.android.service.DaemonService"
        android:enabled="true"
        android:exported="true">
        <intent-filter>
            <action android:name="cn.jpush.android.intent.DaemonService" />
            <category android:name="com.geekandroid.sdk.sample" />
        </intent-filter>

    </service>

    <!-- Required SDK核心功能-->
    <receiver
        android:name="cn.jpush.android.service.PushReceiver"
        android:enabled="true"
        android:exported="false">
        <intent-filter android:priority="1000">
            <action android:name="cn.jpush.android.intent.NOTIFICATION_RECEIVED_PROXY" />   <!--Required  显示通知栏 -->
            <category android:name="com.geekandroid.sdk.sample" />
        </intent-filter>
        <intent-filter>
            <action android:name="android.intent.action.USER_PRESENT" />
            <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
        </intent-filter>
        <!-- Optional -->
        <intent-filter>
            <action android:name="android.intent.action.PACKAGE_ADDED" />
            <action android:name="android.intent.action.PACKAGE_REMOVED" />
            <data android:scheme="package" />
        </intent-filter>

    </receiver>

    <!-- Required SDK核心功能-->
    <receiver android:name="cn.jpush.android.service.AlarmReceiver" android:exported="false"/>

    <!-- User defined.  For test only  用户自定义的广播接收器-->
    <receiver
        android:name=".JPushReceiver"
        android:exported="false"
        android:enabled="true">
        <intent-filter>
            <action android:name="cn.jpush.android.intent.REGISTRATION" /> <!--Required  用户注册SDK的intent-->
            <action android:name="cn.jpush.android.intent.UNREGISTRATION" />
            <action android:name="cn.jpush.android.intent.MESSAGE_RECEIVED" /> <!--Required  用户接收SDK消息的intent-->
            <action android:name="cn.jpush.android.intent.NOTIFICATION_RECEIVED" /> <!--Required  用户接收SDK通知栏信息的intent-->
            <action android:name="cn.jpush.android.intent.NOTIFICATION_OPENED" /> <!--Required  用户打开自定义通知栏的intent-->
            <action android:name="cn.jpush.android.intent.ACTION_RICHPUSH_CALLBACK" /> <!--Optional 用户接受Rich Push Javascript 回调函数的intent-->
            <action android:name="cn.jpush.android.intent.CONNECTION" /><!-- 接收网络变化 连接/断开 since 1.6.3 -->
            <category android:name="com.geekandroid.sdk.sample" />
        </intent-filter>
    </receiver>


    <!-- Required  . Enable it you can get statistics data with channel -->
    <meta-data android:name="JPUSH_CHANNEL" android:value="developer-default"/>
    <meta-data android:name="JPUSH_APPKEY" android:value="xxxx" /> <!--  </>值来自开发者平台取得的AppKey-->

</application>

    <!-- Required  一些系统要求的权限，如访问网络等-->
<uses-permission android:name="com.geekandroid.sdk.sample.permission.JPUSH_MESSAGE" />
<permission
android:name="com.geekandroid.sdk.sample.permission.JPUSH_MESSAGE"
android:protectionLevel="signature" />
</manifest>
```
5.`JPushReceiver` 默认实现
```java
public class JPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[JPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[JPushReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[JPushReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 用户点击打开了通知");
			JPushImpl.getInstance().clearAllNotifications();


        	//打开自定义的Activity
        	Intent i = new Intent(context, JPushOpenActivity.class);
        	i.putExtras(bundle);
        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[JPushReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[JPushReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[JPushReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	//for receive customer msg from jpush server

	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
//		 if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(KEY_MESSAGE, message);
			if (!isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
//		}
	}

	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}
}

```