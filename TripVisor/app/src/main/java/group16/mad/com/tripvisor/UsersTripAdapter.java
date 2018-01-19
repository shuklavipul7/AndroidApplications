package group16.mad.com.tripvisor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vipul on 4/18/2017.
 */

public class UsersTripAdapter extends RecyclerView.Adapter<UsersTripAdapter.RowHolder> {
    private final Context context;
    private ArrayList<Trip> itemList;
    User currentUser;
    TextView tripDescription;

    public View itemView;

    public class RowHolder extends RecyclerView.ViewHolder {

        public RowHolder(View itemView) {
            super(itemView);
            UsersTripAdapter.this.itemView = itemView;
            tripDescription = (TextView) itemView.findViewById(R.id.tripInfo);
        }
    }

    public UsersTripAdapter(ArrayList<Trip> itemList,User currentUser, Context context) {
        this.itemList = itemList;
        this.context = context;
        this.currentUser = currentUser;
    }

    @Override
    public UsersTripAdapter.RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_trips, parent, false);
        return new UsersTripAdapter.RowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        final Trip trip = itemList.get(position);
        tripDescription.setText(trip.getName());

        UsersTripAdapter.this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatPageActivity.class);
                intent.putExtra(TripActivity.TRIP, trip);
                //intent.putExtra(TripActivity.CURRENT_USER, currentUser);
                context.startActivity(intent);
            }
        });

        UsersTripAdapter.this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference usersReference = firebaseDatabase.getReference("User");

                for(Trip t : currentUser.getTripsJoined()){
                    if(t.equals(trip)){
                        trip.getMessages().clear();
                    }
                }

                currentUser.getTripsJoined().remove(trip);
                currentUser.getTripsJoined().add(trip);

                Map<String, Object> userMapToBeSaved = new HashMap<String, Object>();
                userMapToBeSaved.put("/" + currentUser.getId(), currentUser.toMap());
                usersReference.updateChildren(userMapToBeSaved);

                Toast.makeText(context, "Chat cleared for this trip", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
