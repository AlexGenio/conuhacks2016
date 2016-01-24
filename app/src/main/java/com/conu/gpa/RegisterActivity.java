package com.conu.gpa;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.conu.gpa.networking.GPAAPI;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;


public class RegisterActivity extends AppCompatActivity {
    final int CAMERA_CAPTURE = 1;
    final int CROP_PIC = 2;
    private Snackbar s;
    private Uri picUri;
    EditText txtEmail;
    EditText txtName;
    EditText txtPass;
    EditText txtSchool;
    ImageView imgProfile;
    EditText txtDesc;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         Bitmap bm = BitmapFactory.decodeResource(getResources(),
                R.drawable.test);
        setContentView(R.layout.activity_register);
        Button btnCreate =  (Button) findViewById(R.id.create);
        txtEmail = (EditText) findViewById(R.id.email);
        txtName = (EditText) findViewById(R.id.name);
        txtPass = (EditText) findViewById(R.id.password);
        txtSchool = (EditText) findViewById(R.id.school);
        imgProfile = (ImageView) findViewById((R.id.profile));
        txtDesc = (EditText) findViewById(R.id.desc);
        img = (ImageView) findViewById(R.id.profile);
        img.setImageBitmap(Globals.getCroppedBitmap(bm));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // use standard intent to capture an image
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        int permissionCheck = ContextCompat.checkSelfPermission(RegisterActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(RegisterActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    CAMERA_CAPTURE);
                            return;
                        }
                    }
                    img.setImageDrawable(null);
                    Crop.pickImage(RegisterActivity.this);
                } catch (ActivityNotFoundException anfe) {
                    //Toast toast = Toast.makeText(, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
                   // toast.show();
                }
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals.user.name = txtName.getText().toString();
                Globals.user.username = txtEmail.getText().toString();
                Globals.user.description = txtDesc.getText().toString();
                Globals.user.picture = imgProfile.getDrawingCache();
                if(!attemptRegister() ){
                    s = Snackbar.make(view, "Connecting to GPA+", Snackbar.LENGTH_LONG);
                    GPAAPI.createAccount(getApplicationContext(),Globals.user.username,Globals.user.picture,txtPass.getText().toString(),
                            Globals.user.schoolName,Globals.user.name,Globals.user.description, RegisterActivity.this);
                    s.show();
                }
            }
        });

    }

    private boolean attemptRegister() {
        // Reset errors.
        txtName.setError(null);
        txtEmail.setError(null);

        // Store values at the time of the login attempt.
        String email = txtEmail.getText().toString();
        String password = txtPass.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            txtEmail.setError(getString(R.string.error_field_required));
            focusView = txtEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            txtPass.setError(getString(R.string.error_field_required));
            focusView = txtEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        return cancel;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void afterSuccess(){
        if(s.isShown()) {
            s.dismiss();
        }
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void afterFailure(){
       txtPass.setError("Invalid password or email");
        if(s.isShown()) {
            s.dismiss();
        }
    }
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE) {
                // get the Uri for the captured image
                picUri = data.getData();
                performCrop();
            }
            // user is returning from cropping the image
            else if (requestCode == CROP_PIC) {
                // get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                ImageView picView = (ImageView) findViewById(R.id.profile);
                picView.setImageBitmap(Globals.getCroppedBitmap(thePic));
            }
        }
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            ImageView img = (ImageView) findViewById(R.id.profile);
            //img.setImageURI(Crop.getOutput(result));
            InputStream image_stream = null;
            try {
                image_stream = getContentResolver().openInputStream(Crop.getOutput(result));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap= BitmapFactory.decodeStream(image_stream );
            //Globals.user.picture = Globals.getCroppedBitmap(bitmap);
            img.setImageBitmap(Globals.getCroppedBitmap(bitmap));
          //  Globals.user.picture = Globals.getCroppedBitmap(bitmap);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 100);
            cropIntent.putExtra("outputY", 100);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
