package info.androidhive.androidcameraapi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.provider.SyncStateContract.Constants;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

//import com.facebook.android.AsyncFacebookRunner;
//import com.facebook.android.AsyncFacebookRunner.RequestListener;
//import com.facebook.android.Facebook;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.CountDownTimer;
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
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
	private FrameLayout preview;
	private ImageView imgPreview;
	private ImageView imgPreview3;
	private Button btnCapturePicture;
	private Button btnPreviewPicture;
	private Button btnConfirm;
	private Button btnCancel;
	private Button btnSave;
	private Button btnFCancel;
	private Bitmap GBitmap;
	private Bitmap LBitmap;
	private Bitmap CBitmap;
	private Button sun;
	private int side = 0;
	private int openornot = 0;
	private int captured  = 0;
	private static int RESULT_LOAD_IMAGE = 1;
	private GPSTracker gps;
	
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
	
	private PictureCallback CAPJPEG = new PictureCallback() {

	      @Override
	      public void onPictureTaken(byte[] data, Camera camera) {
	      BitmapFactory.Options options = new BitmapFactory.Options();
		  options.inSampleSize = 1;
	      Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data.length, options);
	      GBitmap = bitmap;
			btnCapturePicture.setVisibility(View.GONE);
			btnConfirm.setVisibility(View.VISIBLE);
			btnCancel.setBackgroundResource(R.drawable.cancel);
			btnCancel.setVisibility(View.VISIBLE);
	      
	   }
	};
	
	   public void snapIt(View view){
		   Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
			    @Override
			    public void onAutoFocus(boolean success, Camera camera) {
			        camera.takePicture(null, null, CAPJPEG);
			    }
			};   
		        
		   cameraObject.autoFocus(mAutoFocusCallback);
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
	    //imgPreview.setDrawingCacheEnabled(true);
		imgPreview3 = (ImageView) findViewById(R.id.imgPreview3);
		btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
		btnPreviewPicture = (Button) findViewById(R.id.btnPreviewPicture);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnSave = (Button) findViewById(R.id.btnSave);
		sun = (Button) findViewById(R.id.sun);
		btnFCancel = (Button) findViewById(R.id.btnFCancel);
		gps = new GPSTracker(MainActivity.this);
		cameraObject = isCameraAvailiable();
	    showCamera = new ShowCamera(this, cameraObject);
	    preview = (FrameLayout) findViewById(R.id.camera_preview4);
	    preview.addView(showCamera);
		Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_around_center_point);
		//animation.setRepeatCount(Animation.INFINITE);
		sun.startAnimation(animation);
	    //showCamera.setDrawingCacheEnabled(true);
	    //uiHelper = new UiLifecycleHelper(this, null);
	    //uiHelper.onCreate(savedInstanceState);
		
		/*/
		 * Capture image button click event
		/*/
		btnCapturePicture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// capture picture
				FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
				FrameLayout preview2 = (FrameLayout) findViewById(R.id.camera_preview2);
				btnPreviewPicture = (Button) findViewById(R.id.btnPreviewPicture);
				btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
				btnConfirm = (Button) findViewById(R.id.btnConfirm);
				btnCancel  = (Button) findViewById(R.id.btnCancel);
				showCamera.theCamera.startPreview();
				//captureImage();
				if (openornot == 1) {
					if (side == 0) {
						snapIt(preview);
						captured = 1;
					} else if (side == 1) {
						snapIt(preview2);
						captured = 1;
					}
				
					
	                // check if GPS enabled     
	                if(gps.canGetLocation()){
	                     
	                    double latitude = gps.getLatitude();
	                    double longitude = gps.getLongitude();
	                     
	                    // \n is for new line
	                    //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
	                }else{
	                    // can't get location
	                    // GPS or Network is not enabled
	                    // Ask user to enable GPS/network in settings
	                    gps.showSettingsAlert();
	                }
				}
				if (openornot == 0) {
					preview.setVisibility(View.VISIBLE);
					//LinearLayout layer1 = (LinearLayout) findViewById(R.id.layer1);
					RelativeLayout left1 = (RelativeLayout) findViewById(R.id.left1);
					left1.setAlpha(0);
					btnCapturePicture.setBackgroundResource(R.drawable.shoot);
					//Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_around_center_point);
					//btnCapturePicture.startAnimation(animation);
					preview.setVisibility(View.GONE);
					imgPreview.setVisibility(View.GONE);
					btnCancel.setVisibility(View.VISIBLE);
					openornot = 1;
				}
				btnPreviewPicture.setVisibility(View.GONE);
			}
		});
		
		btnPreviewPicture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// capture picture
				btnCancel.setVisibility(View.VISIBLE);
				imgPreview = (ImageView) findViewById(R.id.imgPreview);
				imgPreview.setVisibility(View.VISIBLE);
				imgPreview.setBackgroundColor(0);
				showCamera.theCamera.startPreview();
				side = 1;
				
				Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
				btnCapturePicture.setBackgroundResource(R.drawable.shoot);
                openornot = 1;
        	    btnPreviewPicture = (Button) findViewById(R.id.btnPreviewPicture);
        	    btnPreviewPicture.setVisibility(View.GONE); 
			}
		});
		
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (captured == 1) {
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
					//btnCancel.setVisibility(View.GONE);
					btnCancel.setBackgroundResource(R.drawable.back);
					captured = 0;
				} else {
					if (side == 0) {
						preview.setVisibility(View.GONE);
						//LinearLayout layer1 = (LinearLayout) findViewById(R.id.layer1);
						RelativeLayout left1 = (RelativeLayout) findViewById(R.id.left1);
						left1.setAlpha(1);
						btnCapturePicture.setVisibility(View.VISIBLE);
						btnPreviewPicture.setVisibility(View.VISIBLE);
						btnConfirm.setVisibility(View.GONE);
						//btnCancel.setVisibility(View.VISIBLE);
						preview.setVisibility(View.VISIBLE);
						imgPreview.setVisibility(View.GONE);
						btnCancel.setVisibility(View.GONE);
						captured = 0;
						openornot = 0;
					} else {
						//preview.setVisibility(View.GONE);
						//LinearLayout layer1 = (LinearLayout) findViewById(R.id.layer1);
						btnCapturePicture.setVisibility(View.VISIBLE);
						btnPreviewPicture.setVisibility(View.VISIBLE);
						btnConfirm.setVisibility(View.GONE);
						//btnCancel.setVisibility(View.VISIBLE);
						//preview.setVisibility(View.VISIBLE);
						imgPreview.setVisibility(View.GONE);
						btnCancel.setVisibility(View.GONE);
						openornot = 0;
						RelativeLayout right1 = (RelativeLayout) findViewById(R.id.right1);
						right1.setAlpha(1);
						//showCamera.theCamera.startPreview();
						captured = 0;
						side = 0;
						btnCapturePicture.clearAnimation();
					}
					btnCapturePicture.setBackgroundResource(R.drawable.take_pic);
				}
			}
		});
		
		btnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Return
				CharSequence text = "Saved!";
			    int duration = Toast.LENGTH_LONG;
			    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
			    toast.show();
			    
				btnCancel.setVisibility(View.GONE);
				
			    int width = GBitmap.getWidth();
			    int height = GBitmap.getHeight();
			    Bitmap resizedBitmap;
			    
			    if (side == 1) {
			    	 resizedBitmap = Bitmap.createBitmap(GBitmap, width/2, 0, 
		                      width/2, height, null, false);
			    } else {
			    	 resizedBitmap = Bitmap.createBitmap(GBitmap, 0, 0, 
		                      width/2, height, null, false);
			    }
			    if (side == 1) {
			    	CBitmap = combineImages(LBitmap, resizedBitmap);
			    } else {
			    	CBitmap = resizedBitmap;
			    }
			    saveImage();
			    showCamera.theCamera.stopPreview();
			    if (side == 0) {
					preview.setVisibility(View.GONE);
					RelativeLayout left1 = (RelativeLayout) findViewById(R.id.left1);
					left1.setAlpha(1);
					btnCapturePicture.setVisibility(View.VISIBLE);
					btnPreviewPicture.setVisibility(View.VISIBLE);
					btnConfirm.setVisibility(View.GONE);
					preview.setVisibility(View.VISIBLE);
					imgPreview.setVisibility(View.GONE);
					btnCancel.setVisibility(View.GONE);
					openornot = 0;
				} else {
					btnCapturePicture.setVisibility(View.VISIBLE);
					btnPreviewPicture.setVisibility(View.VISIBLE);
					btnConfirm.setVisibility(View.GONE);
					imgPreview.setVisibility(View.GONE);
					btnCancel.setVisibility(View.GONE);
					openornot = 0;
					RelativeLayout right1 = (RelativeLayout) findViewById(R.id.right1);
					right1.setAlpha(1);
					side = 0;
				}
			    captured = 0;
			    btnCapturePicture.setBackgroundResource(R.drawable.take_pic);
			    btnCancel.setBackgroundResource(R.drawable.back);
			    imgPreview.setImageResource(R.drawable.back_left_s);
			}
		});
		
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {                                                                                                                                                                                                                                                                                                                                                                                                                 
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
	
	public void saveImage() {
		long unixTime = System.currentTimeMillis() / 1000L;
		String imageName = Long.toString(unixTime);                                                                                                                                                                                                                                                 
		
	    Bitmap sourceBitmap = CBitmap;
		boolean imageSaved = false;
		
		if (sourceBitmap != null && !sourceBitmap.isRecycled()) {                                                                                                                                                                                                                 
			File storagePath = new File(Environment.getExternalStorageDirectory() + "/MySpecialLocation/Pictures/");                                                                                                                                                                 
			storagePath.mkdirs();                                                                                                                                                                                                                                                    
		                                                                                                                                                                                                                                                                          
			FileOutputStream out = null;                                                                                                                                                                                                                                             
			File imageFile = new File(storagePath, String.format("%s.jpg",imageName));                                                                                                                                                                                               
			try {                                                                                                                                                                                                                                                                   
				out = new FileOutputStream(imageFile);                                                                                                                                                                                                                              
				imageSaved = sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);                                                                                                                                                                                            
			} catch (Exception e) {                                                                                                                                                                                                                                                 
		        //Log.e(Constants.LOG_TAG, "Unable to write the image to gallery", e);                                                                                                                                                                                                
			} finally {                                                                                                                                                                                                                                                             
				if (out != null) {                                                                                                                                                                                                                                                  
					try {
						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}                                                                                                                                                                                                                                                    
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}                                                                                                                                                                                                                                                    
				}                                                                                                                                                                                                                                                                   
			}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
			galleryAddPic(imageFile.getAbsolutePath());                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
		}
	}
	
	public Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom 
	    Bitmap cs = null; 

	    int width, height = 0; 

	    if(c.getWidth() > s.getWidth()) { 
	      width = c.getWidth() + s.getWidth(); 
	      height = c.getHeight(); 
	    } else { 
	      width = s.getWidth() + s.getWidth(); 
	      height = c.getHeight(); 
	    } 

	    cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 

	    Canvas comboImage = new Canvas(cs); 

	    comboImage.drawBitmap(c, 0f, 0f, null); 
	    comboImage.drawBitmap(s, c.getWidth(), 0f, null); 

	    return cs; 
	  } 
	
	private void galleryAddPic(String mCurrentPhotoPath) {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
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
		//uiHelper.onSaveInstanceState(outState);

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
            LBitmap = BitmapFactory.decodeFile(picturePath);
            int w = LBitmap.getWidth();
        	int h = LBitmap.getHeight();
            if (LBitmap.getHeight() < LBitmap.getWidth()*3/2) {
            	Bitmap resizedBitmap = Bitmap.createBitmap(LBitmap, w/2-h/3, 0, 
	                      h*2/3, h, null, false);
            	LBitmap = resizedBitmap;
            }
            if (LBitmap.getHeight() > LBitmap.getWidth()*3/2) {
            	Bitmap resizedBitmap = Bitmap.createBitmap(LBitmap, 0, h/2-w*3/4, 
	                      w, w*3/2, null, false);
            	LBitmap = resizedBitmap;
            }
            imgPreview.setBackgroundColor(Color.rgb(0, 0, 0));
            imgPreview.setImageBitmap(LBitmap);
			Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_around_center_point_180);
			btnCapturePicture.startAnimation(animation);
			animation.setFillAfter(true);
            RelativeLayout right1 = (RelativeLayout) findViewById(R.id.right1);
			right1.setAlpha(0);
         
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
			//BitmapFactory.Options options = new BitmapFactory.Options();

			// downsizing image as it throws OutOfMemory Exception for larger
			// images
			//options.inSampleSize = 8;

			final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath());
					//options);

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