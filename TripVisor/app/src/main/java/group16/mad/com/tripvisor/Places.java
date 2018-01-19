package group16.mad.com.tripvisor;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by vipul on 4/25/2017.
 */

public class Places {
    String placeName;
    LatLng latLng;

    public Places() {
    }

    public Places(String placeName, LatLng latLng) {
        this.placeName = placeName;
        this.latLng = latLng;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public String toString() {
        return placeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Places places = (Places) o;

        return placeName.equals(places.placeName);

    }

    @Override
    public int hashCode() {
        return placeName.hashCode();
    }
}
