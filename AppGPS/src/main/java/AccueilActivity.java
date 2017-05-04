package com.example.raphael.appgps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;

import fr.rlydmz.bus.*;

public class AccueilActivity extends Activity implements LocationListener {

    public static JsonObject fromStringToJson(String str) {
        javax.json.JsonReader jsonReader = Json.createReader(new StringReader(str));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }

    private Button button;
    private TextView latitudeText;
    private TextView longitudeText;
    Socket client;
    private double latitude = 0;
    private double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        button = (Button) findViewById(R.id.updateButton);
        latitudeText = (TextView) findViewById(R.id.LatitudeValue);
        longitudeText = (TextView) findViewById(R.id.LongitudeValue);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Latitude", String.valueOf(latitude));
                Log.e("Longitude", String.valueOf(longitude));

                latitudeText.setText(Double.toString(latitude));
                longitudeText.setText(Double.toString(longitude));
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.e("AJAJAJBDKEBFZ ", "ezfzefezf");

                try {
                    client = new Socket("192.168.0.11", 7182);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Création des flux d'entrées/sorties
                ObjectOutputStream oos= null;
                try {
                    oos = new ObjectOutputStream(client.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(client.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Création d'un objet de type 'Accelerometter'
                GPS gps = new GPS();
                gps.setName("Buzz");
                gps.setClasse("GPS");

                //Création du gestionnaire de requetes
                ClientHandler ch = new ClientHandler(gps);

                //Création d'un objet Json de type 'send' (voir méthode de classe dans Capteur.java)
                JsonObject accJsonObj = gps.toRegisterJsonObject();

                //Envoie de l'objet au serveur
                String str = accJsonObj.toString();
                try {
                    oos.writeObject(str);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    while(ch.registerHandler(fromStringToJson((String)ois.readObject())) == false){
                        System.out.println("Fail to register to server, retrying in 5 seconds");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                String answer = "";

                while(true){

                    gps.setLatitude(latitude);
                    gps.setLongitude(longitude);

                    try {
                        oos.writeObject(gps.toSendJsonObject().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        answer = (String)ois.readObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(ch.sendHandler(fromStringToJson(answer)) == true){
                        System.out.println("Message bien reçu à " + System.currentTimeMillis());
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLatitude();
        System.out.println("lat" + latitude);
        System.out.println("long" + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
