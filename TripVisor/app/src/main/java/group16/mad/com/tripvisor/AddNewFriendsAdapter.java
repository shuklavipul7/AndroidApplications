package group16.mad.com.tripvisor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vipul on 4/15/2017.
 */

public class AddNewFriendsAdapter extends RecyclerView.Adapter<AddNewFriendsAdapter.RowHolder> {
    private ArrayList<User> itemList;
    Context context;
    TextView friendsName;
    Button sendFriendRequest;
    Button cancelFriendRequest;
    User currentUser;
    DatabaseReference mDatabase1;
    DatabaseReference mDatabase2;

    public class RowHolder extends RecyclerView.ViewHolder {
        public View itemView;

        public RowHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            friendsName = (TextView) itemView.findViewById(R.id.friendsName);
            sendFriendRequest = (Button) itemView.findViewById(R.id.sendFriendRequest);
            cancelFriendRequest = (Button) itemView.findViewById(R.id.cancelFriendRequest);
        }
    }

    public AddNewFriendsAdapter(ArrayList<User> itemList, Context context, User user) {
        this.itemList = itemList;
        this.context = context;
        this.currentUser = user;
    }

    @Override
    public AddNewFriendsAdapter.RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_friends, parent, false);
        return new AddNewFriendsAdapter.RowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        final User userFriend = itemList.get(position);

        friendsName.setText(userFriend.getfName() + " " + userFriend.getlName());

        if (currentUser.getFriendRequestsSent().contains(userFriend)) {
            sendFriendRequest.setVisibility(View.INVISIBLE);
            cancelFriendRequest.setVisibility(View.VISIBLE);
        } else {
            sendFriendRequest.setVisibility(View.VISIBLE);
            cancelFriendRequest.setVisibility(View.INVISIBLE);
        }

        sendFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User userWhoSentRequest = new User();
                userWhoSentRequest.setId(currentUser.getId());
                userWhoSentRequest.setfName(currentUser.getfName());
                userWhoSentRequest.setlName(currentUser.getlName());
                userWhoSentRequest.setEmail(currentUser.getEmail());
                mDatabase2 = FirebaseDatabase.getInstance().getReference("User");

                userFriend.getFriendRequestsReceived().add(userWhoSentRequest);
                Map<String, Object> userMapToWhomRequestIsSent = new HashMap<String, Object>();
                userMapToWhomRequestIsSent.put("/" + userFriend.getId(), userFriend.toMap());
                mDatabase2.updateChildren(userMapToWhomRequestIsSent);

                mDatabase1 = FirebaseDatabase.getInstance().getReference("User");
                currentUser.getFriendRequestsSent().add(userFriend);
                Map<String, Object> userMapToBeSaved = new HashMap<String, Object>();
                userMapToBeSaved.put("/" + currentUser.getId(), currentUser.toMap());
                mDatabase1.updateChildren(userMapToBeSaved);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
