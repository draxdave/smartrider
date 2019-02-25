package ir.drax.smartrider;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView message;
    private boolean locationDetected = false;
    private IntentModel launcher = new IntentModel();
    private ArrayList<Marker> markers=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        message = findViewById(R.id.message);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setMessage("Searching for location ...");

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION} ,100);

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (!locationDetected) {
                    // Add a marker in Sydney and move the camera
                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    setMessage("I'm here!");
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14.9f));
                    locationDetected = true;
                    findViewById(R.id.sourcePoint).setVisibility(View.VISIBLE);
                    findViewById(R.id.sourcePoint).animate()
                            .alpha(1)
                            .start();

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setMessage("Tap on source location");
                                }
                            });
                        }
                    },2000);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onMapReady(mMap);

    }

    private void setMessage(String m){
        message.setVisibility(View.VISIBLE);
        message.setAlpha(1);
        message.setText(m);
        message.animate().alpha(0)
                .setStartDelay(2000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

    }


    public void selectSource(View view) {
        if (!locationDetected)return;
        LatLng source= mMap.getCameraPosition().target;
        launcher.setSource(source);

        markers.add( mMap.addMarker(new MarkerOptions()
                .icon(Util.bitmapDescriptorFromVector(this,R.drawable.ic_source)).position(source)
                .title("Source")));

        findViewById(R.id.sourcePoint)
                .setVisibility(View.GONE);
        findViewById(R.id.destPoint)
                .setVisibility(View.VISIBLE);

        source = new LatLng(source.latitude + 0.001, source.longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(source, 14.9f));

        setMessage("Tap on Destination location");
    }


    public void selectDest(View view) {
        launcher.setDest(mMap.getCameraPosition().target);
        enableReqBtn();

        markers.add(mMap.addMarker(new MarkerOptions()
                .icon(Util.bitmapDescriptorFromVector(this,R.drawable.ic_destination)).position(mMap.getCameraPosition().target)
                .title("Destination")));
        findViewById(R.id.destPoint)
                .setVisibility(View.GONE);

        setMessage("Ready! ");
    }

    public void launchCabin(View view) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.cabin.passenger");
        if (launchIntent != null) {
            Bundle data= new Bundle();

            data.putDouble("source_lat" , launcher.getSource().latitude);
            data.putDouble("source_lng" , launcher.getSource().longitude);

            data.putDouble("dest_lat" , launcher.getDest().latitude);
            data.putDouble("dest_lng" , launcher.getDest().longitude);

            data.putDouble("sec_dest_lat" , launcher.getSec_dest().latitude);
            data.putDouble("sec_dest_lng" , launcher.getSec_dest().longitude);


            // value = 0 : disable //  value = 1 enable
            data.putBoolean("two_way" ,launcher.isTwo_way());

            // value = 0 : disable //  value = 1 enable
            data.putInt("wait_type" , launcher.getWait_type());

            data.putInt("vehicle_type",launcher.getVehicle_type());

            launchIntent.putExtra("ride_data" , data);
            startActivity(launchIntent);

        }else {
            setMessage("Cabin passenger app not found!");

        }
    }

    private void enableReqBtn(){
        findViewById(R.id.request)
                .animate().alpha(1)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        boundMap(launcher.getSource(),launcher.getDest());
    }

    private void disableReqBtn(){
        findViewById(R.id.request)
                .animate().alpha(0)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    @Override
    public void onBackPressed() {
        if (launcher.getDest().longitude != 0){
            markers.get(1).remove();
            markers.remove(1);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(launcher.getDest(), 14.9f));
            launcher.setDest(new LatLng(0,0));
            disableReqBtn();

            findViewById(R.id.destPoint)
                    .setVisibility(View.VISIBLE);

        } else if (launcher.getSource().longitude != 0){
            markers.get(0).remove();
            markers.remove(0);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(launcher.getSource(), 14.9f));
            launcher.setSource(new LatLng(0,0));

            findViewById(R.id.sourcePoint)
                    .setVisibility(View.VISIBLE);
            findViewById(R.id.destPoint)
                    .setVisibility(View.GONE);
        }else {

            super.onBackPressed();

        }
    }

    private void boundMap(LatLng first , LatLng sec){
// Gets screen size
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(first).include(sec);

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build() , width, height, 100));
    }
}
