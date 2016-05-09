###  Download new version app in two ways

## 1.showDialog

#### startDiaologService

	  UpdateService.startService(getActivity(), url, name, filePath);


	  url  : your download app url 
	  name : your app name
	  filePath : download filePath in your SDCARD
 

#### checkVersions from server and local in UpdateService
	  private void checkVersions() {
		...
		//update or not
		showUpdateDialog(){
		  // doDownLoad or not
				doDownLoad();
        }
        ...
	  }
		
#### doDownLoad
		
	  public void doDownLoad() {
		...
		//showProgressDialog
		 DialogUtils.showDownLoadDialog()
		...
         //downloadComplete install or not
		 DialogUtils.showInstallDialog();
	  }


##  2.Notification

####  startNotificationService
	
	    NotificationService.startService(getActivity(), url, name, filePath);
	 

#### checkVersions from server and local in UpdateService
	  private void checkVersions() {
		...
		//update or not
		showUpdateNotification(){
		  // doDownLoad or not
				doDownLoad();
        }
        ...
	  }
		
#### doDownLoad
		
	  public void doDownLoad() {
		...
		//showProgress on Notification
		sendNotification(String name, int progress)
		...
         //downloadComplete install or not
		  DialogUtils.showInstallDialog();
	  }