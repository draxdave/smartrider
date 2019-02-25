package ir.drax.smartrider;

import com.google.android.gms.maps.model.LatLng;

public class IntentModel {
    private LatLng source = new LatLng(0,0);
    private LatLng dest = new LatLng(0,0);
    private LatLng sec_dest = new LatLng(0,0);
    private boolean two_way = false;
    private int wait_type = 0;
    private int vehicle_type = 3;

    public LatLng getSource() {
        return source;
    }

    public void setSource(LatLng source) {
        this.source = source;
    }

    public LatLng getDest() {
        return dest;
    }

    public void setDest(LatLng dest) {
        this.dest = dest;
    }

    public LatLng getSec_dest() {
        return sec_dest;
    }

    public void setSec_dest(LatLng sec_dest) {
        this.sec_dest = sec_dest;
    }

    public boolean isTwo_way() {
        return two_way;
    }

    public void setTwo_way(boolean two_way) {
        this.two_way = two_way;
    }

    public int getWait_type() {
        return wait_type;
    }

    public void setWait_type(int wait_type) {
        this.wait_type = wait_type;
    }

    public int getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(int vehicle_type) {
        this.vehicle_type = vehicle_type;
    }
}
