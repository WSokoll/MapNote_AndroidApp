package edu.agh.mapnote_androidapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity{
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private DbHelper dbHelper;
    private List<Note> noteList;
    private MyLocationNewOverlay mLocationOverlay;
    private Double latitude, longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //inflate and create the map
        setContentView(R.layout.activity_map);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);

        //get current latitude and longitude from MainActivity
        Bundle extras = getIntent().getExtras();
        this.latitude = (Double) extras.get("CURRENT_LATITUDE");
        this.longitude = (Double) extras.get("CURRENT_LONGITUDE");

        //getting Notes from db
        dbHelper = new DbHelper(this);
        noteList = dbHelper.getAllNotes();

        //setting icon showing current location
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        this.mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);

        IMapController mapController = map.getController();
        mapController.setZoom(10d);
        mapController.setCenter(new GeoPoint(latitude, longitude));


        //setting pins on map for each note
        for (Note note : noteList) {
            //creating Geopoint
            GeoPoint point = new GeoPoint(note.getLatitude(), note.getLongitude());

            Toast.makeText(this, point.toString(), Toast.LENGTH_SHORT);
            Marker marker = new Marker(map);
            marker.setTitle(note.getNoteContent());
            marker.setIcon(null);
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(marker);

        }
        //GeoPoint startPoint = new GeoPoint(50.06738875352999, 19.916020749346657);
        //GeoPoint labelPoint = new LabelledGeoPoint(50.06738875352999,19.916020749346657, "Teleinfa");
        //mapController.setCenter(startPoint);
        //Marker startMarker = new Marker(map);
        //startMarker.setPosition(labelPoint);
        //startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        //startMarker.setTextIcon("B9");
        //map.getOverlays().add(startMarker);

        requestPermissionsIfNecessary(new String[] {
                // if you need to show the current location, uncomment the line below
                // Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ArrayList<String> permissionsToRequest = new ArrayList<>();

        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }

        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    //probably to be deleted, because we need note content for every pin anyway
//    private List<GeoPoint> makePins(List<Note> notes){
//        List<GeoPoint> points = new ArrayList<>();
//        for (Note note : notes) {
//            GeoPoint point = new GeoPoint(note.getLatitude(), note.getLongitude());
//            points.add(point);
//        }
//        return points;
//    }
}