package group16.mad.com.tripvisor;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    EditText firstNameEditText, lastNameEditText, emailEditText, repeatPswdEditText, choosePswdEditText;
    Button cancelButton, updateProfile;
    RadioGroup gender;
    RadioButton maleButton, femaleButton;
    ImageView avatar;
    User currentUser;
    Uri uriOfAvatar;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference usersReference = firebaseDatabase.getReference("User");
    private StorageReference mStorageRef= FirebaseStorage.getInstance().getReference();
    Boolean hasChanged=false;
    //User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("Your Profile");

        currentUser = (User) getIntent().getExtras().get(Dashboard.USER);
        mAuth= FirebaseAuth.getInstance();

        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        choosePswdEditText = (EditText) findViewById(R.id.choosePswdEditText);
        repeatPswdEditText = (EditText) findViewById(R.id.repeatPswdEditText);
        avatar = (ImageView) findViewById(R.id.avatar);
        gender = (RadioGroup) findViewById(R.id.genderRadioGroup);
        updateProfile = (Button) findViewById(R.id.updateProfile);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        maleButton = (RadioButton) findViewById(R.id.male);
        femaleButton = (RadioButton) findViewById(R.id.female);

        loadUserProfile();

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                if(intent.resolveActivity(getPackageManager())!=null)
                    startActivityForResult(intent, 100);
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fname = firstNameEditText.getText().toString();
                final String lName = lastNameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                int selectedGenderId = gender.getCheckedRadioButtonId();
                final String genderText = ((RadioButton)findViewById(selectedGenderId)).getText().toString();

                if(fname.length()==0 || lName.length()==0 || email.length()==0){
                    Toast.makeText(ProfileActivity.this, "Enter valid Details", Toast.LENGTH_SHORT).show();
                }else{
                    final String key = currentUser.getId();
                    if(hasChanged==false) {
                        if(currentUser.getHasDefaultUrl()==true) {
                           // user = new User(key, fname, lName, genderText, email, true);
                            currentUser.setId(key);
                            currentUser.setfName(fname);
                            currentUser.setlName(lName);
                            currentUser.setGender(genderText);
                            currentUser.setEmail(email);
                            currentUser.setHasDefaultUrl(true);
                        }else{
                           // user = new User(key, fname, lName, genderText, email, false);
                            currentUser.setId(key);
                            currentUser.setfName(fname);
                            currentUser.setlName(lName);
                            currentUser.setGender(genderText);
                            currentUser.setEmail(email);
                            currentUser.setHasDefaultUrl(false);
                        }
                    }else{
                        StorageReference avatarReference = mStorageRef.child("avatar/"+key+"/avatar.png");
                        //user = new User(key, fname, lName, genderText,email,false);
                        currentUser.setId(key);
                        currentUser.setfName(fname);
                        currentUser.setlName(lName);
                        currentUser.setGender(genderText);
                        currentUser.setEmail(email);
                        currentUser.setHasDefaultUrl(false);
                        avatarReference.putFile(uriOfAvatar);
                    }

                    Map<String, Object> userMapToBeSaved  = new HashMap<String, Object>();
                    userMapToBeSaved.put("/"+key, currentUser.toMap());
                    usersReference.updateChildren(userMapToBeSaved);
                    Toast.makeText(ProfileActivity.this,"Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            }
        });

    }

    public void loadUserProfile(){
        firstNameEditText.setText(currentUser.getfName());
        lastNameEditText.setText(currentUser.getlName());
        emailEditText.setText(currentUser.getEmail());
        emailEditText.setEnabled(false);
        if(currentUser.getGender().equalsIgnoreCase("Male")){
            maleButton.setChecked(true);
        }else{
            femaleButton.setChecked(true);
        }

        if (currentUser.getHasDefaultUrl() == true) {
            StorageReference imagePath = mStorageRef.child("defaultAvatar/defaultavatar.png");
            imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("Path of default avatar:", uri.getPath());
                    uriOfAvatar = uri;
                }
            });
            Glide.with(ProfileActivity.this)
                    .using(new FirebaseImageLoader())
                    .load(imagePath).skipMemoryCache(true).signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(avatar);
        } else {
            StorageReference imagePath = mStorageRef.child("avatar/" + currentUser.getId() + "/avatar.png");
            imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("Path of default avatar:", uri.getPath());
                    uriOfAvatar = uri;
                }
            });
            Glide.with(ProfileActivity.this)
                    .using(new FirebaseImageLoader())
                    .load(imagePath).skipMemoryCache(true).signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(avatar);
        }
    }

    public void cancel(View view){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100 && resultCode == RESULT_OK && data!=null) {
            Uri uri = data.getData();
            hasChanged=true;
            uriOfAvatar = uri;
            Picasso.with(this).load(uri).into(avatar);
        }
    }
}
