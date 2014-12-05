package info.androidhive.androidcameraapi;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// Activity request codes
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;

	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
	//private static final int REQ_CODE_PICK_IMAGE = 0;

	private Uri fileUri; // file url to store image/video

	private ImageView imgPreview;
	private ImageView imgPreview3;
	private Button btnCapturePicture;
	private Button btnPreviewPicture;
	private Button btnConfirm;
	private Button btnCancel;
	private Button btnFCancel;
	private Bitmap GBitmap;
	private int side = 0;
	private int openornot = 0;
	
	private static int RESULT_LOAD_IMAGE = 1;
	
	private Camera cameraObject;
	private ShowCamera showCamera;
	//private ImageView pic;
	
	public static Camera isCameraAvailiable(){
	    Camera object = null;
	    try {
	       object = Camera.open(); 
	    }
	    catch (Exception e){
	    }
	    return object; 
	}
	
	private PictureCallback capturedIt = new PictureCallback() {

	      @Override
	      public void onPictureTaken(byte[] data, Camera camera) {

	      Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data .length);
	      GBitmap = bitmap;
	      
	      /*
	      if(bitmap==null){
	         Toast.makeText(getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
	      }
	      else
	      {
	         Toast.makeText(getApplicationContext(), "taken", Toast.LENGTH_SHORT).show();    	
	      }*/
	      
	      //cameraObject.release();
	      //imgPreview.setVisibility(View.VISIBLE);
	      //imgPreview.setImageBitmap(bitmap);
	      
	   }
	};
	
	   public void snapIt(View view){
		      cameraObject.takePicture(null, null, capturedIt);
	   }

	   @Override
	   public boolean onCreateOptionsMenu(Menu menu) {
		      getMenuInflater().inflate(R.menu.main, menu);
		      return true;
	   }
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		imgPreview = (ImageView) findViewById(R.id.imgPreview);
		imgPreview3 = (ImageView) findViewById(R.id.imgPreview3);
		btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
		btnPreviewPicture = (Button) findViewById(R.id.btnPreviewPicture);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnFCancel = (Button) findViewById(R.id.btnFCancel);
		
		cameraObject = isCameraAvailiable();
	    showCamera = new ShowCamera(this, cameraObject);
	    FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview4);
	    preview.addView(showCamera);
		
		/*
		 * Capture image button click event
		 */
		btnCapturePicture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// capture picture
				FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
				FrameLayout preview2 = (FrameLayout) findViewById(R.id.camera_preview2);
				btnPreviewPicture = (Button) findViewById(R.id.btnPreviewPicture);
				btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
				btnConfirm = (Button) findViewById(R.id.btnConfirm);
				btnCancel = (Button) findViewById(R.id.btnCancel);
				
				//captureImage();
				if (openornot == 1) {
					if (side == 0) {
						snapIt(preview);
					} else if (side == 1) {
						snapIt(preview2);
					}
					btnCapturePicture.setVisibility(View.GONE);
					btnConfirm.setVisibility(View.VISIBLE);
					btnCancel.setVisibility(View.VISIBLE);
				}
				if (openornot == 0) {
					preview.setVisibility(View.VISIBLE);
					LinearLayout layer1 = (LinearLayout) findViewById(R.id.layer1);
					RelativeLayout left1 = (RelativeLayout) findViewById(R.id.left1);
					left1.setAlpha(0);
					preview.setVisibility(View.GONE);
					imgPreview.setVisibility(View.GONE);
					openornot = 1;
				}
				
				btnPreviewPicture.setVisibility(View.GONE);
				CharSequence text = Integer.toString(showCamera.bestSize.width);
			    int duration = Toast.LENGTH_LONG;
			    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
			    toast.show();
			}
		});
		
		btnPreviewPicture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// capture picture
				
				imgPreview = (ImageView) findViewById(R.id.imgPreview);
				imgPreview.setVisibility(View.VISIBLE);
				
				FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
				preview.setVisibility(View.INVISIBLE);
				
				preview.removeViewAt(0);
				side = 1;
				
				Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                
                FrameLayout preview2 = (FrameLayout) findViewById(R.id.camera_preview2);
        	    preview2.addView(showCamera);
        	    preview2.setVisibility(View.VISIBLE);
        	    openornot = 1;
        	    btnPreviewPicture = (Button) findViewById(R.id.btnPreviewPicture);
        	    btnPreviewPicture.setVisibility(View.GONE); 
			}
		});
		
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Return
				FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        	    //preview.addView(showCamera);
        	    preview.setVisibility(View.VISIBLE);
        	    
        	    showCamera.theCamera.startPreview();
        	    
        	    //btnPreviewPicture = (Button) findViewById(R.id.btnPreviewPicture);
				btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
				btnConfirm = (Button) findViewById(R.id.btnConfirm);
				btnCancel = (Button) findViewById(R.id.btnCancel);
				
				btnCapturePicture.setVisibility(View.VISIBLE);
				btnConfirm.setVisibility(View.GONE);
				btnCancel.setVisibility(View.GONE);
        	    
			}
		});
		
		btnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Return
				LinearLayout layout = (LinearLayout) findViewById(R.id.layer3);
        	    //preview.addView(showCamera);
        	    layout.setVisibility(View.VISIBLE);
        	    imgPreview3.setImageBitmap(GBitmap);
        	    
			}
		});
		
		// Checking camera availability
		if (!isDeviceSupportCamera()) {
			Toast.makeText(getApplicationContext(),
					"Sorry! Your device doesn't support camera",
					Toast.LENGTH_LONG).show();
			// will close the app if the device does't have camera
			finish();
		}
	}

	/**
	 * Checking device has camera hardware or not
	 * */
	private boolean isDeviceSupportCamera() {
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}


	/*
	 * Capturing Camera Image will lauch camera app requrest image capture
	 */
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	/*
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on scren orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

	/**
	 * Receiving activity result method will be called after closing the camera
	 * */
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if the result is capturing Image
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Toast.makeText(getApplicationContext(),
					"To Load Img", Toast.LENGTH_SHORT)
					.show();
			Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            
            imgPreview.setVisibility(View.VISIBLE);

			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// downsizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;
             
            //ImageView imageView = (ImageView) findViewById(R.id.imgPreview);
            imgPreview.setImageBitmap(BitmapFactory.decodeFile(picturePath, options));
         
        }
		
		else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// successfully captured the image
				// display it in image view
				previewCapturedImage();
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/*
	 * Display image from a path to ImageView
	 */
	private void previewCapturedImage() {
		try {
			// hide video preview
			//videoPreview.setVisibility(View.GONE);

			imgPreview.setVisibility(View.VISIBLE);

			// bimatp factory
			BitmapFactory.Options options = new BitmapFactory.Options();

			// downsizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
					options);

			imgPreview.setImageBitmap(bitmap);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}


	/**
	 * ------------ Helper Methods ---------------------- 
	 * */
	/*
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/*
	 * returning image / video
	 */
	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}
}
