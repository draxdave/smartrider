package ir.drax.smartrider.data.db.model;

import com.google.android.gms.maps.model.LatLng;

import ir.drax.smartrider.ui.main.navigation.interfaces.NavigationStateChangeListener;

public class NavigateModel {
    private NavigationStateChangeListener changeListener;
    private int navStep=0;

    public static int STATE_NONE = 0;
    public static int STATE_SELECTED = 1;
    public static int STATE_READY = 2;
    public static int STATE_STARTED = 3;

    private int state = STATE_NONE;
    private LatLng origin;
    private LatLng dest ;
    private LatLng sec_dest;

    public NavigateModel(NavigationStateChangeListener navigationStateChangeListener) {
        changeListener=navigationStateChangeListener;
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        if (state==STATE_SELECTED)
            changeListener.onSelected();
        else if (state==STATE_READY)
            changeListener.onReady();
        else if (state == STATE_STARTED)
            changeListener.onStarted();
        else
            changeListener.onNone();
    }

    public int getNavStep() {
        return navStep;
    }

    public void nextStep() {
        this.navStep ++;
    }
    public void prevStep() {
        navStep --;
    }
    public void prevState() {
        if (state==STATE_READY)
            setState(state-2);
        else
            setState(state -1);
    }

    public void reset(){
        state=STATE_NONE;
        origin=null;
        dest=null;
        sec_dest=null;
        navStep=0;
        changeListener.onReset();
    }
}
