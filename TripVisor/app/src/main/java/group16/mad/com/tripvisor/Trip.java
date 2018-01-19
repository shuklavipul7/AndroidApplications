package group16.mad.com.tripvisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vipul on 4/18/2017.
 */

public class Trip implements Serializable{
    String id, name, location, createdBy;
    ArrayList<Messages> messages = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();
    Boolean hasDefaultPicture = true;
    ArrayList<Places> listOfPlaces = new ArrayList<>();

    public Trip() {
    }

    public Trip(String id, String name, String location, String createdBy, Boolean hasDefaultPicture) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.hasDefaultPicture = hasDefaultPicture;
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<Messages> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Messages> messages) {
        this.messages = messages;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public Boolean getHasDefaultPicture() {
        return hasDefaultPicture;
    }

    public void setHasDefaultPicture(Boolean hasDefaultPicture) {
        this.hasDefaultPicture = hasDefaultPicture;
    }

    public ArrayList<Places> getListOfPlaces() {
        return listOfPlaces;
    }

    public void setListOfPlaces(ArrayList<Places> listOfPlaces) {
        this.listOfPlaces = listOfPlaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trip trip = (Trip) o;

        return id.equals(trip.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
