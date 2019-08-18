package ir.drax.smartrider.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ir.drax.smartrider.data.network.model.google.Route;

/**
 * Created by janisharali on 28/01/17.
 */

public class GoogleDirectionResponse {

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("routes")
    private List<Route> routes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
