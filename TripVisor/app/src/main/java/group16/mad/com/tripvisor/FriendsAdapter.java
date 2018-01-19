package group16.mad.com.tripvisor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vipul on 4/16/2017.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.RowHolder>{
    private ArrayList<User> itemList;
    TextView friendsName;

    public class RowHolder extends RecyclerView.ViewHolder {
        public View itemView;

        public RowHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            friendsName = (TextView) itemView.findViewById(R.id.friendsName);
        }
    }

    public FriendsAdapter(ArrayList<User> itemList) {
        this.itemList = itemList;
    }

    @Override
    public FriendsAdapter.RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_friends, parent, false);
        return new FriendsAdapter.RowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        final User userFriend = itemList.get(position);
        friendsName.setText(userFriend.getfName() + " " + userFriend.getlName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
