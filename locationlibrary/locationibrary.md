**locationlibrary 说明文档**

功能说明
  主要封装了百度定位功能


引用说明
 基于百度定位加以二次封装
链接：[http://lbsyun.baidu.com/index.php?title=android-locsdk]

配置说明
在项目的build.gradle 中只需要引入 相关aar即可
或
dependencies {
   ......
    compile project(path: ':locationlibrary')

}

接口说明
使用：

1.初始化 BDLocationImpl.getInstance().init(this);
2.开始定位 BDLocationImpl.getInstance().star();
3.停止定位  BDLocationImpl.getInstance().stop();
详见github   LocationSampleFragment

