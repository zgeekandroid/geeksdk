package com.commonslibrary.commons.net;


import android.text.TextUtils;


import com.commonslibrary.commons.config.SystemConfig;
import com.commonslibrary.commons.handler.WeakHandlerNew;
import com.commonslibrary.commons.utils.LogUtils;
import com.commonslibrary.commons.utils.MD5;
import com.commonslibrary.commons.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * date        :  2015-11-30  10:41
 * author      :  Mickaecle gizthon
 * description :
 */
public class DefaultOkHttpIml implements IRequestRemote<String> {

    private WeakHandlerNew mHandler;
    private Object tag = DefaultOkHttpIml.class;
    private static DefaultOkHttpIml mInstance;
    private OkHttpClient mOkHttpClient;


    private DefaultOkHttpIml() {
        mHandler = new WeakHandlerNew();
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
//                .addInterceptor(new GzipRequestInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .build();
    }

    public static DefaultOkHttpIml getInstance() {
        if (null == mInstance) {
            synchronized (DefaultOkHttpIml.class) {
                if (null == mInstance) {
                    mInstance = new DefaultOkHttpIml();
                }
            }
        }
        return mInstance;
    }

    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
//            LogUtils.i(String.format("okhttp request %s on %s%n%s",
//                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            LogUtils.i(String.format("geeksdk  for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, ""/*response.headers()*/));

