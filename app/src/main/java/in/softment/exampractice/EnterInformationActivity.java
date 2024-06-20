package in.softment.exampractice;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import in.softment.exampractice.Model.UserModel;
import in.softment.exampractice.Util.ProgressHud;
import in.softment.exampractice.Util.Services;

public class EnterInformationActivity extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 1;
    private ImageView profile_picture;
    private Uri resultUri = null;
    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), new ActivityResultCallback<CropImageView.CropResult>() {
        @Override
        public void onActivityResult(CropImageView.CropResult result) {
            if (result.isSuccessful()) {
                Uri uri = result.getUriContent();
                Bitmap bitmap = null;
                try {
                    resultUri = uri;
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    profile_picture.setImageBitmap(bitmap);


                } catch (IOException e) {

                }
            }
            else {
                Services.showDialog(EnterInformationActivity.this, "ERROR",result.getError().getLocalizedMessage());
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_information);

        EditText fullName = findViewById(R.id.fullNameET);
        EditText emailAddress = findViewById(R.id.emailET);
        EditText cityName = findViewById(R.id.cityNameET);
        EditText collegeName = findViewById(R.id.collegeNameET);

        profile_picture = findViewById(R.id.user_profile);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sFullName = fullName.getText().toString();
                String sEmail = emailAddress.getText().toString();
                String sCityName = cityName.getText().toString();
                String sCollegeName= collegeName.getText().toString();

                if (sFullName.isEmpty()) {
                    Services.showCenterToast(EnterInformationActivity.this,"Enter Full Name");
                }
                else if (sEmail.isEmpty()) {
                    Services.showCenterToast(EnterInformationActivity.this,"Enter Email Address");
                }
                else if (sCityName.isEmpty()) {
                    Services.showCenterToast(EnterInformationActivity.this,"Enter City Name");
                }
                else if (sCollegeName.isEmpty()) {
                    Services.showCenterToast(EnterInformationActivity.this,"Enter College Address");
                }
                else {

                    UserModel userModel = new UserModel();
                    userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    userModel.fullName = Services.toUpperCase(sFullName);
                    userModel.email =  sEmail;
                    userModel.cityName = Services.toUpperCase(sCityName);
                    userModel.collegeName = Services.toUpperCase(sCollegeName);
                    userModel.registredAt = new Date();
                    uploadImageOnFirebase(userModel);
                    Services.addUserDataOnServer(EnterInformationActivity.this,userModel);
                }

            }
        });

        //TapToChangeImage
        findViewById(R.id.imageViewFrame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermissionForReadExtertalStorage()) {
                    ShowFileChooser();
                }
                else {
                    requestStoragePermission();
                }

            }
        });

        if (!checkPermissionForReadExtertalStorage()) {
            requestStoragePermission();
        }
    }

    public void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return;
        }


        ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);//If the user has denied the permission previously your code will come to this block

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    public void ShowFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {

            Uri filepath = data.getData();

            CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(filepath, new CropImageOptions());
            cropImageContractOptions.setAspectRatio(1,1);
            cropImageContractOptions.setFixAspectRatio(true);
            cropImageContractOptions.setOutputCompressQuality(60);
            cropImage.launch(cropImageContractOptions);


        }
    }

    public boolean checkPermissionForReadExtertalStorage() {
        int result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void uploadImageOnFirebase(UserModel userModel) {
        ProgressHud.show(EnterInformationActivity.this,"Wait...");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ProfilePicture").child(userModel.getUid()+ ".png");
        UploadTask uploadTask = storageReference.putFile(resultUri);
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    ProgressHud.dialog.dismiss();
                    throw Objects.requireNonNull(task.getException());
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                ProgressHud.dialog.dismiss();
                if (task.isSuccessful()) {

                    userModel.profileImage = String.valueOf(task.getResult());

                }

                Services.addUserDataOnServer(EnterInformationActivity.this,userModel);


            }
        });
    }
}
