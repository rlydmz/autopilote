package com.example.raphael.appgps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;

import fr.rlydmz.bus.ClientHandler;
import fr.rlydmz.bus.GPS;

public class ClientGPS implements LocationListener{

    public static JsonObject fromStringToJson(String str) {
        javax.json.JsonReader jsonReader = Json.createReader(new StringReader(str));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }

    Context context;
    private double latitude;
    private double longitude;

    public ClientGPS(Context context){
        latitude = 0;
        longitude = 0;
        this.context = context;
    }

    public void start() throws Exception{

        //Création de la socket client (port conforme aux spécifications)
        Socket client = new Socket("192.168.0.11", 7182);

        //Création des flux d'entrées/sorties
        ObjectOutputStream oos= new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

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
        oos.writeObject(str);

        while(ch.registerHandler(fromStringToJson((String)ois.readObject())) == false){
            System.out.println("Fail to register to server, retrying in 5 seconds");
            Thread.sleep(5000);
        }

        String answer;


        TextView txtView1 = (TextView) ((Activity)context).findViewById(R.id.LatitudeValue);
        TextView txtView2 = (TextView) ((Activity)context).findViewById(R.id.LongitudeValue);

        while(true){

            gps.setLatitude(latitude++);
            gps.setLongitude(longitude++);

            txtView1.setText(Double.toString(latitude));
            txtView2.setText(Double.toString(longitude));

            oos.writeObject(gps.toSendJsonObject().toString());

            answer = (String)ois.readObject();
            if(ch.sendHandler(fromStringToJson(answer)) == true){
                System.out.println("Message bien reçu à " + System.currentTimeMillis());
            }

            Thread.sleep(15);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        TextView txtView1 = (TextView) ((Activity)context).findViewById(R.id.LatitudeValue);
        txtView1.setText(Double.toString(location.getLatitude()));
        TextView txtView2 = (TextView) ((Activity)context).findViewById(R.id.LongitudeValue);
        txtView2.setText(Double.toString(location.getLongitude()));
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
