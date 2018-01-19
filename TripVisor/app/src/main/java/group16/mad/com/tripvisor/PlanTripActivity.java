package group16.mad.com.tripvisor;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class PlanTripActivity extends AppCompatActivity {
    ImageView tripPicture;
    EditText tripTitle, tripLocation;

    User user;
    Uri uriOfTripPicture;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference usersReference = firebaseDatabase.getReference("User");
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_trip);

        tripPicture = (ImageView) findViewById(R.id.tripPicture);
        tripTitle = (EditText) findViewById(R.id.tripTitle);
        tripLocation = (EditText) findViewById(R.id.tripLocation);

        user = (User) getIntent().getExtras().get(Dashboard.USER);

        tripPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(intent, 100);
            }
        });
    }

    public void cancel(View view) {
        finish();
    }

    public void createTrip(View view) {
        final String tripTitleString = tripTitle.getText().toString();
        final String tripLocationString = tripLocation.getText().toString();

        if (tripTitleString.length() == 0 || tripLocationString.length() == 0) {
            Toast.makeText(PlanTripActivity.this, "Enter valid Details", Toast.LENGTH_SHORT).show();
        } else {
            if (uriOfTripPicture != null) {
                String key = user.getId();
                StorageReference tripStorageReference = mStorageRef.child("tripPhoto/" + key + "/tripPhoto.png");
                tripStorageReference.putFile(uriOfTripPicture);

                String tripKey = usersReference.child("trips").push().getKey();
                String createdBy = user.getfName()+" "+user.getlName();

                Trip trip = new Trip(tripKey, tripTitleString, tripLocationString, createdBy, false);

                User userToBeAddedToTrip = new User();
                userToBeAddedToTrip.setId(user.getId());
                userToBeAddedToTrip.setfName(user.getfName());
                userToBeAddedToTrip.setlName(user.getlName());

                trip.getUsers().add(userToBeAddedToTrip);

                user.getTripsCreated().add(trip);
                user.getTripsJoined().add(trip);

                Map<String, Object> userMapToBeSaved = new HashMap<String, Object>();
                userMapToBeSaved.put("/" + key, user.toMap());
                usersReference.updateChildren(userMapToBeSaved);

                Toast.makeText(PlanTripActivity.this, "Trip created", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                String key = user.getId();
                String createdBy = user.getfName()+" "+user.getlName();
                String tripKey = usersReference.child("trips").push().getKey();
                Trip trip = new Trip(tripKey, tripTitleString, tripLocationString, createdBy, true);

                User userToBeAddedToTrip = new User();
                userToBeAddedToTrip.setId(user.getId());
                userToBeAddedToTrip.setfName(user.getfName());
                userToBeAddedToTrip.setlName(user.getlName());

                trip.getUsers().add(userToBeAddedToTrip);

                user.getTripsCreated().add(trip);
                user.getTripsJoined().add(trip);

                Map<String, Object> userMapToBeSaved = new HashMap<String, Object>();
                userMapToBeSaved.put("/" + key, user.toMap());
                usersReference.updateChildren(userMapToBeSaved);

                Toast.makeText(PlanTripActivity.this, "Trip created", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            uriOfTripPicture = uri;
            Picasso.with(this).load(uri).into(tripPicture);
        }
    }
}
