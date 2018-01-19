package group16.mad.com.tripvisor;

import android.content.Intent;
import android.graphics.PorterDuff;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Signup extends AppCompatActivity {
    EditText firstNameEditText, lastNameEditText, emailEditText, repeatPswdEditText, choosePswdEditText;
    Button cancelButton, signUpButton;
    RadioGroup gender;
    ImageView avatar;
    Uri uriOfAvatar;
    User user;
    FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference usersReference = firebaseDatabase.getReference("User");
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign Up");

        mAuth = FirebaseAuth.getInstance();

        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        choosePswdEditText = (EditText) findViewById(R.id.choosePswdEditText);
        repeatPswdEditText = (EditText) findViewById(R.id.repeatPswdEditText);
        avatar = (ImageView) findViewById(R.id.avatar);
        gender = (RadioGroup) findViewById(R.id.genderRadioGroup);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(intent, 100);
            }
        });
    }

    public void signup(View view) {
        final String fname = firstNameEditText.getText().toString();
        final String lName = lastNameEditText.getText().toString();
        final String email = emailEditText.getText().toString();
        final String cPwd = choosePswdEditText.getText().toString();
        final String rPwd = repeatPswdEditText.getText().toString();
        int selectedGenderId = gender.getCheckedRadioButtonId();
        final String genderText = ((RadioButton) findViewById(selectedGenderId)).getText().toString();

        if (fname.length() == 0 || lName.length() == 0 || email.length() == 0 || cPwd.length() == 0 || rPwd.length() == 0) {
            Toast.makeText(Signup.this, "Enter valid Details", Toast.LENGTH_SHORT).show();
        } else if (!cPwd.equals(rPwd)) {
            Toast.makeText(Signup.this, "Passwords Not Same", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, cPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(Signup.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest pr = new UserProfileChangeRequest.Builder().setDisplayName(fname + " " + lName).build();
                        firebaseUser.updateProfile(pr).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Signup.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Signup.this, "User successfully created", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
                        final String key = usersReference.push().getKey();
                        if (uriOfAvatar == null) {
                            //StorageReference avatarReference = mStorageRef.child("avatar/"+key+"/avatar.png");
                            user = new User(key, fname, lName, genderText, email, true);
                        } else {
                            StorageReference avatarReference = mStorageRef.child("avatar/" + key + "/avatar.png");
                            user = new User(key, fname, lName, genderText, email, false);
                            avatarReference.putFile(uriOfAvatar);
                        }
                        Map<String, Object> userMapToBeSaved = new HashMap<String, Object>();
                        userMapToBeSaved.put("/" + key, user.toMap());
                        usersReference.updateChildren(userMapToBeSaved);
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            uriOfAvatar = uri;
            Picasso.with(this).load(uri).into(avatar);
        }
    }

    public void cancel(View view) {
        finish();
    }
}
