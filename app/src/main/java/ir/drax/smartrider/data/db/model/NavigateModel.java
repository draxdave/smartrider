package ir.drax.smartrider.data.db.model;

import com.google.android.gms.maps.model.LatLng;

public class NavigateModel {
    private LatLng source = new LatLng(0,0);
    private LatLng dest = new LatLng(0,0);
    private LatLng sec_dest = new LatLng(0,0);

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

    }
