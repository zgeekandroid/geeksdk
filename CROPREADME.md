### 1.Include the library as local library project 
	 
	compile 'com.yalantis:ucrop:1.5.0' 

###  or include library ucrop.	
	 compile project(':ucrop')
	
	 include ':app', ':ucrop'
	
	

### 2.Add UCropActivity into your AndroidManifest.xml

	<activity
    android:name="com.yalantis.ucrop.UCropActivity"
    android:screenOrientation="portrait"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar"/>
### 3.The uCrop configuration is created using the builder pattern.

	UCrop.of(sourceUri, destinationUri)
    .withAspectRatio(16, 9)
    .withMaxResultSize(maxWidth, maxHeight)
    .start(context);

### 4.Override  onActivityResult  method and handle uCrop result.

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
        final Uri resultUri = UCrop.getOutput(data);
    } else if (resultCode == UCrop.RESULT_ERROR) {
        final Throwable cropError = UCrop.getError(data);
    }
}

### 5.HandleCropResult after you have got crop.
	 private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            ResultActivity.startWithUri(CropSampleActivity.this, resultUri);
        } else {
            Toast.makeText(CropSampleActivity.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

### 6.You can saveCroppedImager 	
	    private void saveCroppedImage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            Uri imageUri = getIntent().getData();
            if (imageUri != null && imageUri.getScheme().equals("file")) {
                try {
                    copyFileToDownloads(getIntent().getData());
                } catch (Exception e) {
                    Toast.makeText(ResultActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, imageUri.toString(), e);
                }
            } else {
                Toast.makeText(ResultActivity.this, getString(R.string.toast_unexpected_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

### or copyToFile

	    private void copyFileToDownloads(Uri croppedFileUri) throws Exception {
        String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(), croppedFileUri.getLastPathSegment());

        File saveFile = new File(downloadsDirectoryPath, filename);

        FileInputStream inStream = new FileInputStream(new File(croppedFileUri.getPath()));
        FileOutputStream outStream = new FileOutputStream(saveFile);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();

        showNotification(saveFile);
    }