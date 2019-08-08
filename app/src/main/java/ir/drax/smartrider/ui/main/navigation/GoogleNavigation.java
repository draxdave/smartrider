package ir.drax.smartrider.ui.main.navigation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.drax.smartrider.data.db.model.NavigateModel;
import ir.drax.smartrider.R;
import ir.drax.smartrider.data.network.model.GoogleDirectionResponse;
import ir.drax.smartrider.ui.base.BaseFragment;
import ir.drax.smartrider.utils.helper.ImageUtil;
import ir.drax.smartrider.ui.main.MainActivity;

public class GoogleNavigation extends BaseFragment implements OnMapReadyCallback, A2F_NavigationInteraction {

    private F2A_NavigationInteraction f2A_navigationInteraction;
    private GoogleMap mMap;
    private boolean locationDetected = false;
    private NavigateModel launcher = new NavigateModel();
    private ArrayList<Marker> markers=new ArrayList<>();

    private LatLng myLocation;
    @BindView(R.id.message) TextView message;
    @BindView(R.id.sourcePoint) View sourcePoint;
    @BindView(R.id.destPoint) View destPoint;
    @BindView(R.id.request) View request;

    public GoogleNavigation() {
        // Required empty public constructor
    }

    public static GoogleNavigation newInstance() {
        GoogleNavigation fragment = new GoogleNavigation();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).setA2FNavigationInteraction(this);
        f2A_navigationInteraction.hideAppbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // setMessage("Searching for location ...");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION} ,100);

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                // Add a marker in Sydney and move the camera
                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                setMessage("You're here!");
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14.9f));

                if (!locationDetected) {
                    locationDetected = true;


                    sourcePoint.setVisibility(View.VISIBLE);
                    sourcePoint.animate()
                            .alpha(1)
                            .start();

                    setMessage("Tap on Destination location");
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof F2A_NavigationInteraction) {
            f2A_navigationInteraction = (F2A_NavigationInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement F2A_NavigationInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        f2A_navigationInteraction = null;
    }

    @Override
    protected void setUp(View view) {

    }

    private void setMessage(String m){
        message.setVisibility(View.VISIBLE);
        message.setAlpha(1);
        message.setText(m);
        message.animate().alpha(0)
                .setStartDelay(1000)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

    }

    @OnClick(R.id.sourcePoint) void selectSource() {

        if (!locationDetected)return;
        LatLng destination= mMap.getCameraPosition().target;
        launcher.setSource(myLocation);
        launcher.setDest(destination);

        markers.add( mMap.addMarker(new MarkerOptions()
                .icon(ImageUtil.bitmapDescriptorFromVector(getContext(),R.drawable.ic_source)).position(destination)
                .title("Destination")));

        
        launcher.setDest(mMap.getCameraPosition().target);
        enableReqBtn();

        markers.add(mMap.addMarker(new MarkerOptions()
                .icon(ImageUtil.bitmapDescriptorFromVector(getContext(),R.drawable.ic_destination)).position(mMap.getCameraPosition().target)
                .title("Destination")));
        destPoint
                .setVisibility(View.GONE);

        setMessage("Enquiring rout ..");

        f2A_navigationInteraction.getGoogleDirection(getGoogleParams());
    }

    private String getGoogleParams() {
        return "origin="+launcher.getSource()+"&destination="+launcher.getDest()+"&key="+getString(R.string.google_api_key);
    }

    private void enableReqBtn(){
        request
                .animate().alpha(1)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();

        boundMap(launcher.getSource(),launcher.getDest());
    }

    private void disableReqBtn(){
        request
                .animate().alpha(0)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }


    public boolean onBackPressed() {
        if (launcher.getDest().longitude != 0){
            markers.get(1).remove();
            markers.remove(1);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(launcher.getDest(), 14.9f));
            launcher.setDest(new LatLng(0,0));
            disableReqBtn();

            destPoint
                    .setVisibility(View.VISIBLE);

        } else if (launcher.getSource().longitude != 0){
            markers.get(0).remove();
            markers.remove(0);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(launcher.getSource(), 14.9f));
            launcher.setSource(new LatLng(0,0));

            sourcePoint
                    .setVisibility(View.VISIBLE);
            destPoint
                    .setVisibility(View.GONE);
        }else {

            return true;

        }
        return false;
    }

    @Override
    public void onMapReady() {
        onMapReady(mMap);
    }

    @Override
    public void setGoogleWaypoints(GoogleDirectionResponse googleWaypoints) {

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
