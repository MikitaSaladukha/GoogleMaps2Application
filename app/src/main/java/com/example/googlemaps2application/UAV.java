package com.example.googlemaps2application;

import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM;
import com.parrot.arsdk.arcommands.ARCOMMANDS_ARDRONE3_PILOTING_MOVETO_ORIENTATION_MODE_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_DICTIONARY_KEY_ENUM;
import com.parrot.arsdk.arcontroller.ARCONTROLLER_ERROR_ENUM;
import com.parrot.arsdk.arcontroller.ARControllerArgumentDictionary;
import com.parrot.arsdk.arcontroller.ARControllerDictionary;
import com.parrot.arsdk.arcontroller.ARControllerException;
import com.parrot.arsdk.arcontroller.ARDeviceController;
import com.parrot.arsdk.arcontroller.ARFeatureARDrone3;


import java.util.ArrayList;


public class UAV implements Runnable {

    private final String MY_TAG="myTag";
    private  ArrayList<Double> Lat;// = new ArrayList<Double>();
    private  ArrayList<Double> Lng;// = new ArrayList<Double>();
    public  double lat=0,lng=0, alt=0;// latIter,lngIter;
    private  boolean stopped;
    private boolean bigStart=false;
    private boolean pressed=false, pressed2=false;

    private byte gaz=0, pitch=0, yaw=0, roll=0;

    public ARDeviceController getDeviceController() {
        return deviceController;
    }

    private ARDeviceController deviceController;

    public String error="";

    public synchronized void setPressed(boolean p){
        pressed=p;
    }
    public synchronized boolean getPressed(){
        return pressed;
    }

    public synchronized void setPressed2(boolean p){
        pressed2=p;
    }
    public synchronized boolean getPressed2(){
        return pressed2;
    }

    public synchronized boolean getStopped(){
        return stopped;
    }


    public synchronized void setGaz(byte g){
        gaz=g;
    }

    public synchronized void setPitch(byte p){
        pitch=p;
    }

    public synchronized void setYaw(byte y){
        yaw=y;
    }

    public synchronized void setRoll(byte r){
        roll=r;
    }

    public UAV(){
    }

    public void nullDeviceController (){
        this.deviceController=null;
    }

    public void setDeviceController (ARDeviceController deviceController){
        this.deviceController=deviceController;
    }

    public synchronized void setBigStart(boolean bs){
        this.bigStart=bs;
    }

    public synchronized boolean isBigStart(){
        return bigStart;
    }

    private int iCurr=0,iStart=0;

    public void setiStart(int i){
        iStart=i;
    }

    public int getiCurr(){
        return iCurr;
    }

    public void start( ArrayList<Double> Lat,  ArrayList<Double> Lng){
        this.Lat=new ArrayList<Double>();
        this.Lng=new ArrayList<Double>();
        getCoordinates();
        this.Lat.add(this.lat);
        this.Lng.add(this.lng);

        this.Lat.addAll(Lat);
        this.Lng.addAll(Lng);

        stopped=false;
        error="";
        Thread thisThread = new Thread(this);
        thisThread.start();

    }





    public void run(){
        try {
            for (int i = iStart; i < Lat.size(); i++) {
                iCurr=i;

                if (getStopped() | getPressed() | getPressed2()) {
                    //getCoordinates();
                    //moveGPS(lat, lng, alt);
                    iCurr=iCurr-1;
                    return;
                }
                if (i<0) i=0;
                moveGPS(Lat.get(i), Lng.get(i), alt);

                try {

                    //if (i==0)
                    //    Thread.sleep(3000);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    error+=e.toString();
                    return;

                }

                while (getPilotingState() != ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING) {

                    if (getStopped() | getPressed() |getPressed2()) {
                        //getCoordinates();
                        //moveGPS(lat, lng, alt);
                        iCurr=iCurr-1;
                        return;
                    }
                }
                if (getStopped() | getPressed() | getPressed2()) {
                    //getCoordinates();
                    //moveGPS(lat, lng, alt);
                    iCurr=iCurr-1;
                    return;
                }

            }

        } catch (Exception e) {
            error+=e.toString();
            stop();//ARControllerException
            return;
        }
        stop();
        setBigStart(false);

    }



    public void move(){
        new Thread()
        {
            public void run() {
                /*
                getCoordinates();
                moveGPS(lat, lng, alt);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */
                while (getPressed() | getPressed2())
                        deviceController.getFeatureARDrone3().sendPilotingPCMD((byte)1, (byte)roll, (byte)pitch, (byte)yaw, (byte)gaz, (int)0);

            }
        }.start();
    }



    public synchronized void stop(){
        try {
            getCoordinates();
            moveGPS(lat, lng, alt);
            deviceController.getFeatureARDrone3().sendPilotingPCMD((byte)1, (byte)0, (byte)0, (byte)0, (byte)0, (int)0);

        } catch (Exception e){
            error=error+e.toString();
        } finally {
            stopped=true;
        }

    }



    public synchronized void getCoordinates()
    {
        ARControllerDictionary elementDictionary = null;
        try {
            elementDictionary = deviceController.getCommandElements(ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_POSITIONCHANGED);


            ARControllerArgumentDictionary<Object> args = elementDictionary.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
            if (args != null) {
                double latitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_POSITIONCHANGED_LATITUDE);
                double longitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_POSITIONCHANGED_LONGITUDE);
                double altitude = (double)args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_POSITIONCHANGED_ALTITUDE);
                this.lat=latitude;
                this.lng=longitude;
                this.alt=altitude;

            }


        } catch (ARControllerException e) {
            e.printStackTrace();
            error+=e.toString();
            stopped=true;

        }
    }


    public  synchronized ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM getPilotingState()
    {
        ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM flyingState = ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.eARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_UNKNOWN_ENUM_VALUE;
        if (deviceController != null)
        {
            try
            {
                ARControllerDictionary dict = deviceController.getCommandElements(ARCONTROLLER_DICTIONARY_KEY_ENUM.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED);
                if (dict != null)
                {
                    ARControllerArgumentDictionary<Object> args = dict.get(ARControllerDictionary.ARCONTROLLER_DICTIONARY_SINGLE_KEY);
                    if (args != null)
                    {
                        Integer flyingStateInt = (Integer) args.get(ARFeatureARDrone3.ARCONTROLLER_DICTIONARY_KEY_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE);
                        flyingState = ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.getFromValue(flyingStateInt);

                        //ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING
                    }
                }
            }
            catch (ARControllerException e)
            {
                e.printStackTrace();
                error+=e.toString();
                stop();
            }

            return flyingState;
        }

        return null; //MikkTA
    }

    public synchronized void moveGPS(double lat, double lng, double alt) {
        ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM flyingState = getPilotingState();
        if (ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING.equals(flyingState) ||
                ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_ENUM.ARCOMMANDS_ARDRONE3_PILOTINGSTATE_FLYINGSTATECHANGED_STATE_HOVERING.equals(flyingState)) {
            ARCONTROLLER_ERROR_ENUM error = deviceController.getFeatureARDrone3().sendPilotingMoveTo(lat,lng,alt, ARCOMMANDS_ARDRONE3_PILOTING_MOVETO_ORIENTATION_MODE_ENUM.ARCOMMANDS_ARDRONE3_PILOTING_MOVETO_ORIENTATION_MODE_TO_TARGET,0);
            //this.error+=error.toString()+" My "+ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK;
            if (error!=ARCONTROLLER_ERROR_ENUM.ARCONTROLLER_OK) this.error+=error+" My";

        }

    }
}
