package com.example.demoproject;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class DetectActivity extends Activity{

	Button button;
	ImageView imageView;
	private Uri imageUri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detect_activity);
		imageUri = Uri.parse(getIntent().getExtras().getString("imageUri"));
//		Toast.makeText(this, imageUri.toString(),Toast.LENGTH_LONG).show();
		System.out.println("second "+imageUri.toString());
		imageView = (ImageView) findViewById(R.id.img_view);
		imageView.setImageURI(null);
		imageView.setImageURI(imageUri);
	}
}
