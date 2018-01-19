package group16.mad.com.tripvisor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TripActivity extends AppCompatActivity {
    User userMain;
    RecyclerView tripsRecyclerView;
    TextView noTripsMessage;
    Button planATripButton;
    DatabaseReference mDatabase;
    String currentUserEmail;
    ArrayList<User> listOfAllUsers = new ArrayList<>();
    User currentUser;
    ArrayList<Trip> tripsAlreadyJoined = new ArrayList<>();
    ArrayList<Trip> tripsThatUserCanJoin = new ArrayList<>();
    public static final String TRIP = "trip";
    public static final String CURRENT_USER = "currentUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        tripsRecyclerView = (RecyclerView) findViewById(R.id.tripsRecyclerView);
        noTripsMessage = (TextView) findViewById(R.id.noTripsMessage);
        planATripButton = (Button) findViewById(R.id.planATripButton);

        userMain = (User) getIntent().getExtras().get(Dashboard.USER);

        mDatabase = FirebaseDatabase.getInstance().getReference("User");
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
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
                    //Here I am getting the current user
                    if (user.getEmail().equals(currentUserEmail)) {
                        currentUser = user;
                    }
                }

                tripsAlreadyJoined.clear();
                for (Trip trip : currentUser.getTripsJoined()) {
                    tripsAlreadyJoined.add(trip);
                }

                ArrayList<User> listOfFriends = new ArrayList<User>();
                for(User u : listOfAllUsers){
                    if(userMain.getFriends().contains(u)){
                        listOfFriends.add(u);
                    }
                }

                tripsThatUserCanJoin.clear();
                for (User user : listOfAllUsers) {
                    if (user.getTripsCreated() != null) {
                        for (Trip trip : user.getTripsCreated()) {
                            if (((userMain.getTripsJoined() == null) || !userMain.getTripsJoined().contains(trip)) && (listOfFriends.contains(user))) {
                                tripsThatUserCanJoin.add(trip);
                            }
                        }
                    }
                }

                if (tripsAlreadyJoined != null && tripsAlreadyJoined.size() > 0) {
                    tripsRecyclerView.setVisibility(View.VISIBLE);
                    noTripsMessage.setVisibility(View.INVISIBLE);

                    UsersTripAdapter usersTripAdapter = new UsersTripAdapter(tripsAlreadyJoined, userMain, TripActivity.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(tripsRecyclerView.getContext(),
                            LinearLayoutManager.VERTICAL);
                    tripsRecyclerView.addItemDecoration(dividerItemDecoration);
                    tripsRecyclerView.setLayoutManager(mLayoutManager);
                    tripsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    tripsRecyclerView.setAdapter(usersTripAdapter);

                } else {
                    tripsRecyclerView.setVisibility(View.INVISIBLE);
                    noTripsMessage.setVisibility(View.VISIBLE);
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
                user.setTripsCreated((ArrayList<Trip>) map2.get("tripsCreated"));
                user.setTripsJoined((ArrayList<Trip>) map2.get("tripsJoined"));
                return user;
            }

            public Trip parseTripData(HashMap map2) {
                Trip trip = new Trip();
                trip.setLocation((String) map2.get("location"));
                trip.setName((String) map2.get("name"));
                trip.setId((String) map2.get("id"));
                trip.setCreatedBy((String)map2.get("createdBy"));

                ArrayList al = (ArrayList) map2.get("messages");
                if (al != null) {
                    for (Object u : al)
                        trip.getMessages().add(parseMessageData((HashMap) u));
                }

                return trip;
            }

            Messages parseMessageData(HashMap map4){
                Messages messages = new Messages();
                messages.setText((String)map4.get("text"));
                messages.setTime((String)map4.get("time"));
                messages.setImage((Boolean)map4.get("isImage"));
                messages.setId((String)map4.get("id"));

                HashMap hmap = (HashMap) map4.get("user");
                User u1 = new User();
                u1.setId((String)hmap.get("id"));
                u1.setfName((String)hmap.get("fName"));
                u1.setlName((String)hmap.get("lName"));
                messages.setUser(u1);
                return messages;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_trips, menu);
        return true;
    }

    public void openPlanTripActivity(View view) {
        Intent intent = new Intent(this, PlanTripActivity.class);
        intent.putExtra(Dashboard.USER, userMain);
        startActivity(intent);
    }

    public void exploreTrips(MenuItem menuItem) {
        tripsRecyclerView.setVisibility(View.VISIBLE);
        noTripsMessage.setVisibility(View.INVISIBLE);

        AllTheOtherTripsAdapter allTheOtherTripsAdapter = new AllTheOtherTripsAdapter(tripsThatUserCanJoin, currentUser, TripActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(tripsRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        tripsRecyclerView.addItemDecoration(dividerItemDecoration);
        tripsRecyclerView.setLayoutManager(mLayoutManager);
        tripsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tripsRecyclerView.setAdapter(allTheOtherTripsAdapter);
    }

}
