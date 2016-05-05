**commonlibrary 说明文档**

功能说明
 主要封装了一些通用的基础工具类，如字符串工具类，手机邮箱等正则匹配的工具类，等等


引用说明
 集以往开发app经验所封装的lib，每一个工具类都具有通用性，暂无相关文档链接

配置说明
在项目的build.gradle 中只需要引入 相关aar即可
或
dependencies {
   ......
    compile project(path: ':commonslibrary')

}

接口说明
主要包括以下工具类
AESUtils 加密
ArithUtils 运算
Base64Utils 转化
BitmapUtils
CommonUtils 公共方法
DateUtils 时间
DeviceUtils
EncryptionUtils
FileUtils
JsonUtils
JudgeUtils 判断
KeyBoadUtils
LogFileUtils
LogUtils
PhoneUtils
RSAUtils
SharePreferenceUtil
StringUtils
ToastUtils
ViewUtils
网络请求 BaseRemoteModel

使用说明
一、工具类的使用 直接使用某工具类的对应方法即可
例：StringUtils.isEmpty(obj)
二、网络请求的使用
直接使用或继承BaseRemoteModel 类的实例 使用对应的网络请求方法
doPost();
doGet();
doUpload();
doDownLoad();
详见 github CommonSampleFragment