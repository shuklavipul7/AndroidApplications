package group16.mad.com.tripvisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vipul.Shukla on 4/13/2017.
 */

public class User implements Serializable{
    String id,fName, lName, gender, email;
    Boolean hasDefaultUrl;
    ArrayList<User> friends = new ArrayList<User>();
    ArrayList<User> friendRequestsSent = new ArrayList<User>();
    ArrayList<User> friendRequestsReceived = new ArrayList<User>();
    ArrayList<Trip> tripsCreated = new ArrayList<>();
    ArrayList<Trip> tripsJoined = new ArrayList<>();

    public User() {
    }

    public User(String id, String fName, String lName, String gender, String email, Boolean hasDefaultUrl) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.gender = gender;
        this.email = email;
        this.hasDefaultUrl = hasDefaultUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getHasDefaultUrl() {
        return hasDefaultUrl;
    }

    public void setHasDefaultUrl(Boolean hasDefaultUrl) {
        this.hasDefaultUrl = hasDefaultUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public ArrayList<User> getFriendRequestsSent() {
        return friendRequestsSent;
    }

    public void setFriendRequestsSent(ArrayList<User> friendRequestsSent) {
        this.friendRequestsSent = friendRequestsSent;
    }

    public ArrayList<User> getFriendRequestsReceived() {
        return friendRequestsReceived;
    }

    public void setFriendRequestsReceived(ArrayList<User> friendRequestsReceived) {
        this.friendRequestsReceived = friendRequestsReceived;
    }

    public ArrayList<Trip> getTripsCreated() {
        return tripsCreated;
    }

    public void setTripsCreated(ArrayList<Trip> tripsCreated) {
        this.tripsCreated = tripsCreated;
    }

    public ArrayList<Trip> getTripsJoined() {
        return tripsJoined;
    }

    public void setTripsJoined(ArrayList<Trip> tripsJoined) {
        this.tripsJoined = tripsJoined;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m1 = new HashMap<>();
        m1.put("id",id);
        m1.put("fName", fName);
        m1.put("lName", lName);
        m1.put("gender", gender);
        m1.put("email",email);
        m1.put("hasDefaultUrl",hasDefaultUrl);
        m1.put("friendRequestsSent",friendRequestsSent);
        m1.put("friendRequestsReceived",friendRequestsReceived);
        m1.put("friends",friends);
        m1.put("tripsCreated",tripsCreated);
        m1.put("tripsJoined", tripsJoined);
        return m1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
