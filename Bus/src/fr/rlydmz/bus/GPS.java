package fr.rlydmz.bus;

import javax.json.Json;
import javax.json.JsonObject;

public class GPS extends Capteur{

    private double latitude;
    private double longitude;

    public GPS(){
        super();
        latitude = 0;
        longitude = 0;
        this.setClasse("fr.rlydmz.bus.GPS");
    }

    public GPS(double lat, double lgn){
        super();
        latitude = lat;
        longitude = lgn;
        this.setClasse("fr.rlydmz.bus.GPS");
    }

    public void setLatitude(double lat){
        latitude = lat;
    }

    public void setLongitude(double lgn){
        longitude = lgn;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public JsonObject toSendJsonObject(){
        JsonObject obj = Json.createObjectBuilder()
                .add("type", "send")
                .add("sender_id", getId())
                .add("contents", Json.createObjectBuilder()
                        .add("lat", getLatitude())
                        .add("lng", getLongitude()))
                .build();
        return obj;
    }

}
