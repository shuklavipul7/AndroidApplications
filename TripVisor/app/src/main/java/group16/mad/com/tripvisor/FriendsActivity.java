package group16.mad.com.tripvisor;

import android.content.SharedPreferences;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class FriendsActivity extends AppCompatActivity {
    User currentUser;
    TextView emptyFriendListMessage;
    ArrayList<User> friendList = new ArrayList<>();
    ArrayList<User> friendRequestsSent = new ArrayList<User>();
    ArrayList<User> friendRequestsReceived = new ArrayList<User>();
    ArrayList<User> listOfAllUsers = new ArrayList<>();
    private Boolean exit = false;
    RecyclerView friendsList;
    AddNewFriendsAdapter addNewFriendsAdapter;
    FriendsRequestAdapter friendsRequestAdapter;
    FriendsAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        setTitle("Your Friends");
        emptyFriendListMessage = (TextView) findViewById(R.id.emptyFriendsPageMsg);
        friendsList = (RecyclerView) findViewById(R.id.friends_list);

        currentUser = (User) getIntent().getExtras().get(Dashboard.USER);
        listOfAllUsers = (ArrayList<User>) getIntent().getExtras().get(Dashboard.ALL_USERS);
        friendList = currentUser.getFriends();
        friendRequestsReceived = currentUser.getFriendRequestsReceived();

        if(friendList.size()==0){
            emptyFriendListMessage.setVisibility(View.VISIBLE);
            friendsList.setVisibility(View.INVISIBLE);
        }else{
            emptyFriendListMessage.setVisibility(View.INVISIBLE);
            friendsList.setVisibility(View.VISIBLE);

            friendsAdapter = new FriendsAdapter(friendList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(friendsList.getContext(),
                    LinearLayoutManager.VERTICAL);
            friendsList.addItemDecoration(dividerItemDecoration);
            friendsList.setLayoutManager(mLayoutManager);
            friendsList.setItemAnimator(new DefaultItemAnimator());
            friendsList.setAdapter(friendsAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friends_activity, menu);
        return true;
    }

    public void showUsersWhoAreNotFriends(MenuItem menuItem){
        ArrayList<User> allUsersWhoAreNotFriends  = new ArrayList<>();
        CopyOnWriteArrayList<User> copyOnWriteArrayList= new CopyOnWriteArrayList<>();
        for(User u : listOfAllUsers){
            copyOnWriteArrayList.add(u);
        }

        for(User u : copyOnWriteArrayList){
            if(friendList.contains(u)){
                copyOnWriteArrayList.remove(u);
            }
        }

        for(User u : copyOnWriteArrayList){
            allUsersWhoAreNotFriends.add(u);
        }

        if(allUsersWhoAreNotFriends.size()>0){
            friendsList.setVisibility(View.VISIBLE);
            emptyFriendListMessage.setVisibility(View.INVISIBLE);
        }else{
            friendsList.setVisibility(View.INVISIBLE);
            emptyFriendListMessage.setVisibility(View.VISIBLE);
            emptyFriendListMessage.setText("No new members found");
        }

        allUsersWhoAreNotFriends.remove(currentUser);
        addNewFriendsAdapter = new AddNewFriendsAdapter(allUsersWhoAreNotFriends, this, currentUser);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(friendsList.getContext(),
                LinearLayoutManager.VERTICAL);
        friendsList.addItemDecoration(dividerItemDecoration);
        friendsList.setLayoutManager(mLayoutManager);
        friendsList.setItemAnimator(new DefaultItemAnimator());
        friendsList.setAdapter(addNewFriendsAdapter);
    }

    public void viewFriendRequests(MenuItem menuItem){
        friendsRequestAdapter = new FriendsRequestAdapter(friendRequestsReceived, this, currentUser, listOfAllUsers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(friendsList.getContext(),
                LinearLayoutManager.VERTICAL);
        friendsList.addItemDecoration(dividerItemDecoration);
        friendsList.setLayoutManager(mLayoutManager);
        friendsList.setItemAnimator(new DefaultItemAnimator());
        friendsList.setAdapter(friendsRequestAdapter);
    }
}
