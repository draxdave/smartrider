package ir.drax.smartrider.ui.main.navigation;

import ir.drax.smartrider.data.network.model.GoogleDirectionResponse;

public interface A2F_NavigationInteraction {
    boolean onBackPressed();
    void onMapReady();
    void setGoogleWaypoints(GoogleDirectionResponse googleWaypoints);
}
