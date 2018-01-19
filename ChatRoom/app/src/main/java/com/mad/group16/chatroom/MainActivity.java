package com.mad.group16.chatroom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

     FirebaseAuth mAuth;
     DatabaseReference mDatabase;
     FirebaseAuth.AuthStateListener mAuthListener;

    EditText email, password;
    Button login, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("ChatRoom");

        mAuth= FirebaseAuth.getInstance();
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        login = (Button) findViewById(R.id.loginButton);
        signup =(Button) findViewById(R.id.signUp);
        email = (EditText) findViewById(R.id.em1);
        password = (EditText) findViewById(R.id.pw1);

        if(user!=null){
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivityForResult(intent, 1001);
        }else{
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userName = email.getText().toString();
                    String pwd = password.getText().toString();
                    if(userName.length()==0 || pwd.length()==0){
                        Toast.makeText(MainActivity.this, "Enter Username and Password Details", Toast.LENGTH_SHORT).show();
                    }else{
                        mAuth.signInWithEmailAndPassword(userName, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }else{
                                    email.setText("");
                                    password.setText("");
                                    Intent intent1 = new Intent(MainActivity.this, ChatActivity.class);
                                    startActivityForResult(intent1,1001);
                                }
                            }
                        });
                    }
                }
            });

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(MainActivity.this, SignupActivity.class);
                    startActivityForResult(intent2, 2001);
                }
            });
        }


    }
}
