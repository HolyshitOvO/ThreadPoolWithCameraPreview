package au.com.alphamu.camerapreviewcaptureimage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CameraPermissionHelper extends Fragment {
    public static final String TAG = "camera_permission";

    private static final int REQUEST_CAMERA = 101;
    private static final int REQUEST_CHECK_SETTINGS = 102;

    private CameraPermissionCallback mCallback;
    private static boolean sPermissionDenied;


    public static CameraPermissionHelper newInstance() {
        return new CameraPermissionHelper();
    }

    public CameraPermissionHelper() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CameraPermissionCallback) {
            mCallback = (CameraPermissionCallback) activity;
            checkCameraPermissions();
        } else {
            throw new IllegalArgumentException(
                    "activity must extend BaseActivity and " +
                            "implement CameraPermissionHelper.CameraPermissionCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @SuppressLint("NewApi")
    public void checkCameraPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mCallback.onCameraPermissionResult(true);
        } else {
            if (!sPermissionDenied) {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // All required changes were successfully made
                    if (mCallback != null) {
                        mCallback.onCameraPermissionResult(true);
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    // The user was asked to change settings, but chose not to
                default:
                    if (mCallback != null) {
                        mCallback.onCameraPermissionResult(false);
                    }
                    break;
            }
        }
    }

    public void setPermissionDenied(boolean permissionDenied) {
        sPermissionDenied = permissionDenied;
    }

    public static boolean isPermissionDenied() {
        return sPermissionDenied;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCallback.onCameraPermissionResult(true);
            } else {
                Log.i("BaseActivity", "CAMERA permission was NOT granted.");
                mCallback.onCameraPermissionResult(false);
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static CameraPermissionHelper attach(FragmentManager fragmentManager) {
        CameraPermissionHelper cameraHelper = (CameraPermissionHelper) fragmentManager.findFragmentByTag(TAG);
        if (cameraHelper == null) {
            cameraHelper = CameraPermissionHelper.newInstance();
            fragmentManager.beginTransaction().add(cameraHelper, TAG).commit();
        }
        return cameraHelper;
    }

    public interface CameraPermissionCallback {
        void onCameraPermissionResult(boolean successful);
    }

}
