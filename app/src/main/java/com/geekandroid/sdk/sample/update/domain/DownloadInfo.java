package com.geekandroid.sdk.sample.update.domain;

/**
 * Created by lenovo on 2016/4/27.
 */
public class DownloadInfo {
    private String downLoadUrl;
    private String fileName;
    private String fileDir;
    boolean isDownLoad;

    public DownloadInfo(String downLoadUrl, String fileName, String fileDir, boolean isDownLoad) {
        this.downLoadUrl = downLoadUrl;
        this.fileName = fileName;
        this.fileDir = fileDir;
        this.isDownLoad = isDownLoad;
    }

    public boolean isDownLoad() {
        return isDownLoad;
    }

    public void setIsDownLoad(boolean isDownLoad) {
        this.isDownLoad = isDownLoad;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }
}
