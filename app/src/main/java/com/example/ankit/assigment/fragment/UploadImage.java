package com.example.ankit.assigment.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import id.zelory.compressor.Compressor;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankit.assigment.BuildConfig;
import com.example.ankit.assigment.R;
import com.example.ankit.assigment.framework.ImageViewModel;
import com.example.ankit.assigment.model.ImageUploadInfo;
import com.example.ankit.assigment.utilities.AppUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.common.util.IOUtils.copyStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UploadImage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UploadImage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadImage extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static final int PERMISSION_ALL = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private OnFragmentInteractionListener mListener;
    private LinearLayout llUploadImage;
    private LinearLayout llUploadCamra;
    private ImageView imgCamera;
    private TextView tapImage;
    private LinearLayout llUploadGallery;
    private ImageView imgGallery ;
    private ImageViewModel imageViewModel;
    private ContentLoadingProgressBar progressPaginationLoading ;
    String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};
    private int REQUEST_PICK_FILE = 101;
    private int REQUEST_CODE_TAKE_PICTURE = 102;
    private int REQUEST_CODE_GALLERY = 103;
    private String imgName;
    private File mFileTemp;
    private String mCurrentPhotoPath;
    private String imgUrl;
    private String path;
    private Uri resultUri;
    private Uri photoURI;
    private TextView txtView;
    boolean flagUi = false ;


    public UploadImage() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UploadImage newInstance() {
        UploadImage fragment = new UploadImage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_iamge_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
    }

    private void findViews(View view) {
        progressPaginationLoading = (ContentLoadingProgressBar) view.findViewById(R.id.progressPaginationLoading);
        llUploadImage = (LinearLayout) view.findViewById(R.id.llUploadImage);
        llUploadCamra = (LinearLayout) view.findViewById(R.id.llUploadCamra);
        imgCamera = (ImageView) view.findViewById(R.id.imgCamera);
        tapImage = (TextView) view.findViewById(R.id.tapImage);
        txtView = (TextView) view.findViewById(R.id.txtView);
        llUploadGallery = (LinearLayout) view.findViewById(R.id.llUploadGallery);
        imgGallery = (ImageView) view.findViewById(R.id.imgGallery);
        llUploadCamra.setOnClickListener(this);
        llUploadGallery.setOnClickListener(this);

}

    private void uploadImageToFireBase() {
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);

        imageViewModel.uploadImageToFireBase(progressPaginationLoading,context,resultUri, imgUrl, imgName).observe(this, new Observer<ImageUploadInfo>() {

            @Override
            public void onChanged(ImageUploadInfo imageUploadInfo) {
                txtView.setText("Upload Image");
                setData(imageUploadInfo);
            }
        });
        imageViewModel.getToastObserver().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void setData(ImageUploadInfo response) {
        if(response !=null) {
            Toast.makeText(context, response.getImageName()+" uploaded", Toast.LENGTH_SHORT).show();
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }





    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.llUploadGallery :
                uploadImage();
                break;
            case R.id.llUploadCamra :
                uploadImage();
                break;
        }
    }

    private void uploadImage() {
        if (!hasPermissions(context, PERMISSIONS)) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else {
            uploadImageServer();
        }
    }



    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_ALL:
                // If request is cancelled, the result arrays are empty.
                uploadImageServer();

                }
    }

    private void uploadImageServer() {
            final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                    "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    context);
            builder.setTitle("Add Document!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {

                        dispatchTakePictureIntent();

                    } else if (items[item].equals("Choose from Gallery")) {
                        flagUi = true ;
                        openGallery();

                    }  else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();

        }


    private File dispatchTakePictureIntent() {
        File photoFile = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go

            photoFile = createImageFile();
            imgUrl = photoFile.getPath();


            // Continue only if the File was successfully created
            if (photoFile != null) {
                 photoURI = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                try {
                    startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PICTURE);
                } catch (Exception e) {
                    Toast.makeText(context, "Please provide require permission", Toast.LENGTH_LONG).show();
                }
            }
        }
        return photoFile;
    }

    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("ddMMMyyyy_HHmms").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();

        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        mFileTemp = image;
        return image;

    }

    private void openGallery() {

        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
        } catch (Exception e) {
            Toast.makeText(context, "Please provide require permission", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            com.theartofdev.edmodo.cropper.CropImage.ActivityResult result =
                    com.theartofdev.edmodo.cropper.CropImage.getActivityResult(resultData);
             resultUri = null;
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
            } else if (resultCode == com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
            try {
                path = resultUri.getPath();
                if (path == null) {
                    return;
                }
                imgUrl = path;
            } catch (Exception e) {

            }
            goToUploadDoc(mFileTemp);



        } else if (requestCode == REQUEST_CODE_TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                try {
                    mFileTemp = new Compressor(context).compressToFile(mFileTemp);
                    imgName = removeExtension(getImageName(photoURI));
                    //  Utility.compressImage(mFileTemp.getAbsolutePath(),mFileTemp.getAbsolutePath());
                    if (AppUtil.getFileSizeInMB(mFileTemp) < 10) {
                        croptImage();
                    } else {
                        Toast.makeText(context,"Please ulpoad lower than 10 mb image",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }

            }

        } else if (requestCode == REQUEST_CODE_GALLERY &&  resultCode == RESULT_OK ) {
            try {
                Uri uri = resultData.getData();
                imgName = removeExtension(getImageName(uri));
                String timeStamp = new SimpleDateFormat("ddMMMyyyy_HHmmss").format(new Date());
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    File imagesFolder = new File(Environment.getExternalStorageDirectory(), "assigment");
                    imagesFolder.mkdirs();
                    mFileTemp = new File(imagesFolder, imgName != null ? getImageName(uri) : "QR_" + timeStamp + ".png");
                } else {
                    mFileTemp = new File(context.getFilesDir(), imgName != null ? getImageName(uri) : "QR_" + timeStamp + ".png");
                }

                InputStream inputStream = context.getContentResolver().openInputStream(resultData.getData());
                FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                copyStream(inputStream, fileOutputStream);
                fileOutputStream.close();
                inputStream.close();


                //   Utility.compressImage(mFileTemp.getAbsolutePath(),mFileTemp.getAbsolutePath());
                if (AppUtil.getFileSizeInMB(mFileTemp) < 10) {
                    croptImage();
                } else {
                    Toast.makeText(context, "Please provide require permission", Toast.LENGTH_LONG).show();

                }


                imgUrl = mFileTemp.getPath();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private void croptImage() {
        com.theartofdev.edmodo.cropper.CropImage.activity(Uri.fromFile(mFileTemp))
                .setInitialCropWindowPaddingRatio(0).start(context,this);
    }

    private void goToUploadDoc(File mFileTemp) {
        progressPaginationLoading.show();
        if (AppUtil.isOnline(context)) {
            uploadImageToFireBase();
        } else {
            Toast.makeText(context, "Please Connect to internet", Toast.LENGTH_SHORT).show();
        }

    }
    private String removeExtension(String imageName) {
        if (imageName != null && !imageName.isEmpty()) {
            int pos = imageName.lastIndexOf(".");
            if (pos > 0 && pos < (imageName.length() - 1)) { // If '.' is not the first or last character.
                imageName = imageName.substring(0, pos);
            }
        }
        return imageName;
    }
    private String getImageName(Uri uri) {
        String displayName = null;

        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.
        Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                // If the size is unknown, the value stored is null.  But since an
                // int can't be null in Java, the behavior is implementation-specific,
                // which is just a fancy term for "unpredictable".  So as
                // a rule, check if it's null before assigning to an int.  This will
                // happen often:  The storage API allows for remote files, whose
                // size might not be locally known.
                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString()
                    // will do the conversion automatically.
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
            }
        } finally {
            cursor.close();
        }
        return displayName;
    }



    /**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */
public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
}
}
