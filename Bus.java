import javax.json.*;
import java.io.*;
import java.util.ArrayList;

public class Bus {

    private int incId;
    private ArrayList<Capteur> capteurList;

    public Bus(){
        incId = 0;
        capteurList = new ArrayList<>();
    }

    public void incrementId(){
        incId++;
    }

    public JsonObject registerHandler(Capteur c){
        incId++;
        c.setId(incId);
        capteurList.add(c);
        JsonObject answer = Json.createObjectBuilder()
                .add("type", "register")
                .add("sender_id", incId)
                .add("ack", Json.createObjectBuilder()
                        .add("resp", "ok")
                ).build();
        return answer;
    }

}
