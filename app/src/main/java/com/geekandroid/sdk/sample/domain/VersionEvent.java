package com.geekandroid.sdk.sample.domain;

import android.webkit.DownloadListener;

import com.geekandroid.sdk.sample.update.domain.DownloadInfo;
import com.geekandroid.sdk.sample.update.domain.VersionBean;

/**
 * Created by lenovo on 2016/4/27.
 */
public class VersionEvent {
    private VersionBean mVersionBean;

    private DownloadInfo mDownloadInfo;

    public int progress;

    public long contentLength;

    public int getProgress() {
        return progress;
    }

    public boolean isFinish;

    public String fileUrl;

    public boolean isFinish() {
        return isFinish;
    }

    public void setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public VersionBean getVersionBean() {
        return mVersionBean;
    }

    public void setVersionBean(VersionBean versionBean) {
        mVersionBean = versionBean;
    }

    public DownloadInfo getDownloadInfo() {
        return mDownloadInfo;
    }

    public void setDownloadInfo(DownloadInfo downloadInfo) {
        mDownloadInfo = downloadInfo;
    }
}
