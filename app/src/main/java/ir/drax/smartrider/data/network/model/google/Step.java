package ir.drax.smartrider.data.network.model.google;

class Step {

    private String travel_mode;
    private String html_instructions;
    private GLtLg start_location;
    private GLtLg end_location;
    private GPolyLine polyline;
    private GDuration duration;
    private GDuration distance;

    public String getTravel_mode() {
        return travel_mode;
    }

    public void setTravel_mode(String travel_mode) {
        this.travel_mode = travel_mode;
    }

    public String getHtml_instructions() {
        return html_instructions;
    }

    public void setHtml_instructions(String html_instructions) {
        this.html_instructions = html_instructions;
    }

    public GLtLg getStart_location() {
        return start_location;
    }

    public void setStart_location(GLtLg start_location) {
        this.start_location = start_location;
    }

    public GLtLg getEnd_location() {
        return end_location;
    }

    public void setEnd_location(GLtLg end_location) {
        this.end_location = end_location;
    }

    public GPolyLine getPolyline() {
        return polyline;
    }

    public void setPolyline(GPolyLine polyline) {
        this.polyline = polyline;
    }

    public GDuration getDuration() {
        return duration;
    }

    public void setDuration(GDuration duration) {
        this.duration = duration;
    }

    public GDuration getDistance() {
        return distance;
    }

    public void setDistance(GDuration distance) {
        this.distance = distance;
    }
}
