# geeksdk
geeksdk is fast dev framework in andriod ..
##imageloaderlibrary
imageloaderlibrary 将imageloader 进行再次封装得到的一个简单易用的通用库
```java
        ImageLoaderView normal = (ImageLoaderView) view.findViewById(R.id.normal);
        ImageLoaderView circle = (ImageLoaderView) view.findViewById(R.id.circle);
        ImageLoaderView round = (ImageLoaderView) view.findViewById(R.id.round);
   
        String url = "http://img4q.duitang.com/uploads/item/201411/04/20141104225919_ZR3h5.thumb.224_0XX.jpeg";
        normal.setUrl(url);
        circle.setCircleUrl(url);
        round.setRoundUrl(url);
```
