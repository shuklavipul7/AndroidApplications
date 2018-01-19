package group16.mad.com.tripvisor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vipul.Shukla on 4/19/2017.
 */

public class AllTheOtherTripsAdapter extends RecyclerView.Adapter<AllTheOtherTripsAdapter.RowHolder> {
    private final Context context;
    User currentUser;
    private ArrayList<Trip> itemList;
    TextView tripDescription;
    Button joinTrip;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference usersReference = firebaseDatabase.getReference("User");

    public class RowHolder extends RecyclerView.ViewHolder {
        public View itemView;

        public RowHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tripDescription = (TextView) itemView.findViewById(R.id.tripName);
            joinTrip = (Button) itemView.findViewById(R.id.joinTrip);
        }
    }

    public AllTheOtherTripsAdapter(ArrayList<Trip> itemList, User currentUser,Context context) {
        this.itemList = itemList;
        this.currentUser = currentUser;
        this.context = context;
    }

    @Override
    public AllTheOtherTripsAdapter.RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_the_other_trips, parent, false);
        return new AllTheOtherTripsAdapter.RowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        final Trip trip = itemList.get(position);
        tripDescription.setText(trip.getName()+" - "+trip.createdBy);
        joinTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentUser.getTripsJoined().add(trip);

                Map<String, Object> userMapToBeSaved = new HashMap<String, Object>();
                userMapToBeSaved.put("/" + currentUser.getId(), currentUser.toMap());
                usersReference.updateChildren(userMapToBeSaved);

                joinTrip.setEnabled(false);
                Toast.makeText(context, "Trip Joined Successfully",Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
