package ir.drax.smartrider.data.network.model.google;

import java.util.List;

public class Route {
    private String summary;
    private List<Leg> legs;
    private List<Integer> waypoint_order;
    private GBounds bounds;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public List<Integer> getWaypoint_order() {
        return waypoint_order;
    }

    public void setWaypoint_order(List<Integer> waypoint_order) {
        this.waypoint_order = waypoint_order;
    }

    public GBounds getBounds() {
        return bounds;
    }

    public void setBounds(GBounds bounds) {
        this.bounds = bounds;
    }
}
