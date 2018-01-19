package com.mad.group16.chatroom;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    TextView userNameId;
    ImageView imageButton;
    ListView listView;
    RelativeLayout rL;
    RelativeLayout rl1;
    int position;
    Message message;
    AlertDialog alert;
    String flag1 = "AC", flag2 = "AC";

    EditText et1;
    ImageView  im1, im2;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DatabaseReference ref = fdb.getReference("message");
    DatabaseReference com = fdb.getReference("comment");

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser fu = mAuth.getCurrentUser();
    FirebaseStorage fs = FirebaseStorage.getInstance();
    StorageReference ref1;

    CustomAdapter customAdapter;

    ArrayList<Message> messages, comments, m;

    FirebaseAuth.AuthStateListener fa = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if(fu==null){
                finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("Chat");

        userNameId = (TextView) findViewById(R.id.userNameId);
        imageButton = (ImageView) findViewById(R.id.imageButton);
        listView = (ListView) findViewById(R.id.listView);
        rL = (RelativeLayout) findViewById(R.id.rL);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
            }
        });

        userNameId.setText(fu.getDisplayName());

        rl1 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.messagelayout,rL,true);

        et1 = (EditText) rl1.findViewById(R.id.mt1);
        im1 = (ImageView) rl1.findViewById(R.id.addPhoto1);
        im2 = (ImageView) rl1.findViewById(R.id.addMessage1);

        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                if(intent.resolveActivity(getPackageManager())!=null)
                    startActivityForResult(intent, 100);
            }
        });

        com.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                flag1="AC";
                comments = new ArrayList<Message>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Message m = ds.getValue(Message.class);
                    comments.add(m);

                    Log.d("test", m.toString());
                }
                flag1="ABC";
                Log.d("c","o1");
                /*if (flag1.equals("ABC") && flag2.equals("ABC")){
                    customAdapter = new CustomAdapter(ChatActivity.this, R.layout.messagelayout,messages);
                    listView.setAdapter(customAdapter);
                }*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                flag2="AC";
                messages = new ArrayList<Message>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Message m = ds.getValue(Message.class);
                    messages.add(m);
                    Log.d("test", m.toString());
                }
                flag2="ABC";
                Log.d("c","o2");
                /*if (flag1.equals("ABC") && flag2.equals("ABC")){
                    customAdapter = new CustomAdapter(ChatActivity.this, R.layout.messagelayout,messages);
                    listView.setAdapter(customAdapter);
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Runnable r = new Runnable() {

            @Override
            public void run() {
                while(true) {
                    while (!(flag2.equals("ABC") && flag1.equals("ABC"))) ;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            set();
                        }
                    });
                    while ((flag2.equals("ABC") && flag1.equals("ABC"))) ;
                }
            }
        };
        Thread t = new Thread(r);
        t.start();

        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et1!=null && et1.length()>0){

                    String key = ref.push().getKey();
                    Message message = new Message(key, fu.getDisplayName(),et1.getText().toString(), null, System.currentTimeMillis());
                    Map<String, Object> cu  = new HashMap<String, Object>();
                    cu.put("/"+key, message.toMap());
                    ref.updateChildren(cu);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(fa);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(fa!=null){
            mAuth.removeAuthStateListener(fa);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100 && resultCode == RESULT_OK && data!=null) {
            Uri uri = data.getData();
            message = new Message(null,fu.getDisplayName(),null, null, System.currentTimeMillis());
            fs.getReference("/images/"+ UUID.randomUUID()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    message.setImageURL(taskSnapshot.getMetadata().getDownloadUrl().toString());

                    String key = ref.push().getKey();
                    message.setId(key);
                    Map<String, Object> cu  = new HashMap<String, Object>();
                    cu.put("/"+key, message.toMap());
                    ref.updateChildren(cu);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("demo", e.getMessage());
                }
            });
        } else if(requestCode == 101 && resultCode == RESULT_OK && data!=null) {
            Uri uri = data.getData();
            message = new Message(null,fu.getDisplayName(),null, null, System.currentTimeMillis());
            fs.getReference("/images/"+ UUID.randomUUID()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    message.setImageURL(taskSnapshot.getMetadata().getDownloadUrl().toString());
                    message.setIscomment(true);
                    String key = ref.push().getKey();
                    message.setId(m.get(position).getId());
                    Map<String, Object> cu  = new HashMap<String, Object>();
                    cu.put("/"+key, message.toMap());
                    com.updateChildren(cu);

                    alert.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("demo", e.getMessage());
                }
            });
        }
    }

    public void click(View v) {
        position = (int) v.getTag();
        ViewGroup view = new LinearLayout(this);
        final RelativeLayout rL2 = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.messagelayout, view, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(rL2).setTitle("Comments");
        alert = builder.create();
        alert.show();
        rL2.findViewById(R.id.addMessage1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) rL2.findViewById(R.id.mt1);
                if(et!=null && et.length() >0) {
                    String key = com.push().getKey();
                    Message message = new Message(m.get(position).getId(), fu.getDisplayName(),et.getText().toString(), null, System.currentTimeMillis());
                    message.setIscomment(true);
                    Map<String, Object> cu  = new HashMap<String, Object>();
                    cu.put("/"+key, message.toMap());
                    com.updateChildren(cu);
                    alert.dismiss();
                }
            }
        });
        rL2.findViewById(R.id.addPhoto1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                if(intent.resolveActivity(getPackageManager())!=null)
                    startActivityForResult(intent, 101);
            }
        });
    }

    public void set() {
        m = new ArrayList<>();
        for(Message m1 : messages) {
            m.add(m1);
            if(comments != null)
                for(Message m2 : comments) {
                    if(m2.getId().equals(m1.getId()))
                        m.add(m2);
                }
        }
        customAdapter = new CustomAdapter(ChatActivity.this, R.layout.messagelayout,m);
        listView.setAdapter(customAdapter);
    }

}
