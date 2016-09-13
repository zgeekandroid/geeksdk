/*******************************************************************************
 *
 * Copyright (c) 2016 Mickael Gizthon . All rights reserved. Email:2013mzhou@gmail.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.geekandroid.sdk.update.domain;

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
