package group16.mad.com.tripvisor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vipul on 4/15/2017.
 */

public class FriendsRequestAdapter extends RecyclerView.Adapter<FriendsRequestAdapter.RowHolder> {
    ArrayList<User> listOfAllUsers;
    private ArrayList<User> itemList;
    Context context;
    User currentUser;
    TextView friendsName;
    ImageView accept;
    ImageView reject;
    DatabaseReference mDatabase1;
    DatabaseReference mDatabase2;

    public class RowHolder extends RecyclerView.ViewHolder {
        public View itemView;

        public RowHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            friendsName = (TextView) itemView.findViewById(R.id.friendsName);
            accept = (ImageView) itemView.findViewById(R.id.accept);
            reject = (ImageView) itemView.findViewById(R.id.reject);
        }
    }

    public FriendsRequestAdapter(ArrayList<User> itemList, Context context, User user, ArrayList<User> listOfAllUsers) {
        this.itemList = itemList;
        this.context = context;
        this.currentUser = user;
        this.listOfAllUsers = listOfAllUsers;
    }

    @Override
    public FriendsRequestAdapter.RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_requests, parent, false);
        return new FriendsRequestAdapter.RowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        final User userFriend = itemList.get(position);
        friendsName.setText(userFriend.getfName() + " " + userFriend.getlName());

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User userFriendComplete = new User();
                for (User u : listOfAllUsers) {
                    if (u.equals(userFriend)) {
                        userFriendComplete = u;
                    }
                }

                currentUser.getFriendRequestsReceived().remove(userFriend);
                userFriendComplete.getFriendRequestsSent().remove(currentUser);

                mDatabase1 = FirebaseDatabase.getInstance().getReference("User");
                currentUser.getFriends().add(userFriend);
                Map<String, Object> userMapToWhomRequestIsSent = new HashMap<String, Object>();
                userMapToWhomRequestIsSent.put("/" + currentUser.getId(), currentUser.toMap());
                mDatabase1.updateChildren(userMapToWhomRequestIsSent);

                mDatabase2 = FirebaseDatabase.getInstance().getReference("User");
                userFriendComplete.getFriends().add(currentUser);
                Map<String, Object> userMapWhoSentFriendRequest = new HashMap<String, Object>();
                userMapWhoSentFriendRequest.put("/" + userFriendComplete.getId(), userFriendComplete.toMap());
                mDatabase1.updateChildren(userMapWhoSentFriendRequest);
                Toast.makeText(context, "Friend request accepted", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mDatabase1 = FirebaseDatabase.getInstance().getReference("User");
                currentUser.getFriendRequestsReceived().remove(userFriend);
                Map<String, Object> userMapToWhomRequestIsSent = new HashMap<String, Object>();
                userMapToWhomRequestIsSent.put("/" + currentUser.getId(), currentUser.toMap());
                mDatabase1.updateChildren(userMapToWhomRequestIsSent);

                User currentUserCopy = new User();
                currentUserCopy.setfName(currentUser.getfName());
                currentUserCopy.setlName(currentUser.getlName());
                currentUserCopy.setId(currentUser.getId());

                mDatabase2 = FirebaseDatabase.getInstance().getReference("User");
                userFriend.getFriendRequestsSent().remove(currentUserCopy);
                Map<String, Object> userMapWhoSentFriendRequest = new HashMap<String, Object>();
                userMapWhoSentFriendRequest.put("/" + userFriend.getId(), userFriend.toMap());
                mDatabase1.updateChildren(userMapWhoSentFriendRequest);*/
                Toast.makeText(context, "Friend request rejected", Toast.LENGTH_SHORT).show();
                //notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}