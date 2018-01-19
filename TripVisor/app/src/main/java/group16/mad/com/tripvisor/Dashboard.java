package group16.mad.com.tripvisor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity {
    DatabaseReference mDatabase;
    String currentUserEmail;
    User currentUser;
    ArrayList<User> listOfAllUsers = new ArrayList<User>();
    ImageView avatarInDashboard;
    TextView nameInDashboard;
    SharedPreferences sharedPreferences;
    public static String USER = "user";
    public static String ALL_USERS = "allUsers";
    FirebaseAuth mAuth;
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private Boolean exit = false;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setTitle("Your Dashboard");

        sharedPreferences = getSharedPreferences("tripvisor", MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        nameInDashboard = (TextView) findViewById(R.id.nameInDashboard);
        avatarInDashboard = (ImageView) findViewById(R.id.avatarInDashboard);

        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if (sharedPreferences.getString(USER, null) != null) {
            nameInDashboard.setText(sharedPreferences.getString(USER, null));
        }

        loading = new ProgressDialog(this);
        loading.setTitle("Loading Profile");
        loading.setCancelable(false);
        loading.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuforprofile, menu);
        return true;
    }

    public void viewProfile(MenuItem menuItem) {
        Intent profileActivity = new Intent(Dashboard.this, ProfileActivity.class);
        profileActivity.putExtra(USER, currentUser);
        startActivity(profileActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listOfAllUsers.clear();

                HashMap map2 = (HashMap) dataSnapshot.getValue();

                for (Object key : map2.keySet()) {
                    HashMap map3 = (HashMap) map2.get((String) key);
                    User user = new User();
                    user.setId((String) map3.get("id"));
                    user.setfName((String) map3.get("fName"));
                    user.setlName((String) map3.get("lName"));
                    user.setGender((String) map3.get("gender"));
                    user.setEmail((String) map3.get("email"));
                    user.setHasDefaultUrl((Boolean) map3.get("hasDefaultUrl"));

                    ArrayList al = (ArrayList) map3.get("friendRequestsSent");
                    if (al != null) {
                        for (Object u : al)
                            user.getFriendRequestsSent().add(parseFriendData((HashMap) u));
                    }

                    ArrayList al2 = (ArrayList) map3.get("friendRequestsReceived");
                    if (al2 != null) {
                        for (Object u : al2)
                            user.getFriendRequestsReceived().add(parseFriendData((HashMap) u));
                    }

                    ArrayList al3 = (ArrayList) map3.get("friends");
                    if (al3 != null) {
                        for (Object u : al3)
                            user.getFriends().add(parseFriendData((HashMap) u));
                    }

                    ArrayList al4 = (ArrayList) map3.get("tripsCreated");
                    if (al4 != null) {
                        for (Object u : al4)
                            user.getTripsCreated().add(parseTripData((HashMap) u));
                    }

                    ArrayList al5 = (ArrayList) map3.get("tripsJoined");
                    if (al5 != null) {
                        for (Object u : al5)
                            user.getTripsJoined().add(parseTripData((HashMap) u));
                    }

                    listOfAllUsers.add(user);
                }
                for (User user : listOfAllUsers) {
                    if (user.getEmail().equals(currentUserEmail)) {
                        currentUser = user;

                        nameInDashboard.setText(currentUser.getfName() + " " + currentUser.getlName());
                        sharedPreferences.edit().putString(USER, currentUser.getfName() + " " + currentUser.getlName()).commit();

                        if (currentUser.getHasDefaultUrl() == true) {
                            StorageReference imagePath = mStorageRef.child("defaultAvatar/defaultavatar.png");
                            try {
                                Glide.with(Dashboard.this)
                                        .using(new FirebaseImageLoader())
                                        .load(imagePath).skipMemoryCache(true).signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                                        .into(avatarInDashboard);
                                loading.dismiss();
                            }catch (Exception e){}

                        } else {
                            StorageReference imagePath = mStorageRef.child("avatar/" + currentUser.getId() + "/avatar.png");
                            try {
                                Glide.with(Dashboard.this)
                                        .using(new FirebaseImageLoader())
                                        .load(imagePath).skipMemoryCache(true).signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                                        .into(avatarInDashboard);
                                loading.dismiss();
                            }catch (Exception e){
                            }
                        }
                    }


                }
            }

            public User parseFriendData(HashMap map2) {
                User user = new User();
                user.setId((String) map2.get("id"));
                user.setfName((String) map2.get("fName"));
                user.setlName((String) map2.get("lName"));
                user.setGender((String) map2.get("gender"));
                user.setEmail((String) map2.get("email"));
                user.setHasDefaultUrl((Boolean) map2.get("hasDefaultUrl"));
                return user;
            }

            public Trip parseTripData(HashMap map2){
                Trip trip = new Trip();
                trip.setLocation((String)map2.get("location"));
                trip.setName((String)map2.get("name"));
                trip.setId((String)map2.get("id"));
                trip.setCreatedBy((String)map2.get("createdBy"));
                return trip;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void logout(MenuItem menuItem) {
        sharedPreferences.edit().clear().commit();
        mAuth.signOut();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            moveTaskToBack(true);
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    public void openFriendsActivity(View view) {
        Intent intent = new Intent(Dashboard.this, FriendsActivity.class);
        intent.putExtra(USER, currentUser);
        intent.putExtra(ALL_USERS, listOfAllUsers);
        startActivity(intent);
    }

    public void openTripsActivity(View view){
        Intent intent = new Intent(Dashboard.this, TripActivity.class);
        intent.putExtra(USER, currentUser);
        startActivity(intent);
    }
}
