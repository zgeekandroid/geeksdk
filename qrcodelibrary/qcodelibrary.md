**qrcodelibrary 说明文档**

功能说明
  主要封装了扫码功能


引用说明
 基于com.google.zxing:core:3.2.0二次封装


配置说明
在项目的build.gradle 中只需要引入 相关aar即可
或
dependencies {
   ......
    compile project(path: ':qrcodeibrary')

}

接口说明
主要类 ScannerActivity 和 ViewfinderView
使用时 1.扫码的Activity需继承ScannerActivity
      2.自定义view继承ViewfinderView
根据实际需求实现及对结果进行处理
详见github MipcaActivityCapture 和QRCodeView

