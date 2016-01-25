package com.example.demoproject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int REQUEST_CROP_ICON = 50;
	private Uri fileUri;
	private Uri croppedFileUri;
	public static final int MEDIA_TYPE_IMAGE = 1;
	Button button1, button2;
	boolean isPicTaken = false;

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "MyCameraApp");
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"IMG_"+ timeStamp + ".jpg");
		} else {
			return null;
		}
		return mediaFile;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});

		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isPicTaken == false) {
					Toast.makeText(MainActivity.this, "Looks like you haven't clicked any photo yet",Toast.LENGTH_LONG).show();
					isPicTaken = true;
				}else{
					Intent intent = new Intent(MainActivity.this, DetectActivity.class);
//					Toast.makeText(MainActivity.this, croppedFileUri.toString(),Toast.LENGTH_LONG).show();
					System.out.println("first "+croppedFileUri.toString());
					intent.putExtra("imageUri", croppedFileUri.toString());
					startActivity(intent);
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void cropPic(){
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(fileUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 400);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("scale", true);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		croppedFileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
		intent.putExtra("output", croppedFileUri);
		startActivityForResult(intent, REQUEST_CROP_ICON);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("Request and result code is "+requestCode+" "+resultCode+" "+data);	
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Image saved to:\n" +
						fileUri, Toast.LENGTH_SHORT).show();
				isPicTaken = true;
				cropPic();
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Photo could not be clicked", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Photo could not be clicked", Toast.LENGTH_LONG).show();
			}
		}else if (requestCode == REQUEST_CROP_ICON) {
			if (resultCode == RESULT_OK) {
			} else if (resultCode == RESULT_CANCELED) {
				croppedFileUri = fileUri;
			} else {
				croppedFileUri = fileUri;
			}
		}

	}
}
