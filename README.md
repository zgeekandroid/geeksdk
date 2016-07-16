# geeksdk
geeksdk is fast dev framework in andriod ..

##imageloaderlibrary 图片加载
imageloaderlibrary 将imageloader 进行再次封装得到的一个简单易用的通用库
使用方式如下：
```java
        ImageLoaderView normal = (ImageLoaderView) view.findViewById(R.id.normal);
        ImageLoaderView circle = (ImageLoaderView) view.findViewById(R.id.circle);
        ImageLoaderView round = (ImageLoaderView) view.findViewById(R.id.round);
   
        String url = "http://img4q.duitang.com/uploads/item/201411/04/20141104225919_ZR3h5.thumb.224_0XX.jpeg";
        normal.setUrl(url);
        circle.setCircleUrl(url);
        round.setRoundUrl(url);
```
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
