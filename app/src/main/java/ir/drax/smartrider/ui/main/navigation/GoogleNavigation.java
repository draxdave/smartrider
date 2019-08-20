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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.drax.smartrider.data.db.model.NavigateModel;
import ir.drax.smartrider.R;
import ir.drax.smartrider.data.network.model.GoogleDirectionResponse;
import ir.drax.smartrider.data.network.model.mapir.Intersections;
import ir.drax.smartrider.data.network.model.mapir.Leg;
import ir.drax.smartrider.data.network.model.mapir.MapirDirectionResponse;
import ir.drax.smartrider.data.network.model.mapir.Route;
import ir.drax.smartrider.data.network.model.mapir.Step;
import ir.drax.smartrider.ui.base.BaseFragment;
import ir.drax.smartrider.ui.main.navigation.interfaces.A2F_NavigationInteraction;
import ir.drax.smartrider.ui.main.navigation.interfaces.F2A_NavigationInteraction;
import ir.drax.smartrider.ui.main.navigation.interfaces.NavigationStateChangeListener;
import ir.drax.smartrider.ui.main.navigation.interfaces.fromLocationApi;
import ir.drax.smartrider.utils.helper.ImageUtil;
import ir.drax.smartrider.ui.main.MainActivity;
import ir.drax.smartrider.utils.helper.LatLngInterpolator;
import ir.drax.smartrider.utils.helper.MapUtil;
import ir.drax.smartrider.utils.helper.MarkerAnimation;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class GoogleNavigation extends BaseFragment implements OnMapReadyCallback, A2F_NavigationInteraction {

    private F2A_NavigationInteraction f2A_navigationInteraction;
    private GoogleMap mMap;
    private boolean locationDetected = false;
    private NavigateModel launcher;
    private ArrayList<Marker> markers=new ArrayList<>();
    private long ViewTouchedAt;
    private PolylineOptions line;
    private LatLngBounds.Builder bounder;
    private Marker myMarker;

    private Location myLocation;
    @BindView(R.id.message) TextView message;
    @BindView(R.id.destinationView) View destinationView;
    @BindView(R.id.request) View letsGo;
    private Locationing locationing;

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

        f2A_navigationInteraction.hideAppbar();
        showLoading();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        setMessage("Searching for location ...");
        launcher=new NavigateModel(new NavigationStateChangeListener() {
            @Override
            public void onNone() {
                destinationView.setVisibility(View.VISIBLE);
                destinationView.animate()
                        .alpha(1)
                        .start();
                setMessage("Tap on Destination location");
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( myLocation.getLatitude(),myLocation.getLongitude()), Zoom.Normal));

                mMap.clear();
                markers.clear();
                line=null;
                showHideLetsGO(false);
            }

            @Override
            public void onSelected() {
                destinationView.animate()
                        .alpha(0)
                        .withEndAction(() -> destinationView.setVisibility(View.GONE))
                        .start();

                markers.add(mMap.addMarker(new MarkerOptions()
                        .icon(ImageUtil.bitmapDescriptorFromVector(getContext(),R.drawable.ic_destination))
                        .position(launcher.getDest())
                        .title("Destination")));

                markers.add(mMap.addMarker(new MarkerOptions()
                        .icon(ImageUtil.bitmapDescriptorFromVector(getContext(),R.drawable.ic_origin))
                        .position(launcher.getOrigin())
                        .title("Origin")));

                boundMap(launcher.getOrigin(),launcher.getDest());
            }

            @Override
            public void onReady() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounder.build(),40));
                showHideLetsGO(true);
            }

            @Override
            public void onStarted() {
                showHideLetsGO(false);
                CameraFollow(new LatLng( myLocation.getLatitude(),myLocation.getLongitude()),launcher.getOrigin());
            }

            @Override
            public void onReset() {
                onNone();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (locationing != null) {
            locationing.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // stop location updates
        if (locationing!=null) {
            locationing.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION} ,100);

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(() -> {
            double distanceLat = mMap.getCameraPosition().target.latitude - myLocation.getLatitude();
            double distanceLng = mMap.getCameraPosition().target.longitude- myLocation.getLongitude();
            if (distanceLat<0)distanceLat=distanceLat*-1;
            if (distanceLng<0)distanceLng=distanceLng*-1;

            if (distanceLat < 1.397106078755428E-4 && distanceLng < 1.397106078755428E-4 ){
                CameraPosition.Builder position = new CameraPosition.Builder(mMap.getCameraPosition());
                if (mMap.getCameraPosition().zoom>Zoom.Normal)
                    position.zoom(Zoom.Normal);
                else
                    position.zoom(Zoom.Maximize);

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position.build()));
                return true;
            }


            return false;
        });

        getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ViewTouchedAt =System.currentTimeMillis();
                return false;
            }
        });

        locationing=Locationing.getInstance(getContext());
        locationing.setFromLocationApi(new fromLocationApi() {
            @Override
            public void onLocationMessage(String message) {
                showMessage(message);
            }

            @Override
            public void onLocationReceived(Location newLocation) {
                if (locationDetected) {
                    MarkerAnimation.animateMarkerToICS(myMarker, 1000, new LatLng(newLocation.getLatitude(), newLocation.getLongitude()), new LatLngInterpolator.Spherical());
                    MarkerAnimation.rotateMarker(myMarker, (float) MapUtil.getBearing(myLocation.getLatitude(), myLocation.getLongitude(), newLocation.getLatitude(), newLocation.getLongitude()), mMap);
                }
                if (launcher.getState()== NavigateModel.STATE_READY
                        || launcher.getState()== NavigateModel.STATE_SELECTED){


                }else if(launcher.getState() == NavigateModel.STATE_STARTED){
                    //Display speed
                    //move camera
                    //navigateNextStep();
                    /*CameraFollow(new LatLng(line.getPoints().get(launcher.getNavStep()).latitude,line.getPoints().get(launcher.getNavStep()).longitude)
                            , new LatLng(line.getPoints().get(launcher.getNavStep()+1).latitude,line.getPoints().get(launcher.getNavStep()+1).longitude));*/
                    CameraFollow(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())
                            , new LatLng(newLocation.getLatitude(), newLocation.getLongitude()));

                    myLocation = newLocation;

                }else if (!locationDetected) {
                    myLocation = newLocation;

                    //.flat(true);
                    locationDetected = true;
                    launcher.setState(NavigateModel.STATE_NONE);
                    hideLoading();
                    myMarker=mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                            //.zIndex(12)
                            .rotation(newLocation.getBearing())
                            .icon(ImageUtil.bitmapDescriptorFromVector(getContext(),R.drawable.ic_navigation_img))
                            .title("موقعیت کنونی شما"));



                }else if (System.currentTimeMillis()- ViewTouchedAt >6000){

                    //myMarker.setPosition(new LatLng( newLocation.getLatitude(),newLocation.getLongitude()));*/

                   // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( newLocation.getLatitude(),newLocation.getLongitude()), Zoom.Normal));
                }

                myLocation = newLocation;
            }
        });
        locationing.connect();
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

    @OnClick(R.id.destinationView) void selectDestination() {

        if (!locationDetected)return;

        LatLng destination= mMap.getCameraPosition().target;

        launcher.setOrigin(new LatLng( myLocation.getLatitude(),myLocation.getLongitude()));
        launcher.setDest(destination);
        launcher.setState(NavigateModel.STATE_SELECTED);

        requestRoutes();
    }

    private void requestRoutes(){
        setMessage("Requesting for route ..");
        showLoading();
        f2A_navigationInteraction.getMapirDirection(getMapirApiParams());
    }

    private String getMapirApiParams() {
        return launcher.getOrigin().longitude+","+launcher.getOrigin().latitude+";"+launcher.getDest().longitude+","+launcher.getDest().latitude+"?steps=true";
    }

    private String getGoogleApiParams() {
        return "origin="+launcher.getOrigin()+"&destination="+launcher.getDest()+"&key="+getString(R.string.google_api_key);
    }


    public boolean onBackPressed() {
       /* if (launcher.getState().equals(NavigateModel.STATE_STARTED)){


        }else */
        if (launcher.getState()>0){

            launcher.prevState();
            //launcher.reset();

        }else {

            return true;

        }
        return false;
    }

    private void showHideLetsGO(boolean show){
        if (show){
            letsGo.setVisibility(View.VISIBLE);
            letsGo.animate()
                    .translationY(1)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();

        }else
            letsGo.animate()
                    .translationY(-1)
                    .setInterpolator(new DecelerateInterpolator())
                    .withEndAction(()-> letsGo.setVisibility(View.GONE))
                    .start();


    }

    @Override
    public void onMapReady() {
        onMapReady(mMap);
    }

    @Override
    public void setGoogleWaypoints(GoogleDirectionResponse googleWaypoints) {
        hideLoading();
        launcher.setState(NavigateModel.STATE_SELECTED);
    }

    @Override
    public void setMapirWaypoints(MapirDirectionResponse mapirDirectionResponse) {
        hideLoading();

        bounder= new LatLngBounds.Builder();
        for (Route route : mapirDirectionResponse.getRoutes()) {
            line = new PolylineOptions();

            for (Leg leg : route.getLegs()) {
                for (Step step : leg.getSteps()) {
                    for (Intersections intersection : step.getIntersections()) {
                        line.add(new LatLng(intersection.getLocation()[1],intersection.getLocation()[0]));
                        bounder.include(new LatLng(intersection.getLocation()[1],intersection.getLocation()[0]));
                    }
                }

            }

            line.width(23);
            mMap.addPolyline(line);
        }

        if (launcher.getState() == NavigateModel.STATE_SELECTED){
            launcher.setState(NavigateModel.STATE_READY);
        }else
            CameraFollow(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()),launcher.getOrigin());
    }

    @OnClick(R.id.request) void letsGo() {
        launcher.setState(NavigateModel.STATE_STARTED);
    }

    private void CameraFollow(LatLng from , LatLng to){
        double atoB=MapUtil.distance(from.latitude,from.longitude,1,to.latitude,to.longitude,1);
        double metoB=MapUtil.distance(myLocation.getLatitude(),myLocation.getLongitude(),1
                ,to.latitude,to.longitude,1);
        double metoA=MapUtil.distance(myLocation.getLatitude(),myLocation.getLongitude(),1
                ,from.latitude,from.longitude,1);

        //if ((metoA + metoB) - atoB > atoB/3){
        if ((metoA + metoB) - atoB > 1000){

            launcher.setOrigin(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()));
            requestRoutes();
            return;

        }else if(metoB<10){
            // launcher.nexcctStep();
        }



        CameraPosition.Builder CPB= new CameraPosition.Builder();
        CPB.zoom(23)
                .bearing((float) MapUtil.getBearing(from.latitude,from.longitude,to.latitude,to.longitude))
                .tilt(90)
                .target(to);

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CPB.build()));
    }

    private void boundMap(LatLng first , LatLng sec){
// Gets screen size
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(first).include(sec);

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build() , width, height, 100));
    }

    @Override
    public void onError(String message) {
        hideLoading();
        super.onError(message);
        onBackPressed();
    }

    private float computeAngleBetween(double fromLat, double fromLng, double toLat, double toLng) {
        // Haversine's formula
        double dLat = fromLat - toLat;
        double dLng = fromLng - toLng;
        return (float) (2 * asin(sqrt(pow(sin(dLat / 2), 2) +
                cos(fromLat) * cos(toLat) * pow(sin(dLng / 2), 2))));
    }
}
