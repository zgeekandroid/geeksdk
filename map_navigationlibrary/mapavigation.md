**map_navigationlibrary 说明文档**

功能说明
  主要封装了百度地图、百度定位、导航功能


引用说明
 基于百度地图加以二次封装
链接：[http://lbsyun.baidu.com/index.php?title=android-locsdk]

配置说明
在项目的build.gradle 中只需要引入 相关aar即可
或
dependencies {
   ......
    compile project(path: ':map_navigationlibrary')

}

接口说明
使用：
一、地图
 新建MapActivity 继承 BDBaseMapActivity 根据需求实现
二、定位
1.初始化 BDLocationImpl.getInstance().init(this);
2.开始定位 BDLocationImpl.getInstance().star();
3.停止定位  BDLocationImpl.getInstance().stop();
详见github   LocationSampleFragment
三、导航
主要类RoutePlanSearch根据选择不同的方式选择路线
例：
DRIVE：
 mSearch.drivingSearch((new DrivingRoutePlanOption()).from(startNode).to(endNode));
BUS：
 mSearch.transitSearch((new TransitRoutePlanOption()).from(startNode).city(appContext.cityLoc.cityName).to(endNode));
WALK:
 mSearch.walkingSearch((new WalkingRoutePlanOption()).from(startNode).to(endNode));
 路径规划回调类 OnGetRoutePlanResultListener 根据实际需要实现
