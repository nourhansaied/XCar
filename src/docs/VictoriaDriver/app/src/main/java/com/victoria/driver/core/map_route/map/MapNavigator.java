package com.victoria.driver.core.map_route.map;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.location.Location;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.SphericalUtil;
import com.victoria.driver.util.LocationManager;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by hlink21 on 24/3/17.
 */

public class MapNavigator implements Consumer<Location> {

    private double length = 0;

    private Disposable disposable;
    private LocationManager locationManager;
    private TypeEvaluator<LatLng> latLngTypeEvaluator = new TypeEvaluator<LatLng>() {
        @Override
        public LatLng evaluate(float v, LatLng fromLng, LatLng toLatLng) {
            return SphericalUtil.interpolate(fromLng, toLatLng, v);
        }
    };
    public Location lastLocation;
    private float lastHead;
    private GoogleMap googleMap;
    private Marker currentMarker;

    private LatLng startingLatLng;

    private boolean isNavigationMpde = true;

    private PathUpdateListener pathUpdateListener;


    public void setNavigationMpde(boolean navigationMpde) {
        isNavigationMpde = navigationMpde;
    }

    public void setStartingLatLng(LatLng startingLatLng) {
        this.startingLatLng = startingLatLng;
    }

    public MapNavigator(LocationManager locationManager, GoogleMap googleMap) {
        this.locationManager = locationManager;
        this.googleMap = googleMap;
    }

    public void startNavigation() {
        if (disposable == null)
            disposable = locationManager.subject.subscribe(this);
        locationManager.triggerLocation(new LocationManager.LocationListener() {
            @Override
            public void onLocationAvailable(LatLng latLng) {

            }

            @Override
            public void onFail(Status status) {

            }
        });
    }

    public void stopNavigation() {
        if (disposable != null)
            disposable.dispose();

        disposable = null;
    }

    public void setCurrentMarker(Marker currentMarker) {
        this.currentMarker = currentMarker;
        this.currentMarker.setFlat(true);
        this.currentMarker.setAnchor(0.5f, 0.5f);


        CameraPosition build = CameraPosition.builder()
                .bearing(currentMarker.getRotation())
                .target(currentMarker.getPosition())
                .zoom(17).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(build));
        lastHead = googleMap.getCameraPosition().bearing;

//        this.currentMarker.setFlat(true);

//        this.currentMarker.setAnchor(0.5f, 0.5f);

       /* CameraPosition build = CameraPosition.builder()
                .bearing(currentMarker.getRotation())
                .target(currentMarker.getPosition())
                .zoom(17).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(build));
        lastHead = googleMap.getCameraPosition().bearing;*/

        //startingLatLng = currentMarker.getPosition();
    }

    public void addMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        // currentMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).position(latLng));
        currentMarker.setPosition(latLng);
        currentMarker.setFlat(true);
        this.currentMarker.setAnchor(0.5f, 0.5f);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        lastHead = googleMap.getCameraPosition().bearing;
    }

    @Override
    public void accept(@NonNull Location location) throws Exception {
        if (currentMarker == null)
            addMarker(location);

        if (lastLocation == null || lastLocation.getLatitude() != location.getLatitude()) {
            animate(location);
            lastLocation = location;
        }
    }


    private LatLng locationToLatLng(Location location) {
        if (location != null)
            return new LatLng(location.getLatitude(), location.getLongitude());
        else return new LatLng(0.0, 0.0);
    }


    private void animate(Location location) {

        if (lastLocation == null) {
            return;
        }

        final LatLng from = locationToLatLng(lastLocation);
        final LatLng to = locationToLatLng(location);

        ValueAnimator valueAnimator = ValueAnimator.ofObject(latLngTypeEvaluator, from, to);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LatLng newLatLng = (LatLng) valueAnimator.getAnimatedValue();

                currentMarker.setPosition(newLatLng);

                if (isNavigationMpde) {
                    CameraPosition lastCameraPosition = googleMap.getCameraPosition();

                    CameraPosition cameraPosition = CameraPosition.builder(lastCameraPosition)
                            .target(newLatLng)
                            .build();

                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                try {
                    if (!isNavigationMpde) {
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(to);
                        builder.include(startingLatLng);
                        LatLngBounds bounds = builder.build();
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 70);
                        googleMap.animateCamera(cu);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        valueAnimator.start();

        final float bearingTo = (float) SphericalUtil.computeHeading(from, to);
        ValueAnimator bearingAnimator = ValueAnimator.ofFloat(lastHead, bearingTo);
        bearingAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        bearingAnimator.setDuration(400);
        bearingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                float value = (float) valueAnimator.getAnimatedValue();
                currentMarker.setRotation(value);
                if (isNavigationMpde) {
                    CameraPosition cameraPosition = CameraPosition.builder(googleMap.getCameraPosition())
                            .bearing(value)
                            .build();

                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });
        bearingAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                lastHead = bearingTo;
            }
        });
        bearingAnimator.start();

        if (pathUpdateListener != null)
            pathUpdateListener.updatePath(to);
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setPathUpdateListener(PathUpdateListener pathUpdateListener) {
        this.pathUpdateListener = pathUpdateListener;
    }

}