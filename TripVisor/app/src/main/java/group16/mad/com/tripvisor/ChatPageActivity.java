package group16.mad.com.tripvisor;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatPageActivity extends AppCompatActivity {
    TextView tripTitle;
    RecyclerView messagesRecyclerView;
    EditText messageText;
    ImageView sendMessage;
    ImageView attachPhotoButton;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference usersReference = firebaseDatabase.getReference("User");
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    public static final String TRIP_DETAILS = "tripDetails";

    Trip mainTrip;
    User currentUser;
    String currentUserEmail;
    ArrayList<User> listOfAllUsers = new ArrayList<>();
    ArrayList<User> listOfUsersToWhomMessageWillBeSent = new ArrayList<>();
    Uri uriOfPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        mainTrip = (Trip) getIntent().getExtras().get(TripActivity.TRIP);

        tripTitle = (TextView) findViewById(R.id.tripTitle);
        messagesRecyclerView = (RecyclerView) findViewById(R.id.messagesRecyclerView);
        messageText = (EditText) findViewById(R.id.messageText);
        sendMessage = (ImageView) findViewById(R.id.sendMessage);
        attachPhotoButton = (ImageView) findViewById(R.id.attachPhotoButton);

        tripTitle.setText(mainTrip.getName() + " Trip");
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
                        listOfUsersToWhomMessageWillBeSent.add(user);
                    }
                }

                sendMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (messageText.getText().toString().length()==0) {
                        } else {

                            //This code will set the message in the user who sent the message
                            Messages messageToAdd = new Messages();
                            messageToAdd.setText(messageText.getText().toString());

                            messageToAdd.setTime(new Date().toString());

                            User userToSet = new User();
                            userToSet.setId(currentUser.getId());
                            userToSet.setfName(currentUser.getfName());
                            userToSet.setlName(currentUser.getlName());
                            messageToAdd.setUser(userToSet);
                            mainTrip.getMessages().add(messageToAdd);

                            currentUser.getTripsJoined().remove(mainTrip);
                            currentUser.getTripsJoined().add(mainTrip);

                            Map<String, Object> userMapToBeSaved = new HashMap<String, Object>();
                            userMapToBeSaved.put("/" + currentUser.getId(), currentUser.toMap());
                            usersReference.updateChildren(userMapToBeSaved);

                            //This code will set the message in the users who are a part of this trip

                            for (User u : listOfUsersToWhomMessageWillBeSent) {
                                u.getTripsJoined().remove(mainTrip);
                                u.getTripsJoined().add(mainTrip);

                                Map<String, Object> otherUsersMapToBeSaved = new HashMap<String, Object>();
                                otherUsersMapToBeSaved.put("/" + u.getId(), u.toMap());
                                usersReference.updateChildren(otherUsersMapToBeSaved);
                            }


                            messageText.setText("");
                        }
                    }
                });

                attachPhotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        if (intent.resolveActivity(getPackageManager()) != null)
                            startActivityForResult(intent, 100);
                    }
                });

                MessageLayoutAdapter messageLayoutAdapter = new MessageLayoutAdapter(mainTrip.getMessages(), currentUser, ChatPageActivity.this, mainTrip);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(messagesRecyclerView.getContext(),
                        LinearLayoutManager.VERTICAL);
                messagesRecyclerView.addItemDecoration(dividerItemDecoration);
                messagesRecyclerView.setLayoutManager(mLayoutManager);
                messagesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                messagesRecyclerView.setAdapter(messageLayoutAdapter);
                messageLayoutAdapter.notifyDataSetChanged();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            uriOfPic = uri;
            String key = firebaseDatabase.getReference("User/"+currentUser.getId()+"/tripsJoined/messages").push().getKey();
            StorageReference picReference = mStorageRef.child(mainTrip.getLocation()+"/" + key + "/trippic.png");
            picReference.putFile(uri);

            //This code will set the image in the user who sent the message
            Messages messageToAdd = new Messages();
            messageToAdd.setImage(true);
            messageToAdd.setId(key);
            messageToAdd.setTime(new Date().toString());

            User userToSet = new User();
            userToSet.setId(currentUser.getId());
            userToSet.setfName(currentUser.getfName());
            userToSet.setlName(currentUser.getlName());
            messageToAdd.setUser(userToSet);
            mainTrip.getMessages().add(messageToAdd);
            //mainTrip.setId(key);

            currentUser.getTripsJoined().remove(mainTrip);
            currentUser.getTripsJoined().add(mainTrip);

            Map<String, Object> userMapToBeSaved = new HashMap<String, Object>();
            userMapToBeSaved.put("/" + currentUser.getId(), currentUser.toMap());
            usersReference.updateChildren(userMapToBeSaved);

            //This code will set the message in the users who are a part of this trip

            for (User u : listOfUsersToWhomMessageWillBeSent) {
                u.getTripsJoined().remove(mainTrip);
                u.getTripsJoined().add(mainTrip);

                Map<String, Object> otherUsersMapToBeSaved = new HashMap<String, Object>();
                otherUsersMapToBeSaved.put("/" + u.getId(), u.toMap());
                usersReference.updateChildren(otherUsersMapToBeSaved);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_places, menu);
        return true;
    }

    public void explorePlaces(MenuItem menuItem){
        Intent intent = new Intent(ChatPageActivity.this, PlacesToVisitActivity.class);
        intent.putExtra(TRIP_DETAILS, mainTrip);
        startActivity(intent);
    }
}
