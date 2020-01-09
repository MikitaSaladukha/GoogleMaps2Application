package com.example.googlemaps2application;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
//import android.support.annotation.NonNull;
//import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v7.app.AppCompatActivity;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;
import com.parrot.arsdk.ARSDK;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_KEYTYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_TYPE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DICTIONARY_KEY_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_ERROR_ENUM;
import com.parrot.arsdk.arcontroller.ARControllerArgumentDictionary;
import com.parrot.arsdk.arcontroller.ARControllerCodec;
import com.parrot.arsdk.arcontroller.ARControllerDictionary;
import com.parrot.arsdk.arcontroller.ARControllerException;
import com.parrot.arsdk.arcontroller.ARDeviceController;
import com.parrot.arsdk.arcontroller.ARDeviceControllerListener;
import com.parrot.arsdk.arcontroller.ARDeviceControllerStreamListener;
import com.parrot.arsdk.arcontroller.ARFeatureWifi;
import com.parrot.arsdk.arcontroller.ARFrame;
import com.parrot.arsdk.ardiscovery.ARDISCOVERY_PRODUCT_ENUM;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDevice;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;
import com.parrot.arsdk.ardiscovery.ARDiscoveryException;
import com.parrot.arsdk.ardiscovery.ARDiscoveryService;
import com.parrot.arsdk.ardiscovery.receivers.ARDiscoveryServicesDevicesListUpdatedReceiver;
import com.parrot.arsdk.ardiscovery.receivers.ARDiscoveryServicesDevicesListUpdatedReceiverDelegate;
import com.parrot.arsdk.arsal.ARSALPrint;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends AppCompatActivity /*FragmentActivity*/ implements OnMapReadyCallback, ARDeviceControllerListener, ARDeviceControllerStreamListener

         {


             private static int iDel=-1;

    private class LongPressLocationSource implements LocationSource, GoogleMap.OnMapLongClickListener {

        private OnLocationChangedListener mListener;

        /**
         * Flag to keep track of the activity's lifecycle. This is not strictly necessary in this
         * case because onMapLongPress events don't occur while the activity containing the map is
         * paused but is included to demonstrate best practices (e.g., if a background service were
         * to be used).
         */
        private boolean mPaused;

        @Override
        public void activate(OnLocationChangedListener listener) {
            mListener = listener;
        }

        @Override
        public void deactivate() {
            mListener = null;
        }

        TextView infoText1;

        @Override
        public void onMapLongClick(LatLng point) {


                Lat.add(point.latitude);
                Lng.add(point.longitude);

                MarkerOptions marker = new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title(Integer.toString(Lat.size()));
                markers.add(marker);
                Marker marker11;

                marker11= mMap.addMarker(marker);


                marker11.showInfoWindow();

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(point.latitude, point.longitude)));
                markers1.add(marker11);
                iDel=markers1.size();


            infoText1=MapsActivity.this.findViewById(R.id.infoText);
            infoText1.setText("latitude: "+point.latitude+"\n"+ "longitude: "+point.longitude);


        }

        public void onPause() {
            mPaused = true;
        }

        public void onResume() {
            mPaused = false;
        }
    }

    private static ArrayList<Marker> markers1 = new ArrayList<Marker>();
    private static ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();
    private static ArrayList<Double> Lat = new ArrayList<Double>();
    private static ArrayList<Double> Lng = new ArrayList<Double>();
    private static GoogleMap mMap;
    private final String MY_TAG="myTag";
    private Joystick joystick, joystick2;



    private Button tolBtn, startBtn, stopBtn,uavBtn, delSelBtn, initBtn, clearBtn;//, stopUpgrdBtn;


    TextView infoText, keyText, withPeriodText;
    SupportMapFragment mapFragment;
    Button showPathBtn;
    CheckBox encrypChB;
    EditText periodEditText;


    private LongPressLocationSource mLocationSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mLocationSource = new LongPressLocationSource();
        ARSDK.loadSDKLibs();
        infoText=findViewById(R.id.infoText);
        keyText=findViewById(R.id.keyText);
        showPathBtn=findViewById(R.id.showPathBtn);

        tolBtn=findViewById(R.id.tolBtn);
        tolBtn.setEnabled(false);
        //conBtn=findViewById(R.id.conBtn);
        //conBtn.setEnabled(false);

        startBtn=findViewById(R.id.startBtn);
        startBtn.setEnabled(false);
        stopBtn=findViewById(R.id.stopBtn);
        stopBtn.setEnabled(false);
        uavBtn=findViewById(R.id.uavBtn);
        uavBtn.setEnabled(false);


        encrypChB=findViewById(R.id.encrypChB);
        encrypChB.setChecked(true);
            withPeriodText=findViewById(R.id.withPeriodText);
            periodEditText=findViewById(R.id.periodEditText);
        //stopUpgrdBtn=findViewById(R.id.stopUpgrdBtn);
        //stopUpgrdBtn.setEnabled(false);


        delSelBtn = findViewById(R.id.delSelBtn);

        initBtn=findViewById(R.id.InitBtn);

        joystick = (Joystick) findViewById(R.id.joystick);

        joystick.setJoystickListener(new JoystickListener() {
            @Override
            public void onDown() {

                uav.setPressed(true);
                if (!uav.getStopped() && uav.isBigStart() && !uav.getPressed2())
                    uav.stop();

                if (!uav.getPressed2())
                    uav.move();


                // ..
            }

            @Override
            public void onDrag(float degrees, float offset) {
                // ..
                byte gaz=(byte)Math.round(100*offset*Math.sin(Math.PI*degrees/180));
                byte roll=(byte)Math.round(100*offset*Math.cos(Math.PI*degrees/180));
                uav.setGaz(gaz);
                uav.setRoll((byte) 0);
                infoText.setText("up: "+gaz+" shift: "+roll);
            }

            @Override
            public void onUp() {

                uav.setGaz((byte)0);
                uav.setRoll((byte)0);
                uav.setPressed(false);

                if (!uav.getPressed2()) {
                    uav.getCoordinates();
                    if (uav.isBigStart()) {
                        infoText.setText("onUp: , iCurr=" + uav.getiCurr());
                        uav.setiStart(uav.getiCurr());
                        onMyStart();

                        //uav.bigStart=false;
                    }
                }

                // ..
            }
        });

        //////////////////////////////////////////////////////////
        joystick2 = (Joystick) findViewById(R.id.joystick2);

        joystick2.setJoystickListener(new JoystickListener() {
            @Override
            public void onDown() {

                uav.setPressed2(true);
                if (!uav.getStopped() && uav.isBigStart() && !uav.getPressed())
                    uav.stop();

                if (!uav.getPressed())
                    uav.move();


                // ..
            }

            @Override
            public void onDrag(float degrees, float offset) {
                // ..
                byte pitch=(byte)Math.round(100*offset*Math.sin(Math.PI*degrees/180));
                byte yaw=(byte)Math.round(100*offset*Math.cos(Math.PI*degrees/180));
                uav.setYaw(yaw);
                uav.setPitch(pitch);
                infoText.setText("forward: "+pitch+" turn: "+yaw);
            }

            @Override
            public void onUp() {

                uav.setYaw((byte)0);
                uav.setPitch((byte)0);
                uav.setPressed2(false);

                if (!uav.getPressed()) {
                    uav.getCoordinates();
                    if (uav.isBigStart()) {
                        infoText.setText("onUp: , iCurr=" + uav.getiCurr());
                        uav.setiStart(uav.getiCurr());
                        onMyStart();

                        //uav.bigStart=false;
                    }
                }

                // ..
            }
        });

        joystick.setEnabled(false);
        joystick2.setEnabled(false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        googleMap.setLocationSource(mLocationSource);
        googleMap.setOnMapLongClickListener(mLocationSource);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(listener);
        CameraPosition cameraPosition1 = new CameraPosition.Builder()
                .target(new LatLng(Olat,Olng))
                .zoom(17)
                .build();

        mMap.moveCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition1));
        //mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
    }



             GoogleMap.OnMarkerClickListener listener = new
                     GoogleMap.OnMarkerClickListener() {

                         @Override
                         public boolean onMarkerClick(final Marker marker) {
                             marker.showInfoWindow();
                             if (marker==uavMarker)
                                 iDel = -1;
                             else
                                 iDel= Integer.parseInt(marker.getTitle().toString());

                             return true;

                         }

                     };


    private static int markerCounter=0;




    PolylineOptions path=new PolylineOptions();
    Polyline pathP;
    Timer T;
    private  int timesOfrepaint=0;

    private void onMyStart(){
        try {
            //stopPressed=false;
            path = new PolylineOptions();
            path.color(Color.BLUE);

            if (pathP != null) pathP.remove();

            if (uavMarker != null) {
                path.add(uavMarker.getPosition());
                for (int i = 0; i < Lat.size(); i++) {
                    path.add(markers1.get(i).getPosition());
                }
                pathP = mMap.addPolyline(path);
            }

            if (uav == null | Lat == null | Lng == null | Lat.size() == 0 | Lng.size() == 0) return;

            uav.start(Lat, Lng);

            showPathBtn.setText("Hide path");
            pathShown = true;
            stopBtn.setEnabled(true);
            startBtn.setEnabled(false);
            //upBtn.setEnabled(false);
            //downBtn.setEnabled(false);
            uavBtn.setEnabled(false);
            tolBtn.setEnabled(false);
            delSelBtn.setEnabled(false);

            //conBtn.setEnabled(false);
            initBtn.setEnabled(false);

            withEnryp=encrypChB.isChecked();
            encrypChB.setEnabled(false);
            periodEditText.setEnabled(false);


            infoText.setText("Started mission");


        } catch (Exception e){
            uav.error+=e.toString();
            uav.stop();
            onMyStop();
        }
    }

    Timer Tupdate;
    boolean updating=false;
    boolean withEnryp=true;


    public void onStart(View v){
        uav.setBigStart(true);
        uav.setiStart(1);
        onMyStart();

        T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getBaseContext(),"size Lng: "+Lng.size(),Toast.LENGTH_SHORT).show();
                        //infoText.setText("timesOfrepaint: " + timesOfrepaint);
                        timesOfrepaint++;
                        repaintUAVmarker();
                        if (!uav.isBigStart()) {
                            onMyStop();

                        }

                    }
                });
            }
        }, 1000, 1000);

        if (withEnryp){
            long periodEncryp=40000;
            try {
                periodEncryp = (long)(1000*Integer.parseInt(periodEditText.getText().toString()));
            } catch (Exception e){
                periodEditText.setText("40");
            }
            if (periodEncryp<1000) {
                periodEncryp=1000;
                periodEditText.setText("1");
            }

            Tupdate = new Timer();
            Tupdate.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!uav.isBigStart()) {
                                onMyStop();
                                return;
                            }

                            if (!updating)
                                update2();

                        }
                    });
                }
            }, 2000, periodEncryp);
        }


    }




             private synchronized void  repaintUAVmarker(){
                try {
                    if (uav == null) return;
                    uav.getCoordinates();
                    if (uavMarker.getPosition().latitude != uav.lat | uavMarker.getPosition().longitude != uav.lng) {
                        uavMarker.remove();
                        uavMarkerOpt = new MarkerOptions()
                                .position(new LatLng(uav.lat, uav.lng))
                                .title("UAV")
                                //.snippet("and snippet")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        uavMarker = mMap.addMarker(uavMarkerOpt);
                    }
                } catch (Exception e){
                    uav.error+=e.toString();
                    uav.stop();
                    onMyStop();
                }
             }

      public void onMyStop(){
          //if (uav!=null) uav.stop();
          //infoText.setText("error="+error+"\n uav.bigStart="+uav.bigStart+"\n length=0: "+(uav.error.length()==0)+"\n !uav.getPressed()="+(!uav.getPressed())+"\n !stopPressed="+(!stopPressed+"\n i="+uav.getiCurr()));

          /*
          if (uav.bigStart && uav.error.length()==0 && !uav.getPressed() && !stopPressed) {

              if (uav.getiCurr()!=0)
                uav.setiStart(uav.getiCurr()-1);
              else uav.setiStart(0);
              onMyStart();
              infoText.setText("Restarted");
              return;
          }
          */


          if (!uav.isBigStart()) {
              if (T!=null) T.cancel();
              if (Tupdate != null && withEnryp) Tupdate.cancel();

              if (pathP != null) pathP.remove();
              showPathBtn.setText("Show path");
              pathShown=false;
              stopBtn.setEnabled(false);
              startBtn.setEnabled(true);



              uavBtn.setEnabled(true);
              tolBtn.setEnabled(true);
              delSelBtn.setEnabled(true);


              initBtn.setEnabled(true);
              encrypChB.setEnabled(true);
              periodEditText.setEnabled(true);
              infoText.setText("Stopped mission");

          }


      }

      //private boolean stopPressed=false;

    public void onStop(View v){
        uav.stop();
        uav.setBigStart(false);
        //stopPressed=true;
        //onMyStop();
        /*
        if (uav!=null) uav.stop();
        if (T!=null) T.cancel();


        if (pathP != null) pathP.remove();
        showPathBtn.setText("Show path");
        pathShown=false;
        stopBtn.setEnabled(false);
        startBtn.setEnabled(true);
        upBtn.setEnabled(true);
        downBtn.setEnabled(true);
        uavBtn.setEnabled(true);
        tolBtn.setEnabled(true);
        delSelBtn.setEnabled(true);

        conBtn.setEnabled(true);
        initBtn.setEnabled(true);
        if (uav.error!="")
            infoText.setText("Error: "+uav.error);

        */

    }



    Marker uavMarker;
    MarkerOptions uavMarkerOpt;
    private UAV uav=new UAV();;

    public void onUAV(View v){
        try {




            uav.getCoordinates();
            infoText.setText("latitude is  " + uav.lat + "\n longitude is " + uav.lng);

            uavMarkerOpt = new MarkerOptions()
                    .position(new LatLng(uav.lat, uav.lng))
                    .title("UAV")
                    //.snippet("and snippet")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            if (uavMarker != null) uavMarker.remove();
            uavMarker = mMap.addMarker(uavMarkerOpt);
            uavMarker.showInfoWindow();
            CameraPosition cameraPosition1 = new CameraPosition.Builder()
                    .target(uavMarker.getPosition())
                    .zoom(17)
                    .build();
            if  (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING.equals(uav.getPilotingState()))
                startBtn.setEnabled(true);
                mMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition1));

            iDel = -1;



        } catch (Exception e){
            infoText.setText("Error: \n"+e);
        }

    }

    private boolean pathShown=false;
    public void  Show_path(View v){

        if (uav==null){
            Toast.makeText(getBaseContext(), "No UAV!", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (Lat==null | Lng==null | Lat.size()==0 | Lng.size()==0){
            Toast.makeText(getBaseContext(), "No coordinates!", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (!this.pathShown) {
            showPathBtn.setText("Hide path");
            path=new PolylineOptions();
            path.color(Color.BLUE);

            if (pathP!=null) pathP.remove();

            if (uavMarker!=null) {
                path.add(uavMarker.getPosition());
                for (int i = 0; i < Lat.size(); i++) {
                    path.add(markers1.get(i).getPosition());
                }
                pathP = mMap.addPolyline(path);
            }

            pathShown=true;



        } else {
            if (pathP != null) pathP.remove();
            showPathBtn.setText("Show path");
            pathShown=false;
        }
    }


    public void Delete_selected_marker(View v){
        if (markers != null & markers.size() > 0 & iDel!=-1){
            int i=iDel - 1;
            for (int k=i+1;k<markers.size();k++)
                markers1.get(k).setTitle(Integer.toString(k));

            markers1.get(i).remove();
            markers1.remove(i);
            markers.remove(i);
            Lat.remove(i);
            Lng.remove(i);
            //infoText.setText("Deleted marker "+edtMarkerN.getText().toString());
            Toast.makeText(getBaseContext(), "Deleted marker "+iDel, Toast.LENGTH_SHORT)
                    .show();
            iDel=-1;

            return;
        }

        if (markers == null || markers.size()==0){
            //infoText.setText("No markers on map!");
            Toast.makeText(getBaseContext(), "No markers on map!", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if  (iDel==-1){
            //infoText.setText("Enter correct marker number!");
            Toast.makeText(getBaseContext(), "Select marker to delete!", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
    }

    ///from UAV window////////////////////////////////////////////////////////////////////////////////////////
    private final String TAG="parrot Tag";
             private ARDiscoveryService mArdiscoveryService;
             private ServiceConnection mArdiscoveryServiceConnection;



             private synchronized void initDiscoveryService()
             {
                 // create the service connection
                 if (mArdiscoveryServiceConnection == null)
                 {
                     mArdiscoveryServiceConnection = new ServiceConnection()
                     {
                         @Override
                         public void onServiceConnected(ComponentName name, IBinder service)
                         {
                             mArdiscoveryService = ((ARDiscoveryService.LocalBinder) service).getService();

                             startDiscovery();
                         }

                         @Override
                         public void onServiceDisconnected(ComponentName name)
                         {
                             mArdiscoveryService = null;
                         }
                     };
                 }

                 if (mArdiscoveryService == null)
                 {
                     // if the discovery service doesn't exists, bind to it
                     Intent i = new Intent(getApplicationContext(), ARDiscoveryService.class);
                     getApplicationContext().bindService(i, mArdiscoveryServiceConnection, Context.BIND_AUTO_CREATE);
                 }
                 else
                 {
                     // if the discovery service already exists, start discovery
                     startDiscovery();
                 }
             }


             private void startDiscovery()
             {
                 if (mArdiscoveryService != null)
                 {
                     mArdiscoveryService.start();
                 }
             }

             ////Start discovery: end//////////////////////////////
////The libARDiscovery will let you know when BLE and Wifi devices have been found on network://///////////////////////////////////////////////////////
             List<ARDiscoveryDeviceService> deviceList;
             ARDiscoveryServicesDevicesListUpdatedReceiver mArdiscoveryServicesDevicesListUpdatedReceiver;
             private synchronized void registerReceivers()
             {
                 ARDiscoveryServicesDevicesListUpdatedReceiver receiver =
                         new ARDiscoveryServicesDevicesListUpdatedReceiver(mDiscoveryDelegate);
                 LocalBroadcastManager localBroadcastMgr = LocalBroadcastManager.getInstance(getApplicationContext());
                 localBroadcastMgr.registerReceiver(receiver,
                         new IntentFilter(ARDiscoveryService.kARDiscoveryServiceNotificationServicesDevicesListUpdated));

                 mArdiscoveryServicesDevicesListUpdatedReceiver=receiver; ///MikkiTA
             }

             private final ARDiscoveryServicesDevicesListUpdatedReceiverDelegate mDiscoveryDelegate =
                     new ARDiscoveryServicesDevicesListUpdatedReceiverDelegate() {

                         @Override
                         public synchronized void onServicesDevicesListUpdated() {
                             if (mArdiscoveryService != null) {
                                 deviceList = mArdiscoveryService.getDeviceServicesArray();
                                 infoText.setText("deviceList size: "+ deviceList.size());
                                 if (deviceList!=null & deviceList.size()>0) {
                                     //conBtn.setEnabled(true);
                                     ////////////////////////////
                                     /*
                                     Calendar c0=new GregorianCalendar();
                                     Calendar c_curr=new GregorianCalendar();
                                     while (c_curr.getTimeInMillis()-c0.getTimeInMillis()<1000) {
                                         c_curr=new GregorianCalendar();
                                     }
                                    */

                                         Create_discovery_device3();
                                         close_Services4();
                                         Create_Device_Controller5();
                                         add_Listeners6();
                                         deviceController_start7();
                                         uav.setDeviceController(deviceController);
                                         infoText.setText("Device connected!");
                                         tolBtn.setEnabled(true);
                                         //getWifiId0();
                                         connected=true;

                                     //stopUpgrdBtn.setEnabled(true);

                                     ///////////////////////////

                                 }
                                 else {
                                     //conBtn.setEnabled(false);

                                 }
                                 // Do what you want with the device list
                             }
                         }
                     };


             public void Init_And_Register2(View v){
                 try{
                     initDiscoveryService();
                     registerReceivers();
                     mDiscoveryDelegate.onServicesDevicesListUpdated();
////////////////////////////////////////////
                     //while (deviceList.size()==0){};
 /*                    Calendar c0=new GregorianCalendar();
                     Calendar c_curr=new GregorianCalendar();
                     while (c_curr.getTimeInMillis()-c0.getTimeInMillis()<4000) {
                         c_curr=new GregorianCalendar();
                     }


                     Create_discovery_device3(v);
                     close_Services4(v);
                     Create_Device_Controller5(v);
                     add_Listeners6(v);
                     deviceController_start7(v);
                     uav.setDeviceController(deviceController);
                     infoText.setText("Device connected!");
                     tolBtn.setEnabled(true);
*/////////////////////////////////////////////////////////////////////
                 } catch (Exception e){
                     infoText.setText("Error: "+e);
                 }

                 //myText.setText("Button2 pressed! "+myText.getText());
             }
////The libARDiscovery will let you know when BLE and Wifi devices have been found on network: end/////////////////////////////////////////////////////////
///Once you have the ARService you want to use, transform it into an ARDiscoveryDevice///////////////////////////////////////////////////////////
             // Context mContext = getApplicationContext();//(Context) MainActivity.this;

             private ARDiscoveryDevice device;
             private ARDiscoveryDevice createDiscoveryDevice(@NonNull ARDiscoveryDeviceService service) {
                 ARDiscoveryDevice device = null;
                 try {
                     device = new ARDiscoveryDevice(/*mContext*/ this, service);
                     infoText.setText("device: "+device.toString());
                 } catch (ARDiscoveryException e) {
                     Log.e(TAG, "Exception", e);
                     infoText.setText("Exception "+e.toString());
                 }

                 return device;
             }


             public void Create_discovery_device3(){
                 this.device= createDiscoveryDevice(deviceList.get(0));
                 infoText.setText("Button3 pressed! device:"+device.toString());

             }


             //Clean everything://////////////////////////////////////////////////////////////////
             private void unregisterReceivers()
             {
                 LocalBroadcastManager localBroadcastMgr = LocalBroadcastManager.getInstance(getApplicationContext());

                 localBroadcastMgr.unregisterReceiver(mArdiscoveryServicesDevicesListUpdatedReceiver);
             }

             private void closeServices()
             {
                 //Log.d(TAG, "closeServices ...");

                 if (mArdiscoveryService != null)
                 {
                     new Thread(new Runnable() {
                         @Override
                         public void run()
                         {

                             try {
                                 mArdiscoveryService.stop();

                                 getApplicationContext().unbindService(mArdiscoveryServiceConnection);
                                 // mArdiscoveryService = null;                       MikkiTA commented
                                 //  boolean isNull=  mArdiscoveryService == null;      MikkiTA added
                                 // myText.setText("Is Null= "+isNull);               MikkiTA added
                             } catch (Exception e){
                                 infoText.setText("Exception "+e.toString());
                             }
                         }
                     }).start();
                 }
             }

             public void close_Services4(){

                 try {
                     unregisterReceivers();
                     closeServices();
                     infoText.setText("Button4 pressed! ");
                 } catch (Exception e){
                     infoText.setText("Exception "+e.toString());
                 }

             }
             /////Create the device controller:////////////////////////////////////////////////////////////////////////////////////
             ARDeviceController deviceController; //MikkiTA



             public void CreateDeviceController(){
                 ARDiscoveryDeviceService mDeviceService=deviceList.get(0);  //MikkiTA

                 ARDiscoveryDevice discoveryDevice = createDiscoveryDevice(mDeviceService);
                 if (discoveryDevice != null) {
                     try
                     {
                         ARDiscoveryDevice device = createDiscoveryDevice(deviceList.get(0)); //MikkiTA

                         ARDeviceController deviceController = new ARDeviceController(device);
                         this.deviceController=deviceController; //MikkiTA
                         discoveryDevice.dispose();
                         infoText.setText("Created device controller: "+ deviceController.toString()+"\n discoveryDevice: "+ discoveryDevice.toString());
                     }
                     catch (ARControllerException e)
                     {
                         e.printStackTrace();
                         infoText.setText("Exception "+e.toString());
                     }
                 }

             }
             public void Create_Device_Controller5(){

                 try {
                     CreateDeviceController();
                     infoText.setText("Button5 pressed! "+infoText.getText());
                 } catch (Exception e){
                     infoText.setText("Exception "+e.toString());
                 }

             }

             /////Listen to the states changes://////////////////////////////////////////////////////////
             public void addDeviceControllerListener (){  //MikkiTA
                 deviceController.addListener (this);      //MikkiTA
             }                                                //MikkiTA


             @Override
// called when the state of the device controller has changed
             public void onStateChanged (ARDeviceController deviceController, ARCONTROLLER_DEVICE_STATE_ENUM newState, ARCONTROLLER_ERROR_ENUM error)
             {
                 switch (newState)
                 {
                     case ARCONTROLLER_DEVICE_STATE_RUNNING:
                         break;
                     case ARCONTROLLER_DEVICE_STATE_STOPPED:
                         break;
                     case ARCONTROLLER_DEVICE_STATE_STARTING:
                         break;
                     case ARCONTROLLER_DEVICE_STATE_STOPPING:
                         break;

                     default:
                         break;
                 }
             }


             //MikkiTA
             @Override
             public void onExtensionStateChanged(ARDeviceController deviceController, ARCONTROLLER_DEVICE_STATE_ENUM newState, ARDISCOVERY_PRODUCT_ENUM product, String name, ARCONTROLLER_ERROR_ENUM error) {
                 //MikkiTA
             }
             //MikkiTA


             ///////Listen to the commands received from the drone (example of the battery level received)////////////////////////////////////////////////////////

             // called when a command has been received from the drone

             @Override
             public void onCommandReceived(ARDeviceController deviceController, ARCONTROLLER_DICTIONARY_KEY_ENUM commandKey, ARControllerDictionary elementDictionary)
             {


                 if (elementDictionary != null) {
                 /*
                // if the command received is a battery state changed
                if (commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_BATTERYSTATECHANGED) {
                    ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);

                    if (args != null) {
                        Integer batValue = (Integer) args.get(ARFeatureCommon.ARCONTROLLER_DICTIONARY_KEY_COMMON_COMMONSTATE_BATTERYSTATECHANGED_PERCENT);
                        myText.setText("battery level: "+batValue);
                        // do what you want with the battery level
                    }

                }

                if (commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED)
                    {
                        ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                        if (args != null)
                        {
                            Integer flyingStateInt = (Integer) args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE);
                            ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM flyingState = ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.getFromValue(flyingStateInt);
                            myText.setText("flying State: "+flyingState);
                        }
                    }

                if (commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_POSITIONCHANGED){
                    ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                    if (args != null) {
                        double latitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_POSITIONCHANGED_LATITUDE);
                        double longitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_POSITIONCHANGED_LONGITUDE);
                        double altitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_POSITIONCHANGED_ALTITUDE);
                        myText.setText("latitude: "+latitude+"\n"+"longitude: "+longitude+"\n"+"altitude: "+altitude);
                    }
                }

                */

                 /*
                 if ((commandKey == ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_NETWORKSETTINGSSTATE_WIFISECURITY)
                         && (elementDictionary != null) && check) {
                         ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                         if (args != null) {
                             String key = (String) args.get(ARFeatureWifi.ARCONTROLLER_DICTIONARY_KEY_WIFI_SECURITYCHANGED_KEY);
                             ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM key_type = ARCOMMANDS_WIFI_SECURITY_TYPE_ENUM.getFromValue((Integer) args.get(ARFeatureWifi.ARCONTROLLER_DICTIONARY_KEY_WIFI_SECURITYCHANGED_KEY_TYPE));
                             infoText.setText("key: " + key + "type: " + key_type.toString());
                             check = false;
                         }
                     }
                    */

                 } else {
                     Log.e(TAG, "elementDictionary is null");
                     infoText.setText("elementDictionary is null");
                     // myText.setText("elementDictionary is null");
                 }


             }

             ///////Listen to the video stream received from the drone//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
             public void addDeviceControllerStreamListener(){
                 deviceController.addStreamListener(this);
             }

             @Override
             public ARCONTROLLER_ERROR_ENUM configureDecoder(ARDeviceController deviceController, final ARControllerCodec codec) {
                 // configure your decoder
                 // return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK if display went well
                 // otherwise, return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_ERROR. In that case,
                 // configDecoderCallback will be called again

                 return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK; //MikkiTA
             }

             @Override
             public ARCONTROLLER_ERROR_ENUM onFrameReceived(ARDeviceController deviceController, final ARFrame frame) {
                 // display the frame
                 // return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK if display went well
                 // otherwise, return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_ERROR. In that case,
                 // configDecoderCallback will be called again


                 return ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK; //MikkiTA
             }

             @Override
             public void onFrameTimeout(ARDeviceController deviceController) {}


             public void add_Listeners6(){
                 try {
                     addDeviceControllerListener();
                     addDeviceControllerStreamListener();
                     infoText.setText("Listeners set!");
                     infoText.setText("Button6 pressed! "+infoText.getText());
                 } catch (Exception e){
                     infoText.setText("Error: "+e.toString());
                 }

             }
             /////Finally, starts the device controller (after that call, the callback you set in ARCONTROLLER_Device_AddStateChangedCallback should be called).
             ARCONTROLLER_ERROR_ENUM error; //MikkiTA

             public void start(){
                 ARCONTROLLER_ERROR_ENUM error = deviceController.start();
                 boolean success = ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK==error;
                 infoText.setText("success start: "+success);
                 if (!success)
                     infoText.setText("Error: "+error);

                 this.error=error; //MikkiTA

             }

             public void stop(){
                 ARCONTROLLER_ERROR_ENUM error = deviceController.stop();
                 this.error=error; //MikkiTA
                 // only when the deviceController is stopped
                 deviceController.dispose();

             }

             public void deviceController_start7() {
                 start();
                 infoText.setText("Button7 pressed! "+infoText.getText());

             }

             /////Take off////////////////////////////////////////////////////////////////


             private void takeoff()
             {
                 if (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED.equals(uav.getPilotingState()))
                 {
                     ARCONTROLLER_ERROR_ENUM error = deviceController.getFeatureARDrone3().sendPilotingTakeOff();

                     if (!error.equals(ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK))
                     {
                         ARSALPrint.e(TAG, "Error while sending take off: " + error);
                     }
                 }
             }



             public void TakeOff8(View v) {
                 if (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED.equals(uav.getPilotingState()))
                 {
                     takeoff();
                     uavBtn.setEnabled(true);
                     if (uavMarker!=null) startBtn.setEnabled(true);
                     tolBtn.setText("Land");
                     joystick.setEnabled(true);
                     joystick2.setEnabled(true);

                 }

                 if (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING.equals(uav.getPilotingState()))
                 {
                     land();
                     uavBtn.setEnabled(false);
                     startBtn.setEnabled(false);
                     tolBtn.setText("Take off");
                     joystick.setEnabled(false);
                     joystick2.setEnabled(false);
                 }

                 infoText.setText("Take off/Land");
             }




             private void land()
             {
                 ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM flyingState = uav.getPilotingState();
                 if (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING.equals(flyingState))
                 {
                     ARCONTROLLER_ERROR_ENUM error = deviceController.getFeatureARDrone3().sendPilotingLanding();

                     if (!error.equals(ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK))
                     {
                         ARSALPrint.e(TAG, "Error while sending take off: " + error);
                     }
                 }
             }


/*
             public void Alldone(View v){

                 try {
                     Create_discovery_device3();
                     close_Services4();
                     Create_Device_Controller5();
                     add_Listeners6();
                     deviceController_start7();
                     uav.setDeviceController(deviceController);
                     infoText.setText("Device connected!");
                     tolBtn.setEnabled(true);

                 } catch (Exception e){
                     infoText.setText("Error: "+e);
                 }
             }
*/
    final double Olat=53.838219;//48.87889993217496;
    final double Olng=27.475630;//2.367780036369033;
    double tempLat=Olat,tempLon=Olng;

    public void cameraMove(View v){
        CameraPosition cameraPosition1 = new CameraPosition.Builder()
                .target(new LatLng(tempLat,tempLon))
                .zoom(17)
                .build();

        mMap.moveCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition1));

        check=true;
        /*
        ARCONTROLLER_ERROR_ENUM sec= deviceController.getFeatureARDrone3().sendNetworkSettingsWifiSecurity(ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_TYPE_ENUM.ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_TYPE_WPA2,
                "12345678",
                ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_KEYTYPE_ENUM.ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_KEYTYPE_MAX);
        */

                //ARCONTROLLER_ERROR_ENUM res= deviceController.getFeatureCommon().sendSettingsReset();
        //ARCONTROLLER_ERROR_ENUM a=deviceController.getFeatureCommon().sendCommonReboot();
        //String key = ARFeatureWifi.ARCONTROLLER_DICTIONARY_KEY_WIFI_SECURITYCHANGED_KEY;
        //String keyType=   ARFeatureWifi.ARCONTROLLER_DICTIONARY_KEY_WIFI_SECURITYCHANGED_KEY_TYPE;
        //infoText.setText("sec: "+sec.toString()+" reboot: "+a.toString());//+" key: "+key+" ktype: "+keyType);
    }
    boolean check =false;

    public void onUp(View v){
        uav.getCoordinates();
        uav.moveGPS(uav.lat,uav.lng,uav.alt+1);
    }

   public void onDown(View v){
     uav.getCoordinates();
     if (uav.alt>1.5)
     uav.moveGPS(uav.lat,uav.lng,uav.alt-1);
   }

    String ssid="milos_pc_e5a084";
    String key = "ad13254768";
    boolean open=true;
    String cap="";
    String summ="";

             public void getWifiId(View v){
                 WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService (Context.WIFI_SERVICE);
                 WifiInfo info = wifiManager.getConnectionInfo ();
                 ssid  = info.getSSID();
                 //WifiConfiguration.KeyMgmt.WPA_EAP
                // if (ssid.equals("Solo_MTS_WiFi")) open=false;
               //  if (ssid.equals("milos_pc_e5a084")) open=true;
                 //final List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();

                 wifiManager.startScan();
                 List<ScanResult> networkList = wifiManager.getScanResults();
                 for (ScanResult result : networkList) {
                     summ = result.SSID;
                     String temp = result.SSID;
                     String temp2 = ssid;
                     //summ=temp+"?"+temp2;

                         if (temp.contains("milos_pc_e5a084")/* || temp.contains("Solo_MTS_WiFi")*/ ) {
                             summ = "entered ssid, id=" + temp;

                            cap = result.capabilities;
                            ssid = temp;
                            if (result.capabilities.toUpperCase().contains("WPA") ||
                                 result.capabilities.toUpperCase().contains("WPA2")) open = false;
                            else open = true;

                            break;

                         }

                 }


                 infoText.setText("summ="+summ+" id wifi: "+ssid+" open="+open+" cap="+cap);
             }

             public synchronized void getWifiId0(){
                 WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService (Context.WIFI_SERVICE);
                 WifiInfo info = wifiManager.getConnectionInfo ();
                 ssid  = info.getSSID();

                 wifiManager.startScan();
                 List<ScanResult> networkList = wifiManager.getScanResults();
                 for (ScanResult result : networkList) {
                     summ = result.SSID;
                     String temp = result.SSID;
                     String temp2 = ssid;
                     //summ=temp+"?"+temp2;

                     if (temp.contains("milos_pc_e5a084")/* || temp.contains("Solo_MTS_WiFi")*/ ) {
                         summ = "entered ssid, id=" + temp;

                         cap = result.capabilities;
                         ssid = temp;
                         if (result.capabilities.toUpperCase().contains("WPA") ||
                                 result.capabilities.toUpperCase().contains("WPA2")) open = false;
                         else open = true;

                         break;

                     }

                 }


                 //infoText.setText("summ="+summ+" id wifi: "+ssid+" open="+open+" cap="+cap);
             }

             public void conWifi(View v){

                 /*
                 WifiConfiguration conf = new WifiConfiguration();
                 conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                 conf.SSID = "\"" + ssid + "\"";
                 conf.preSharedKey = "\""+ key +"\"";


                 WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                 wifiManager.addNetwork(conf);

                 List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                 for( WifiConfiguration i : list ) {
                     if(i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                         wifiManager.disconnect();
                         wifiManager.enableNetwork(i.networkId, true);
                         wifiManager.reconnect();

                         break;
                     }
                 }
                 */


                 WifiConfiguration wifiConfig = new WifiConfiguration();
                 wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                 wifiConfig.SSID = String.format("\"%s\"", ssid);

                 if (open) wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                 else wifiConfig.preSharedKey = String.format("\"%s\"", key);

                 WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//remember id
                 int netId = wifiManager.addNetwork(wifiConfig);
                 wifiManager.disconnect();
                 wifiManager.enableNetwork(netId, true);
                 wifiManager.reconnect();

             }

             public synchronized void conWifi0(){
                 WifiConfiguration wifiConfig = new WifiConfiguration();
                 wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                 wifiConfig.SSID = String.format("\"%s\"", ssid);

                 if (open) wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                 else wifiConfig.preSharedKey = String.format("\"%s\"", key);

                 WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//remember id
                 int netId = wifiManager.addNetwork(wifiConfig);
                 wifiManager.disconnect();
                 wifiManager.enableNetwork(netId, true);
                 wifiManager.reconnect();

             }

             public void clear(View v){
                    /*
                     clear0();


                 Calendar c00=new GregorianCalendar();
                 Calendar c_curr0=new GregorianCalendar();
                 while (c_curr0.getTimeInMillis()-c00.getTimeInMillis()<3000) {
                     c_curr0=new GregorianCalendar();
                 }
                 deviceController=null;

                     initDiscoveryService();
                     registerReceivers();
                     mDiscoveryDelegate.onServicesDevicesListUpdated();

                     while (!connected) {
                     }
                     ;

                     infoText.setText("OK!");
                     */



                 connected=false;

                 uav.nullDeviceController();
                 //deviceController.start();

                 //deviceController.dispose();


                 deviceController.stop();
                 while (true){
                     try {
                         if (deviceController.getState()==ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STOPPED) break;
                     } catch (ARControllerException e) {
                         e.printStackTrace();
                         infoText.setText("Error: "+e.toString());
                         break;
                     }
                 }



                 deviceController.removeStreamListener(this); // !
                 deviceController.removeListener(this);          // !


                 mArdiscoveryServiceConnection=null;
                 mArdiscoveryService=null;
                 deviceList=null;
                 mArdiscoveryServicesDevicesListUpdatedReceiver=null;
                 device=null;
                 deviceController=null;
                 infoText.setText("Nulled!");






             }


             public synchronized void clear0(){
                 connected=false;
                 uav.nullDeviceController();
                 //deviceController.start();

                 //deviceController.dispose();


                 deviceController.stop();
                 deviceController.removeStreamListener(this); // !
                 deviceController.removeListener(this);          // !

                 mArdiscoveryServiceConnection=null;
                 mArdiscoveryService=null;
                 deviceList=null;
                 mArdiscoveryServicesDevicesListUpdatedReceiver=null;
                 device=null;

                 infoText.setText("Nulled!");



             }

             HenonMap chipering=new HenonMap();
             int errorBlock=0;
             Exception e1;
             //boolean fin=false;
             //boolean finished=false;
             boolean connected=false;

             public void stopNupdate(View v){
                    update2();

             }

             private void update2() {

                 c000=new GregorianCalendar();
                 new LongOperationStop().execute("stopping");

                 //new LongOperation().execute("started");
             }


             private void update() throws Exception{

                       //  fin=false;
                            /*
                             while (!ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED.equals(uav.getPilotingState()))
                                 if (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING.equals(uav.getPilotingState()))
                                     land();
                            */

                             errorBlock = 1;


                             //chipering.update();



                /*
                ARCONTROLLER_ERROR_ENUM sec= deviceController.getFeatureARDrone3().sendNetworkSettingsWifiSecurity(ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_TYPE_ENUM.ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_TYPE_WPA2,
                chipering.getNewKey(),
                ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_KEYTYPE_ENUM.ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_KEYTYPE_MAX);

                 ARCONTROLLER_ERROR_ENUM a=deviceController.getFeatureCommon().sendCommonReboot();

                 Calendar c0=new GregorianCalendar();
                 Calendar c_curr=new GregorianCalendar();
                 while (c_curr.getTimeInMillis()-c0.getTimeInMillis()<2000) {
                     c_curr=new GregorianCalendar();
                 }
                */


                /*
                finished=false;

                new Thread()
                {
                    public void run() {

                        errorBlock=11;
                 getWifiId0();

                 while(true) {
                     errorBlock=2;
                     conWifi0();
                     Calendar c00=new GregorianCalendar();
                     Calendar c_curr0=new GregorianCalendar();
                     while (c_curr0.getTimeInMillis()-c00.getTimeInMillis()<2000) {
                         c_curr0=new GregorianCalendar();
                     }

                     WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService (Context.WIFI_SERVICE);
                     WifiInfo info = wifiManager.getConnectionInfo ();
                     ssid  = info.getSSID();
                     if (ssid.contains("milos_pc_e5a084"))
                         break;
                 }
                          }
                }.start();

                 finished=true;
                */

                 uav.nullDeviceController();
                 //deviceController.start();

                 //deviceController.dispose();


                 deviceController.stop();

                 //while (deviceController.getState()==ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STOPPING) {};

                 while (true){
                     try {
                         if (deviceController.getState()==ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STOPPED) break;
                     } catch (ARControllerException e) {
                         e.printStackTrace();
                         infoText.setText("Error: "+e.toString());
                         break;
                     }
                 }



                 deviceController.removeStreamListener(this); // !
                 deviceController.removeListener(this);          // !


                 mArdiscoveryServiceConnection=null;
                 mArdiscoveryService=null;
                 deviceList=null;
                 mArdiscoveryServicesDevicesListUpdatedReceiver=null;
                 device=null;
                 deviceController=null;

                 initBtn.callOnClick();
                 tolBtn.setEnabled(false);
                 while (true) {
                     if (deviceController!=null && uav.getDeviceController()!=null) break;
                 };

                 while (true){
                     if (deviceController.getState()==ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING) break;
                 }

                    infoText.setText("OK2!");


                                /*
                             errorBlock = 4;
                             while (!ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING.equals(uav.getPilotingState()))
                                 if (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED.equals(uav.getPilotingState()))
                                     takeoff();

                                 */

                             //infoText.setText("key="+chipering.getNewKey()+uav.getPilotingState().toString());



             }

             public void getState(View v) {
                 try {
                     infoText.setText("getState: "+deviceController.getState().toString()+" getExtensionState: "+deviceController.getExtensionState().toString());

                 } catch (ARControllerException e) {
                     e.printStackTrace();
                 }
             }

             public void setNull(View v) {
                 deviceController=null;
             }

            double uavalt;
             int uavi;

             Calendar c000;//=new GregorianCalendar();



             private class LongOperationStop extends AsyncTask<String, String, String> {

                 @Override
                 protected String doInBackground(String... params) {
                     publishProgress("Stopping");
                     uavalt=uav.alt;
                     uavi=uav.getiCurr();
                     uav.stop();


                     return "stop executed";
                 }

                 @Override
                 protected void onPostExecute(String result) {

                         new LongOperationWait5s00().execute("");




                 }

                 @Override
                 protected void onPreExecute() {
                     stopBtn.setEnabled(false);
                     updating=true;
                     joystick.setEnabled(false);
                     joystick2.setEnabled(false);

                 }

                 @Override
                 protected void onProgressUpdate(String... text) {
                     infoText.setText(text[0]);
                 }
             }

             private class LongOperationWait5s00 extends AsyncTask<String, String, String> {

                 @Override
                 protected String doInBackground(String... params) {
                     publishProgress("waiting stop");
                     try {

                         Thread.sleep(1500);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                         return "Wait stop interrupted";
                     }

                     return "wait stop executed";
                 }

                 @Override
                 protected void onPostExecute(String result) {
                     if (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING.equals(uav.getPilotingState())&&
                             uav.getStopped()){
                         infoText.setText(result);
                         new LongOperationLanding().execute("");
                     } else{
                         uav.stop();
                         new LongOperationWait5s00().execute("");
                     }




                 }

                 @Override
                 protected void onPreExecute() {


                 }

                 @Override
                 protected void onProgressUpdate(String... text) {
                     infoText.setText(text[0]);
                 }
             }



             private class LongOperationLanding extends AsyncTask<String, String, String> {

                 @Override
                 protected String doInBackground(String... params) {
                     publishProgress("Landing");
                     if (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING.equals(uav.getPilotingState()))
                             land();
                     Calendar c00=new GregorianCalendar();
                     Calendar c_curr0=new GregorianCalendar();
                     while (c_curr0.getTimeInMillis()-c00.getTimeInMillis()<2000) {
                         c_curr0=new GregorianCalendar();
                     }
                     return "Land executed";
                 }

                 @Override
                 protected void onPostExecute(String result) {
                     infoText.setText(result);

                     new LongOperationWait5s0().execute("");

                     // txt.setText(result);
                     // might want to change "executed" for the returned string passed
                     // into onPostExecute() but that is upto you
                 }

                 @Override
                 protected void onPreExecute() {


                 }

                 @Override
                 protected void onProgressUpdate(String... text) {
                     infoText.setText(text[0]);
                 }
             }

             private class LongOperationWait5s0 extends AsyncTask<String, String, String> {

                 @Override
                 protected String doInBackground(String... params) {
                     publishProgress("waiting");
                     try {
                         Thread.sleep(500);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                         return "Wait interrupted";
                     }

                     return "wait executed";
                 }

                 @Override
                 protected void onPostExecute(String result) {
                     if (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED.equals(uav.getPilotingState())){
                         infoText.setText(result);
                         new LongOperationNewKeyReboot().execute("");
                     } else{
                         new LongOperationWait5s0().execute("");
                     }




                 }

                 @Override
                 protected void onPreExecute() {


                 }

                 @Override
                 protected void onProgressUpdate(String... text) {
                     infoText.setText(text[0]);
                 }
             }


             private class LongOperationNewKeyReboot extends AsyncTask<String, String, String> {

                 @Override
                 protected String doInBackground(String... params) {
                     publishProgress("waiting","key waiting");
                     chipering.update();
                     key= chipering.getNewKey();

                     //here may be a problem
                     ARCONTROLLER_ERROR_ENUM sec= deviceController.getFeatureARDrone3().sendNetworkSettingsWifiSecurity(ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_TYPE_ENUM.ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_TYPE_WPA2,
                             key,
                             ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_KEYTYPE_ENUM.ARCOMMANDS_ARDRONE3_NETWORKSETTINGS_WIFISECURITY_KEYTYPE_MAX);

                     ARCONTROLLER_ERROR_ENUM a=deviceController.getFeatureCommon().sendCommonReboot();

                     publishProgress("key got!",key);
                     Calendar c00=new GregorianCalendar();
                     Calendar c_curr0=new GregorianCalendar();
                     while (c_curr0.getTimeInMillis()-c00.getTimeInMillis()<2000) {
                         c_curr0=new GregorianCalendar();
                     }

                     return key;
                 }

                 @Override
                 protected void onPostExecute(String result) {
                     keyText.setText("key: "+result);


                     new LongOperationTakeSnapshot().execute("");


                 }

                 @Override
                 protected void onPreExecute() {


                 }

                 @Override
                 protected void onProgressUpdate(String... text) {
                     infoText.setText(text[0]);
                     keyText.setText("key: "+text[1]);

                 }
             }



             private class LongOperationTakeSnapshot extends AsyncTask<String, String, String> {



                 @Override
                 protected String doInBackground(String... params) {
                     publishProgress("taking snapshot");

                     Date now = new Date();
                     android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
                     try {
                         // image naming and path  to include sd card  appending name you choose for file
                         String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

                         // create bitmap screen capture
                         View v1 = getWindow().getDecorView().getRootView();
                         v1.setDrawingCacheEnabled(true);
                         Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                         v1.setDrawingCacheEnabled(false);

                         File imageFile = new File(mPath);

                         FileOutputStream outputStream = new FileOutputStream(imageFile);
                         int quality = 100;
                         bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                         outputStream.flush();
                         outputStream.close();

                         publishProgress(imageFile.getAbsolutePath().toString());


                     } catch (Throwable e) {
                         // Several error may come out with file handling or DOM
                        publishProgress(e.toString());
                     }

                     /*
                     Calendar c00=new GregorianCalendar();
                     Calendar c_curr0=new GregorianCalendar();
                     while (c_curr0.getTimeInMillis()-c00.getTimeInMillis()<5000) {
                         c_curr0=new GregorianCalendar();
                     }
                     */

                     return "snapshot OK!";
                 }

                 @Override
                 protected void onPostExecute(String result) {
                     infoText.setText(result);
                     new LongOperationWaitAfterReboot().execute("");


                 }

                 @Override
                 protected void onPreExecute() {


                 }

                 @Override
                 protected void onProgressUpdate(String... text) {
                     infoText.setText(text[0]);
                 }
             }


             private class LongOperationWaitAfterReboot extends AsyncTask<String, String, String> {

                 @Override
                 protected String doInBackground(String... params) {
                     publishProgress("waiting after reboot");
                     try {
                         Thread.sleep(5000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                         return "Wait interrupted";
                     }

                     return "wait after reboot executed";
                 }

                 @Override
                 protected void onPostExecute(String result) {
                     infoText.setText(result);
                     new LongOperationGetWifi().execute("");


                 }

                 @Override
                 protected void onPreExecute() {


                 }

                 @Override
                 protected void onProgressUpdate(String... text) {
                     infoText.setText(text[0]);
                 }
             }


             private class LongOperationGetWifi extends AsyncTask<String, String, String> {

                 @Override
                 protected String doInBackground(String... params) {
                     publishProgress("Getting WiFi info");
                     WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService (Context.WIFI_SERVICE);
                     WifiInfo info = wifiManager.getConnectionInfo ();
                     ssid  = info.getSSID();
                     //WifiConfiguration.KeyMgmt.WPA_EAP
                     // if (ssid.equals("Solo_MTS_WiFi")) open=false;
                     //  if (ssid.equals("milos_pc_e5a084")) open=true;
                     //final List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();

                     wifiManager.startScan();
                     List<ScanResult> networkList = wifiManager.getScanResults();
                     for (ScanResult result : networkList) {
                         summ = result.SSID;
                         String temp = result.SSID;
                         String temp2 = ssid;
                         //summ=temp+"?"+temp2;

                         if (temp.contains("milos_pc_e5a084")/* || temp.contains("Solo_MTS_WiFi")*/ ) {
                             summ = "entered ssid, id=" + temp;

                             cap = result.capabilities;
                             ssid = temp;
                             if (result.capabilities.toUpperCase().contains("WPA") ||
                                     result.capabilities.toUpperCase().contains("WPA2")) open = false;
                             else open = true;

                             break;

                         }

                     }


                     //infoText.setText("summ="+summ+" id wifi: "+ssid+" open="+open+" cap="+cap);

                     return "Getting WiFi info executed: "+"summ="+summ+" id wifi: "+ssid+" open="+open+" cap="+cap;
                 }

                 @Override
                 protected void onPostExecute(String result) {
                     infoText.setText(result);
                     new LongOperationConnectWifi().execute("");


                 }

                 @Override
                 protected void onPreExecute() {


                 }

                 @Override
                 protected void onProgressUpdate(String... text) {
                     infoText.setText(text[0]);
                 }
             }

             private class LongOperationConnectWifi extends AsyncTask<String, String, String> {

                 @Override
                 protected String doInBackground(String... params) {
                     publishProgress("Connecting WiFi");
                     WifiConfiguration wifiConfig = new WifiConfiguration();
                     wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                     wifiConfig.SSID = String.format("\"%s\"", ssid);

                     if (open) wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                     else wifiConfig.preSharedKey = String.format("\"%s\"", key);

                     WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//remember id
                     int netId = wifiManager.addNetwork(wifiConfig);
                     wifiManager.disconnect();
                     wifiManager.enableNetwork(netId, true);
                     wifiManager.reconnect();
                     Calendar c00=new GregorianCalendar();
                     Calendar c_curr0=new GregorianCalendar();
                     while (c_curr0.getTimeInMillis()-c00.getTimeInMillis()<2000) {
                         c_curr0=new GregorianCalendar();
                     }


                     return "Connecting WiFi executed";
                 }

                 @Override
                 protected void onPostExecute(String result) {
                     infoText.setText(result);
                     new LongOperationReconnect().execute("");


                 }

                 @Override
                 protected void onPreExecute() {


                 }

                 @Override
                 protected void onProgressUpdate(String... text) {
                     infoText.setText(text[0]);
                 }
             }


             private class LongOperationReconnect extends AsyncTask<String, String, String> {

                 @Override
                 protected String doInBackground(String... params) {
                     publishProgress("Reconnection started");


                     infoText.setText("landed");

                     uav.nullDeviceController();
                     //deviceController.start();

                     //deviceController.dispose();


                     deviceController.stop();

                     //while (deviceController.getState()==ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STOPPING) {};

                     while (true){
                         try {
                             if (deviceController.getState()==ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_STOPPED) break;
                         } catch (ARControllerException e) {
                             publishProgress("before nulled after stop Error: "+e.toString());
                             return "before nulled after stop Error: "+e.toString();
                         }
                     }



                     deviceController.removeStreamListener(MapsActivity.this); // !
                     deviceController.removeListener(MapsActivity.this);          // !


                     mArdiscoveryServiceConnection=null;
                     mArdiscoveryService=null;
                     deviceList=null;
                     mArdiscoveryServicesDevicesListUpdatedReceiver=null;
                     device=null;
                     deviceController=null;

                     publishProgress("Nulled!");

                     initBtn.callOnClick();
                     tolBtn.setEnabled(false);

                     while (true) {
                         if (deviceController!=null && uav.getDeviceController()!=null) break;
                     };

                     while (true){
                         try {
                             if (deviceController.getState()==ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING) break;
                         } catch (ARControllerException e) {
                             publishProgress("after nulled Error: "+e.toString());
                             return "after nulled Error: "+e.toString();
                         }
                     }

                     publishProgress("connected!");
                     return "connected!";
                     /*
                     while (!ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING.equals(uav.getPilotingState()))
                         if (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED.equals(uav.getPilotingState()))
                             takeoff();
                     publishProgress("Take off");

                     return "Reconnect executed";
                     */

                 }

                 @Override
                 protected void onPostExecute(String result) {
                     infoText.setText(result);
                     tolBtn.setEnabled(false);
                     new LongOperationTake0ff().execute("");

                     // txt.setText(result);
                     // might want to change "executed" for the returned string passed
                     // into onPostExecute() but that is upto you
                 }

                 @Override
                 protected void onPreExecute() {


                 }

                 @Override
                 protected void onProgressUpdate(String... text) {
                     infoText.setText(text[0]);
                 }
             }

             private class LongOperationTake0ff extends AsyncTask<String, String, String> {

                 @Override
                 protected String doInBackground(String... params) {
                     publishProgress("Taking off");
                     if (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_LANDED.equals(uav.getPilotingState()))
                             takeoff();
                     Calendar c00=new GregorianCalendar();
                     Calendar c_curr0=new GregorianCalendar();
                     while (c_curr0.getTimeInMillis()-c00.getTimeInMillis()<2000) {
                         c_curr0=new GregorianCalendar();
                     }


                     return "Take off executed";
                 }

                 @Override
                 protected void onPostExecute(String result) {
                     infoText.setText(result);
                     new LongOperationWait5s1().execute("");
                     // txt.setText(result);
                     // might want to change "executed" for the returned string passed
                     // into onPostExecute() but that is upto you
                 }

                 @Override
                 protected void onPreExecute() {


                 }

                 @Override
                 protected void onProgressUpdate(String... text) {
                     infoText.setText(text[0]);
                 }
             }

             private class LongOperationWait5s1 extends AsyncTask<String, String, String> {

                 @Override
                 protected String doInBackground(String... params) {
                     publishProgress("waiting");
                     try {
                         Thread.sleep(500);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                         return "Wait interrupted";
                     }

                     return "wait executed";
                 }

                 @Override
                 protected void onPostExecute(String result) {
                     if (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING.equals(uav.getPilotingState())){
                         infoText.setText(result);

                         uav.setiStart(uavi);
                         uav.alt=uavalt;
                         onMyStart();

                         stopBtn.setEnabled(true);

                         infoText.setText("Continue flying");
                         Calendar c_curr0=new GregorianCalendar();
                         long ms=c_curr0.getTimeInMillis()-c000.getTimeInMillis();
                         infoText.setText("Continue flying, Milliseconds on update: "+ms); // 25 000-35 000 milliseconds on update
                         joystick.setEnabled(true);
                         joystick2.setEnabled(true);
                         updating=false;
                        // while (c_curr0.getTimeInMillis()-c00.getTimeInMillis()<2000) {
                         //    c_curr0=new GregorianCalendar();
                     } else{
                         new LongOperationWait5s1().execute("");
                     }


                     //new LongOperationReconnect().execute("");

                     // txt.setText(result);
                     // might want to change "executed" for the returned string passed
                     // into onPostExecute() but that is upto you
                 }

                 @Override
                 protected void onPreExecute() {


                 }

                 @Override
                 protected void onProgressUpdate(String... text) {
                     infoText.setText(text[0]);
                 }
             }

             public void onEncryp(View v) {
                 if (encrypChB.isChecked()) {
                     withPeriodText.setVisibility(View.VISIBLE);
                     periodEditText.setVisibility(View.VISIBLE);
                 } else
                 {
                     withPeriodText.setVisibility(View.INVISIBLE);
                     periodEditText.setVisibility(View.INVISIBLE);
                 }
             }

             public void setO(View v) {
                 Intent intent = new Intent(this, OGPSActivity.class);
                 startActivityForResult(intent, 1);
             }

             @Override
             protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                 if (data == null) {return;}
                 double lon = data.getDoubleExtra("lon",Olng);
                 double lat = data.getDoubleExtra("lat",Olat);
                 tempLon=lon;
                 tempLat=lat;
             }

         }
