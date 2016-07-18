# geeksdk
geeksdk is fast dev framework in andriod ..

##imageloaderlibrary 图片加载
imageloaderlibrary 将imageloader 进行再次封装得到的一个简单易用的通用库.
同时支持，对默认图片进行处理。比如 `setCircleUrl（url）` 会使得 图片加载失败的时候，默认的图片也会有圆角展示。同样的圆角正方形也是一样的道理。   
使用方式如下：
```java
        ImageLoaderView normal = (ImageLoaderView) view.findViewById(R.id.normal);
        ImageLoaderView circle = (ImageLoaderView) view.findViewById(R.id.circle);
        ImageLoaderView round = (ImageLoaderView) view.findViewById(R.id.round);
   
        String url = "http://img4q.duitang.com/uploads/item/201411/04/20141104225919_ZR3h5.thumb.224_0.jpeg";
        normal.setUrl(url);
        circle.setCircleUrl(url);
        round.setRoundUrl(url);
```

####设置加载失败的默认图片
 ```java
        ImageLoaderManager.getInstance().setResIdOnFailUri(resId);
 ```
 只需要在使用 `setUrl()` 之前使用 这句代码就可以了。一般，全局都会默认设置一个图片。
 
####配置
1.依赖包

```xml
     compile 'com.zgeekandroid.sdk:imageloaderlibrary:1.0.1'
     compile 'com.zgeekandroid.sdk:commonslibrary:1.0.0'
     compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
```
2.初始化(一般在application中配置)
```java
ImageLoaderManager.getInstance().init(this);
```


##locationlibrary 地图定位包
地图定位主要是将百度地图定位模块进行再次封装。同时添加了，百度权限请求，兼容Android 6.0 权限
使用方式如下：
```java
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
####配置  
1.依赖
```xml
            compile 'com.zgeekandroid.sdk:locationlibrary:1.0.1'
            compile 'com.zgeekandroid.sdk:commonslibrary:1.0.0'
            compile 'com.tbruyelle.rxpermissions:rxpermissions:0.7.0@aar'
 ```

2.初始化(一般在application中配置)
```java
     BDLocationImpl.getInstance().init(this);
```
3.manifest 配置
```xml
 <meta-data
            android:name="com.baidu.lbsapi.API_KEY"
            android:value="填写你的key" />
        <!-- //key:开发者申请的key-->
        <service
            android:name="com.baidu.location.f"
            android:enabled="true"
            android:process=":remote" />
```
