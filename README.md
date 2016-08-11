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
     compile 'com.zgeekandroid.sdk:imageloaderlibrary:1.0.5'
     compile 'com.zgeekandroid.sdk:commonslibrary:1.0.2'
```
2.初始化(一般在application中配置)
``` java
ImageLoaderManager.getInstance().init(this);
```


## locationlibrary 地图定位包
地图定位主要是将百度地图定位模块进行再次封装。同时添加了，百度权限请求，兼容Android 6.0 权限
使用方式如下：
``` java
BDLocationImpl.getInstance().start(Activity,new RequestCallBack<Location>() {
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
            compile 'com.zgeekandroid.sdk:locationlibrary:1.0.6'
            compile 'com.zgeekandroid.sdk:commonslibrary:1.0.2'
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
            android:process=":remote" >
            <intent-filter>
                <action android:name="com.baidu.location.service_v2.2" >
                </action>
            </intent-filter>
        </service>
```


## jpushlibrary 极光推送
jpushlibrary主要是将极光推送模块进行再次封装。已经打成aar的包，方便替换和编译
使用方式如下：

``` java
//1.先启动jpush
//2.再设置别名

  //启动jpush
   JPushImpl.getInstance().resumePush();

//设置别名
  JPushImpl.getInstance().setAlias("alias", new IPushCallBack() {
                     @Override
                     public void gotResult(int i, String s, Set<String> set) {
                         logI(" got Result : i = " + i + " ,s = " + s + " ,set = " + set);
                     }
                 });



  //停止jpush
   JPushImpl.getInstance().stopPush();
```
#### 配置
1.依赖
``` xml
        compile 'com.zgeekandroid.sdk:jpushlibrary:1.0.2'
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
4.manifest 配置(要按照这个顺序，可直接复制粘贴)
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



## paylibrary 支付模块
paylibrary主要是将支付宝和微信支付模块进行再次封装。已经打成aar的包，方便替换和编译

>支付流程为 ：  请求订单 -> 获得请求参数 -> 调用客户端支付 -> 回调查询支付

以下的所有方法都是继承之后按需求实现的。


在调用支付方法之前需要初始化

1.初始化方法 `init(Context)`

2.支付入口 对应 `pay(Map<String, Object> params, RequestCallBack<String> callBack)`

3.请求订单 对应  `requestOrder(Map<String, Object> params, RequestCallBack<String> callBack)`

4.获取参数  对应 `getPayParam(Map<String, Object> params, RequestCallBack<String> callBack) `

**注意:**
前面 请求订单  和 获取参数  并非是所有的应用都需要的。其最主要目的是为了获得支付宝和微信支付的相关支付参数。



5.真正的支付方法 对应 `doRealPay(Map<String, Object> params)`


`doRealPay(Map<String, Object> params)`  中的params中的参数与支付宝，微信支付中所需参数完全一致。更多详细请参考微信支付和支付宝支付下单参数。


6.回调查询 对应 `getPayResult(Map<String, Object> params, RequestCallBack<String> callBack)`





使用方式如下：
``` java
    //微信支付
   WeiXinPay weixipay = new WeiXinPay();
   weixipay.init(Context);
   Map<String, Object> parameters = getNullParameters();
   parameters.put("out_trade_no", out_trade_no);
   parameters.put("user_id", userId);
   //微信支付的单位是分， 如果传入的是double 类型，geeksdk 会自动转换成int 分，如果是int 就不会转换

    //1.传入的是int 不会再次转换
   int changeFee = (int) ArithUtils.mul(total_fee, 100);
   //2.传入的是double 会，自动转换成分
    传入的是double changeFee = total_fee;

   parameters.put("total_fee", changeFee);
   parameters.put("body", body);
   parameters.put("detail", body);
       .....
   weixipay.pay(parameters, RequestCallBack<String> callBack);






    //支付宝支付
   AliPay alipay = new AliPay();
   alipay.init(Context);

     Map<String, Object> parameters = getNullParameters();
      parameters.put("out_trade_no", out_trade_no);
      parameters.put("user_id", userId);
      //微信支付的单位是分， 如果传入的是double 类型，geeksdk 会自动转换成int 分，如果是int 就不会转换

       //1.传入的是int 不会再次转换
      int changeFee = (int) ArithUtils.mul(total_fee, 100);
      //2.传入的是double 会，自动转换成分
       传入的是double changeFee = total_fee;

      parameters.put("total_fee", changeFee);
      parameters.put("body", body);
      parameters.put("detail", body);
      alipay.pay(parameters, RequestCallBack<String> callBack);
```
#### 配置

微信支付必须在你当前的包下面建立一个`wxapi` 的包。如 `com.excample.xx.wxapi` 为了统一方便，将所有的支付相关的类都放在此包下。

##### 相关说明


>  请求订单 -> 获得请求参数 -> 调用客户端支付 -> 回调查询支付

```
|-wxapi
|----AliPay.java              继承CHAlipay.java 并且实现其参数构造函数
|----WeixinPay.java           继承CHWeixinPay.java 并且实现其参数构造函数
|----WXPayEntryActivity.java  微信默认回调页面
|----CashPay.java             继承CHCashPay.java 现金支付,可以将现金支付相关请求在此实现，这里仅仅是为了规范使用
|----YuEPay.java              继承CHYuEPay.java 余额支付，可以将余额支付相关请求在此实现，这里仅仅是规范使用
```

支付流程中所有的都是在 *Pay.java中实现的,支付入口 支付宝实现如下：

###### 支付宝支付

``` java

    //2.支付入口
    @Override
    public void pay(Map<String, Object> params, RequestCallBack<String> callBack) {
        if (activity == null) {
            throw new NullPointerException("没有初始化支付");
        }
        //给private_key赋值
        setPrivate_key(private_key);

        //显示进度条
        showProgress();

        this.parameters = params;


        //3.请求订单
        requestOrder(parameters, requestOrderCallBack);

        this.callBack = callBack;
    }


        private RequestCallBack<String> requestOrderCallBack = new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {

                     //result 解析 reslut 获得相关参数

                    Map<String, Object> parme = new HashMap<>();
                    parme.put("xx","xxx");

                    //4.获取相关参数
                    getPayParam(parme, getParamCallBack);

                    }

                    @Override
                    public void onFailure(String errorMessage, Exception exception) {
                        hideProgress(errorMessage);
                    }
                };

        private RequestCallBack<String> getParamCallBack = new RequestCallBack<String>() {
                @Override
                public void onSuccess(String result) {

                        //解析reslut 获得相关参数 如：阿里支付的公钥，在回调验证的时候会用上
                         alipayPublicKey

                       Map<String, Object> parme = new HashMap<>();
                       parme.put("subject", parameters.get("subject"));
                       parme.put("body", parameters.get("body"));
                       parme.put("out_trade_no", parameters.get("out_trade_no"));
                       parme.put("total_fee", parameters.get("total_fee"));
                       parme.put("seller_id", "SellerId换成自己的参数");
                       parme.put("partner", "AlipayPartner换成自己的参数");
                       parme.put("notify_url", "AlipayNotifyUrl换成自己的参数");
                       parme.put("spbillCreateIp", "Ip换成自己的参数");




                        //5.调用真实的支付
                        doRealPay(parme);
                }

                @Override
                public void onFailure(String errorMessage, Exception exception) {
                }
            };

           //6.回调验证是否支付成功
         @Override
            public void callClientSuccess(PayResult result) {
                String resultInfo = result.getResult();
                String content = getSignContent(resultInfo);
                String sign = getSign(resultInfo);

                //alipayPublicKey 是在在上面获取参数的时候获取到的，也可以直接写死在客户端

                boolean verify = SignUtils.verify(content, sign, alipayPublicKey);
                // 验签不正确
                if (!verify) {
                    hideProgress("支付数据异常，请重试");
                    return;
                }
                hideProgress();
                if (callBack == null) {
                    return;
                }
                callBack.onSuccess("支付成功");
            }

         @Override
        public void getPayParam(Map<String, Object> params, RequestCallBack<String> callBack) {

            //根据不同业务实现不同请求

        }

        @Override
        public void requestOrder(Map<String, Object> params, RequestCallBack<String> callBack) {

            //根据不同业务实现不同请求

        }
```
###### 微信支付

``` java
     //开启支付流程，请求订单 -> 获得请求参数 -> 调用客户端支付 -> 回调查询支付

       //2.支付入口
        @Override
        public void pay(Map<String, Object> params, RequestCallBack<String> callBack) {
            if (activity == null) {
                throw new NullPointerException("没有初始化支付");
            }

            if (!msgApi.isWXAppInstalled()) {
                ToastUtils.show(activity, "没有安装微信客户端,请先安装!");
                return;
            }
            //设置微信支付的APPID ，APPKEY ，MCHID
            setKeyAndID(API_KEY, APP_ID, MCH_ID);
            showProgress();

            this.callBack = callBack;
            this.parameters = params;

            //3.请求订单
            requestOrder(parameters, requestOrderCallBack);


        }

        private RequestCallBack<String> requestOrderCallBack = new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {

                     //result 解析 reslut 获得相关参数

                    Map<String, Object> parme = new HashMap<>();
                    parme.put("xx","xxx");

                    //4.获取相关参数
                    getPayParam(parme, getParamCallBack);

                    }

                    @Override
                    public void onFailure(String errorMessage, Exception exception) {
                        hideProgress(errorMessage);
                    }
                };





        private RequestCallBack<String> getParamCallBack = new RequestCallBack<String>() {
            @Override
            public void onSuccess(String result) {

            //从这里获取到支付的notify url  和 ip
                WeixinParameterEntity parameterEntity = presenter.fromJson(result, WeixinParameterEntity.class);
                Map<String, Object> para = new HashMap<>();
                para.put("detail",parameters.get("detail"));
                para.put("body",parameters.get("body"));
                para.put("out_trade_no",parameters.get("out_trade_no"));

                //特别说明：微信支付的传入的这个 total_fee
                //如果是double类型，就会自动 转换单位 元 为 分，也就是说，如，传入的是 1.0 元 -> 100 分
                //如果是int 类型，就默认不会转

                para.put("total_fee",parameters.get("total_fee"));
                para.put("attach",parameters.get("attach"));
                para.put("spbill_create_ip","ip换成自己的参数");
                para.put("notify_url", "WeixinNotifyurl换成自己的参数");


                doRealPay(para);
            }

            @Override
            public void onFailure(String errorMessage, Exception exception) {
                hideProgress(errorMessage);
            }
        };

        //6.回调验证是否支付成功
        @Override
        public void callClientSuccess(  boolean isSuccess) {
            //调用微信端成功
            //此处在微信客户端中，调用了微信  xx.xx.xx.wxapi.WXPayEntryActivity.java 进入这个类中，进行查询是否支付成功
            if (callBack == null){
                return;
            }
            if (isSuccess){
                //打开微信客户端
                callBack.onSuccess("");
            }else {
                callBack.onFailure("请升级最新版微信使用",new Exception(""));
            }
        }


         @Override
        public void getPayParam(Map<String, Object> params, RequestCallBack<String> callBack) {

            //根据不同业务实现不同请求

        }

        @Override
        public void requestOrder(Map<String, Object> params, RequestCallBack<String> callBack) {

            //根据不同业务实现不同请求

        }
```



1.依赖
```
    compile 'com.zgeekandroid.sdk:paylibrary:1.0.3'
```
2.manifest 配置
``` xml
    ....

<!--微信支付-->
<activity
android:name=".wxapi.WXPayEntryActivity"
android:exported="true"
android:screenOrientation="portrait"
android:windowSoftInputMode="adjustPan" />


<!--含有支付相关页面，必须指定对应的scheme 为 微信支付的appid-->
<activity
    android:name=".user.PayActivity"
    android:configChanges="orientation|keyboardHidden|navigation"
    android:exported="true"
    android:screenOrientation="portrait"
    android:windowSoftInputMode="adjustPan">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:scheme="微信appid" />
    </intent-filter>
</activity>
<!--微信支付-->


<!--支付宝-->
<activity
    android:name="com.alipay.sdk.app.H5PayActivity"
    android:configChanges="orientation|keyboardHidden|navigation"
    android:exported="false"
    android:screenOrientation="behind"
    android:windowSoftInputMode="adjustPan"/>
<activity
    android:name="com.alipay.sdk.auth.AuthActivity"
    android:configChanges="orientation|keyboardHidden|navigation"
    android:exported="false"
    android:screenOrientation="behind"
    android:windowSoftInputMode="adjustPan"/>
<!--支付宝-->
```
