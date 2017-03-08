import javax.json.Json;
import javax.json.JsonObject;

public class Gyroscope extends Capteur{

    private float x;
    private float y;
    private float z;

    public Gyroscope(){
        super();
        x = 0;
        y = 0;
        z = 0;
    }

    public Gyroscope(float x, float y, float z){
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setZ(int z){
        this.z = z;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getZ(){
        return z;
    }


    public JsonObject toSendJsonObject(){
        JsonObject obj = Json.createObjectBuilder()
                .add("type", "send")
                .add("sender_id", getId())
                .add("contents", Json.createObjectBuilder()
                        .add("x", getX())
                        .add("y", getY())
                        .add("z", getZ()))
                .build();
        return obj;
    }

}
