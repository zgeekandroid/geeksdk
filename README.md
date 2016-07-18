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
 ```ImageLoaderManager.getInstance().setResIdOnFailUri(resId);```
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
