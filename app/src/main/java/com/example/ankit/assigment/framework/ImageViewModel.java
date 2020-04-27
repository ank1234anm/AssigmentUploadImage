package com.example.ankit.assigment.framework;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.view.View;


import com.example.ankit.assigment.model.ImageUploadInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ImageViewModel extends AndroidViewModel {
    private MutableLiveData<ImageUploadInfo> responseMutableLiveData;
    private MutableLiveData<List<ImageUploadInfo>> responseUploadedImage;
    private LiveData<List<ImageUploadInfo>> imageInfoList ;
    private MutableLiveData<String> networkResponseErrorMessage = new MutableLiveData();
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    ContentLoadingProgressBar progressPaginationLoading ;
    List<ImageUploadInfo> infoList = null ;
    Context context ;
    // Folder path for Firebase Storage.
    String Storage_Path = "All_Image_Uploads/";

    // Root Database Name for Firebase Database.
    String Database_Path = "All_Image_Uploads_Database";

    public ImageViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<ImageUploadInfo> uploadImageToFireBase(ContentLoadingProgressBar progressPaginationLoading ,Context context, Uri uri, String path, String imageName) {
        if (responseMutableLiveData == null) {
            responseMutableLiveData = new MutableLiveData<>();
            this.context = context ;
            this.progressPaginationLoading = progressPaginationLoading ;
            storageReference = FirebaseStorage.getInstance().getReference(Storage_Path);

            // Assign FirebaseDatabase instance with root database name.
            databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
            UploadImageFileToFirebaseStorage(uri,path,imageName);
        }
        return responseMutableLiveData;
    }








    private void UploadImageFileToFirebaseStorage(final Uri uri, String path, final String imageName) {
            // Checking whether FilePathUri Is empty or not.
            if (uri != null) {

                // Setting progressDialog Title.

                // Showing progressDialog.

                // Creating second StorageReference.
                StorageReference storageReference2nd = storageReference.child(path);

                // Adding addOnSuccessListener to second StorageReference.
                storageReference2nd.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                progressPaginationLoading.setVisibility(View.GONE);
                               // Showing toast message after done uploading.
                                @SuppressWarnings("VisibleForTests")
                                ImageUploadInfo imageUploadInfo = new ImageUploadInfo(imageName, String.valueOf(uri));

                                // Getting image upload ID.
                                String ImageUploadId = databaseReference.push().getKey();

                                // Adding image upload id s child element into databaseReference.
                                databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                                responseMutableLiveData.setValue(imageUploadInfo);
                            }
                        })
                        // If something goes wrong .
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                                // Hiding the progressDialog.
                                networkResponseErrorMessage.setValue(exception.getMessage());
                                // Showing exception erro message.
                            }
                        });

                        // On progress change upload time.
            }


    }
    public LiveData<List<ImageUploadInfo>> getUploadedImageFromFireBase() {
        if (responseUploadedImage == null) {
            responseUploadedImage = new MutableLiveData<>();
            this.context = context ;
            storageReference = FirebaseStorage.getInstance().getReference(Storage_Path);

            // Assign FirebaseDatabase instance with root database name.
            databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
            infoList = new ArrayList<>();
            getImageFromFireBase();
        }
        return responseUploadedImage;
    }


    private void getImageFromFireBase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
               if(snapshot.getValue() !=null) {
                   if(infoList !=null && !infoList.isEmpty())
                   {
                       infoList.clear();
                   }
                   for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                       ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);
                       infoList.add(imageUploadInfo);
                       responseUploadedImage.setValue(infoList);

                   }
               }else {
                   responseUploadedImage.setValue(null);
               }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                networkResponseErrorMessage.setValue(databaseError.getMessage());

            }
        });
    }

    public LiveData<String> getToastObserver(){
        return networkResponseErrorMessage;
    }


}


