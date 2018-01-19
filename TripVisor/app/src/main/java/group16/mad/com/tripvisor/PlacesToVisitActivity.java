package group16.mad.com.tripvisor;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlacesToVisitActivity extends AppCompatActivity {
    RecyclerView placesRecyclerView;
    Button addPlacesToTrip, viewTrip;
    TextView emptyPlacesTextView;
    Trip mainTrip;
    int REQ_CODE = 1;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference usersReference = firebaseDatabase.getReference("User");
    User currentUser;
    ArrayList<User> listOfAllUsers = new ArrayList<>();
    ArrayList<User> listOfUsersWhoArePartOfTheTrip = new ArrayList<>();
    String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_to_visit);

        placesRecyclerView = (RecyclerView) findViewById(R.id.places_recycler_view);
        addPlacesToTrip = (Button) findViewById(R.id.addPlacesToTrip);
        viewTrip = (Button) findViewById(R.id.viewTrip);
        emptyPlacesTextView = (TextView) findViewById(R.id.emptyPlacesTextView);

        mainTrip = (Trip) getIntent().getExtras().get(ChatPageActivity.TRIP_DETAILS);
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    @Override
    protected void onResume() {
        super.onResume();

        usersReference.addValueEventListener(new ValueEventListener() {

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

                for (User user : listOfAllUsers) {
                    if (user.getTripsJoined().contains(mainTrip)) {
                        listOfUsersWhoArePartOfTheTrip.add(user);
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
                user.setTripsCreated((ArrayList<Trip>) map2.get("tripsCreated"));
                user.setTripsJoined((ArrayList<Trip>) map2.get("tripsJoined"));
                return user;
            }

            public Trip parseTripData(HashMap map2) {
                Trip trip = new Trip();
                trip.setLocation((String) map2.get("location"));
                trip.setName((String) map2.get("name"));
                trip.setId((String) map2.get("id"));
                trip.setCreatedBy((String) map2.get("createdBy"));

                ArrayList al = (ArrayList) map2.get("messages");
                if (al != null) {
                    for (Object u : al)
                        trip.getMessages().add(parseMessageData((HashMap) u));
                }

                return trip;
            }

            Messages parseMessageData(HashMap map4) {
                Messages messages = new Messages();
                messages.setText((String) map4.get("text"));
                messages.setTime((String) map4.get("time"));
                messages.setImage((Boolean)map4.get("isImage"));
                messages.setId((String)map4.get("id"));

                HashMap hmap = (HashMap) map4.get("user");
                User u1 = new User();
                u1.setId((String) hmap.get("id"));
                u1.setfName((String) hmap.get("fName"));
                u1.setlName((String) hmap.get("lName"));
                messages.setUser(u1);
                return messages;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        if(mainTrip.getListOfPlaces()==null || mainTrip.getListOfPlaces().size()==0){
            emptyPlacesTextView.setVisibility(View.VISIBLE);
            placesRecyclerView.setVisibility(View.INVISIBLE);
        }else{
            emptyPlacesTextView.setVisibility(View.INVISIBLE);
            placesRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void startPlacePicker(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = builder.build(this);
            startActivityForResult(intent, REQ_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE){
            if(resultCode==RESULT_OK){
                Place place = PlacePicker.getPlace(data, this);
                String placeName = (String) place.getName();
                LatLng latLng = place.getLatLng();
                Places placeToVisit = new Places(placeName, latLng);
                mainTrip.getListOfPlaces().add(placeToVisit);

                Toast.makeText(this, placeName+" has been added to your trip", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