            return response;
        }
    }

    @Override
    public void doGet(String url, Map<String, Object> parameters, RequestCallBack<String> callBack) {

        if (callBack != null) {
            callBack.onStart();
        }
        double random = Math.random();
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        parameters.put("r", String.valueOf(random));

        String requestUrl = url + "?" + StringUtils.formatUrl(parameters);

        printLog(requestUrl, null);

        Request request = new Request.Builder().url(requestUrl).tag(tag).get().build();
        execute(callBack, request);
    }


    @Override
    public void doPost(String url, Map<String, Object> parameters, RequestCallBack<String> callBack) {
        if (callBack != null) {
            callBack.onStart();
        }

        FormBody.Builder builder = new FormBody.Builder();

        addParams(url, parameters, builder);
        RequestBody body = builder.build();

        Request request = new Request.Builder().url(url).post(body)
                .tag(tag).build();
        execute(callBack, request);

    }

    public void doPost(String url, String json, final RequestCallBack<String> callBack) {
        if (callBack != null) {
            callBack.onStart();
        }
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).tag(tag).build();
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .socketFactory(null)
                .build();
        try {
            mOkHttpClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, final IOException e) {
                    sendFailResultCallback("", e, callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response == null || !response.isSuccessful()) {
                        sendFailResultCallback(response, "服务器繁忙,请稍后重试!", new Exception(""), callBack);
                        return;
                    }
                    sendSuccessResultCallback(response, response.body().string(), callBack);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        execute(callBack, request);
    }


    @Override
    public void doUpload(String url, Map<String, Object> parameters, Map<String, File> files, final RequestCallBack<String> callBack) {
        if (callBack != null) {
            callBack.onStart();
        }


        if (!parameters.containsKey("fileKey")) {
            if (callBack != null) {
                callBack.onFailure("必须指定上传的key", new IllegalArgumentException(""));
            }
            return;
        }


        MultipartBody.Builder builder = new MultipartBody.Builder();
        addParams(url, parameters, files, builder);

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(new ProgressRequestBody(requestBody, callBack))
                .tag(tag)
                .build();


        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailResultCallback("上传文件失败,请稍后重试!", new Exception(""), callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response == null || !response.isSuccessful()) {
                    sendFailResultCallback(response, "服务繁忙,请稍后重试!", new Exception(""), callBack);
                    return;
                }

                sendSuccessResultCallback(response, response.body().string(), callBack);
            }
        });

    }


    @Override
    public void doDownLoad(String url, Map<String, Object> parameters, final RequestCallBack<String> callBack) {
        if (callBack != null) {
            callBack.onStart();
        }
        if (parameters == null) {
            parameters = new HashMap<>();
        }

        if (!parameters.containsKey("fileName")) {
            if (callBack != null) {
                callBack.onFailure("必须指定下载的文件名称", new IllegalArgumentException(""));
            }
            LogUtils.e("必须指定下载的文件名称");
            return;
        }
        String md5 = "";
        if (parameters.containsKey("md5")) {
            md5 = (String) parameters.get("md5");
            if (md5.equals("null") || md5.trim().equals("")){
                md5 = "";
                LogUtils.e("没有指定文件的md5，下载完成之后不会进行md5加密校验");
            }
        }else {
            LogUtils.e("没有指定文件的md5，下载完成之后不会进行md5加密校验");
        }

        final String fileMd5 = md5;

        final String destFileName = StringUtils.formatValue(parameters, "fileName");

        //clear 多余的参数
        parameters.remove("fileName");


        String downLoadUrl = url + "?" + StringUtils.formatUrl(parameters);

        Request request = new Request.Builder()
                .url(downLoadUrl)
                .tag(tag)
                .build();


        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailResultCallback("下载失败,请重试", e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response == null || !response.isSuccessful()) {
                    sendFailResultCallback(response, "服务器繁忙,请稍后重试!", new Exception(""), callBack);
                    return;
                }

                saveFile(response, destFileName, callBack, fileMd5);
            }
        });

    }

    /**
     * 异步发送请求
     *
     * @param callBack 回调接口
     * @param request  请求
     */

    private void execute(final RequestCallBack<String> callBack, Request request) {
        try {
            mOkHttpClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, final IOException e) {
                    sendFailResultCallback("", e, callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response == null || !response.isSuccessful()) {
                        sendFailResultCallback(response, "服务器繁忙,请稍后重试!", new Exception(""), callBack);
                        return;
                    }
                    sendSuccessResultCallback(response, response.body().string(), callBack);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 主线程请求失败回调
     *
     * @param error    返回响应失败原因
     * @param e        异常
     * @param callBack 回调接口
     */
    public void sendFailResultCallback(final String error, final Exception e, final RequestCallBack<String> callBack) {
        sendFailResultCallback(null, error, e, callBack);
    }

    public void sendFailResultCallback(Response response, final String error, final Exception e, final RequestCallBack<String> callBack) {
        if (callBack == null) return;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(error)) {
                    callBack.onFailure("网络请求失败,请稍后重试!", new Exception("Unexpected code " + e));
                } else {
                    callBack.onFailure(error, new Exception("Unexpected code " + e));
                }
            }
        });

        try {
            if (response == null) {
                LogUtils.i(e.toString());
            } else {
                LogUtils.i(response.body().string());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            LogUtils.i(response.message());
        }
    }


    /**
     * 主线程请求成功回调
     *
     * @param response 返回响应
     * @param result   请求字符串结果
     * @param callBack 回调接口
     */
    public void sendSuccessResultCallback(final Response response, final String result, final RequestCallBack<String> callBack) {
        if (callBack == null) return;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(response.body().charStream());
                callBack.onSuccess(result);

            }
        });

        LogUtils.i(result);
    }


    /**
     * 保存文件到指定目录
     *
     * @param response     请求返回响应
     * @param destFileName 存入的文件名称
     * @param callBack     进度回调
     * @return 保存的文件
     * @throws IOException
     */
    public void saveFile(final Response response, String destFileName, final RequestCallBack<String> callBack, final String fileMd5) throws IOException {
        String destFileDir = SystemConfig.getSystemFileDir();
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        File file = null;
        long total = 0;
        try {
            is = response.body().byteStream();
            total = response.body().contentLength();
            long sum = 0;

            LogUtils.e("文件总大小:" + total);

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                boolean createSuccess = dir.mkdirs();
                LogUtils.i("文件夹不存在,创建" + (createSuccess ? "成功" : "失败"));
            }
            file = new File(dir, destFileName);

            if (file.exists()) {
                boolean deleteSuccess = file.delete();
                LogUtils.i("文件已经存在,删除" + (deleteSuccess ? "成功" : "失败"));
            }

            fos = new FileOutputStream(file);
            final long finalTotal = total;
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onProgress(finalSum, finalTotal, finalTotal == finalSum);
                    }
                });
            }
            fos.flush();

        } catch (Exception e) {
            if (file != null) {
                boolean deleteSuccess = file.delete();
                LogUtils.i("文件下载失败,删除" + (deleteSuccess ? "成功" : "失败"));
            }
        } finally {
            try {
                if (is != null) is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (checkFileIsDownloadSuccess(fileMd5, file, total)) {
                sendSuccessResultCallback(response, file.getAbsolutePath(), callBack);
            } else {
                if (file != null) {
                    boolean deleteSuccess = file.delete();
                    LogUtils.i("文件下载失败,删除" + (deleteSuccess ? "成功" : "失败"));
                }
                sendFailResultCallback(response, "下载失败!", new Exception(""), callBack);
            }
        }
    }

    private boolean checkFileIsDownloadSuccess(String fileMd5, File file, long total) throws IOException {
        return (file != null && file.exists()) && (total == file.length()) && (TextUtils.isEmpty(fileMd5) || (!TextUtils.isEmpty(fileMd5) && MD5.checkPassword(MD5.getFileMD5(file), fileMd5)));
    }

    /**
     * 打印日志
     *
     * @param url        请求url
     * @param parameters 请求的参数
     */
    private void printLog(String url, Map<String, Object> parameters) {
        if (parameters == null) {
            LogUtils.i(url);
        } else {
            String requestUrl = url + "?" + StringUtils.formatUrl(parameters);
            LogUtils.i(requestUrl);
        }
    }

    /**
     * 获得文件上传的类型
     *
     * @param path 文件的路径
     * @return 类型
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    /**
     * 文件上传参数添加，必须指定一个 fileKey 用来指定什么类型的文件上传，由服务器指定
     *
     * @param url     上传的url
     * @param params  上传的别的参数
     * @param files   上传的文件
     * @param builder 上传参数构造
     */
    private void addParams(String url, Map<String, Object> params, Map<String, File> files, MultipartBody.Builder builder) {

        double random = Math.random();
        if (params == null || params.isEmpty()) {
            params = new HashMap<>();
        }

        params.put("r", String.valueOf(random));
        builder.addFormDataPart("r", String.valueOf(random));

        printLog(url, params);//打印请求url

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            builder.addFormDataPart(entry.getKey(), String.valueOf(entry.getValue()));
        }

        for (Map.Entry<String, File> entry : files.entrySet()) {
            File file = entry.getValue();
            if (file.exists()) {
                builder.addFormDataPart(entry.getKey(), file.getName(), RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file));
            }
        }


    }


    /**
     * 请求添加参数
     *
     * @param url     请求的url
     * @param params  请求的参数
     * @param builder 请求表单构造
     */
    private void addParams(String url, Map<String, Object> params, FormBody.Builder builder) {
//        double random = Math.random();

        if (params == null || params.isEmpty()) {
            params = new HashMap<>();
        }

//        params.put("r", String.valueOf(random));

        printLog(url, params);//打印请求url

        for (String key : params.keySet()) {
            builder.add(key, String.valueOf(params.get(key)));
        }


    }

    /**
     * 设置请求操作tag
     *
     * @param tag 请求tag，比如在一个activity里面 设置一个tag,当activity退出的时候，取消请求操作
     */
    public DefaultOkHttpIml setTag(Object tag) {
        this.tag = tag;
        return this;
    }


    /**
     * 取消请求操作
     *
     * @param tag 取消请求的tag，一般都是指定activity
     */
    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }


    /**
     * gzip压缩;
     * gzip way to compress data;
     *
     * @param val 原始数据
     * @return 压缩后的bytes数据
     * @throws IOException 错误
     */
    private static byte[] gzip(byte[] val) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(val.length);
        GZIPOutputStream gos = null;
        try {
            gos = new GZIPOutputStream(bos);
            gos.write(val, 0, val.length);
            gos.finish();
            val = bos.toByteArray();
            bos.flush();
        } finally {
            if (gos != null) {
                //gos已经finish，不做任何处理
            }
            if (bos != null) {
                bos.close();
            }
        }
        return val;
    }


}
