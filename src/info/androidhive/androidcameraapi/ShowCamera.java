package info.androidhive.androidcameraapi;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {

   public SurfaceHolder holdMe;
   public Camera theCamera;
   //public Camera.Size bestSize;

   public ShowCamera(Context context,Camera camera) {
      super(context);
      theCamera = camera;
      Camera.Parameters mParameters = theCamera.getParameters();
      
      mParameters.setPreviewSize(2048, 1536);
      mParameters.setPictureSize(2048, 1536);
      theCamera.setParameters(mParameters);
      theCamera.setDisplayOrientation(0);
      //theCamera.setPicture = 2048;
      holdMe = getHolder();
      holdMe.addCallback(this);
   }

   private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
       final double ASPECT_TOLERANCE = 0.1;
       double targetRatio = (double) w / h;
       if (sizes == null) return null;

       Camera.Size optimalSize = null;
       double minDiff = Double.MAX_VALUE;

       int targetHeight = h;

       // Try to find an size match aspect ratio and size
       for (Camera.Size size : sizes) {
           double ratio = (double) size.width / size.height;
           if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
           if (Math.abs(size.height - targetHeight) < minDiff) {
               optimalSize = size;
               minDiff = Math.abs(size.height - targetHeight);
           }
       }

       // Cannot find the one match the aspect ratio, ignore the requirement
       if (optimalSize == null) {
           minDiff = Double.MAX_VALUE;
           for (Camera.Size size : sizes) {
               if (Math.abs(size.height - targetHeight) < minDiff) {
                   optimalSize = size;
                   minDiff = Math.abs(size.height - targetHeight);
               }
           }
       }
       return optimalSize;
   }
   
   @Override
   public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
   }

   @Override
   public void surfaceCreated(SurfaceHolder holder) {
      try   {
         theCamera.setPreviewDisplay(holder);
         theCamera.startPreview(); 
      } catch (IOException e) {
      }
   }

   @Override
   public void surfaceDestroyed(SurfaceHolder arg0) {
   }

}