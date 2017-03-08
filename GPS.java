import javax.json.Json;
import javax.json.JsonObject;

public class GPS extends Capteur{

    private float latitude;
    private float longitude;

    public GPS(){
        super();
        latitude = 0;
        longitude = 0;
    }

    public GPS(float lat, float lgn){
        super();
        latitude = lat;
        longitude = lgn;
    }

    public void setLatitude(float lat){
        latitude = lat;
    }

    public void setLongitude(float lgn){
        longitude = lgn;
    }

    public float getLatitude(){
        return latitude;
    }

    public float getLongitude(){
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
