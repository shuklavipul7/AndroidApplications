package com.mad.group16.chatroom;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.text.SimpleDateFormat;

public class SignupActivity extends AppCompatActivity {
    EditText firstNameEditText, lastNameEditText, emailEditText, repeatPswdEditText, choosePswdEditText;
    Button cancelButton, signUpButton;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign Up");

        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        choosePswdEditText = (EditText) findViewById(R.id.choosePswdEditText);
        repeatPswdEditText = (EditText) findViewById(R.id.repeatPswdEditText);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        mAuth= FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              final String fname = firstNameEditText.getText().toString();
               final String lName = lastNameEditText.getText().toString();
               String email = emailEditText.getText().toString();
                String cPwd = choosePswdEditText.getText().toString();
                String rPwd = repeatPswdEditText.getText().toString();

                if(fname.length()==0 || lName.length()==0 || email.length()==0 || cPwd.length()==0 || rPwd.length()==0){
                    Toast.makeText(SignupActivity.this, "Enter valid Details", Toast.LENGTH_SHORT).show();
                }else if(!cPwd.equals(rPwd)){
                    Toast.makeText(SignupActivity.this, "Passwords Not Same", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(email, cPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                          if(!task.isSuccessful()){
                              Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                          }else{
                              firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                              UserProfileChangeRequest pr= new UserProfileChangeRequest.Builder().setDisplayName(fname +" "+ lName).build();
                              firebaseUser.updateProfile(pr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      if(!task.isSuccessful()){
                                          Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                      }else{
                                          mAuth.signOut();
                                          Toast.makeText(SignupActivity.this, "User successfully created", Toast.LENGTH_SHORT).show();
                                          finish();
                                      }
                                  }
                              });
                          }
                        }
                    });
                }
            }
        });
    }
}
