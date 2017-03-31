import javax.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Bus {

    private int incId;
    private ArrayList<Capteur> capteurList;

    public Bus(){
        incId = 0;
        capteurList = new ArrayList<Capteur>();
    }

    public void incrementId(){
        incId++;
    }

    public JsonObject registerHandler(Capteur c){
        incrementId();
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

    public JsonObject deregisterHandler(Capteur c){
        //A faire : enlever l'élément c de la liste
        JsonObject answer = Json.createObjectBuilder()
                .add("type", "deregister")
                .add("ack", Json.createObjectBuilder()
                    .add("resp", "ok")
                ).build();
        return answer;
    }

    public JsonObject listHandler(){
        JsonArray answerTab = Json.createArrayBuilder().build();
        for(Iterator<Capteur> it = capteurList.iterator(); it.hasNext();){
            answerTab.add(it.next().toJson());
        }
        JsonObject answer = Json.createObjectBuilder()
                .add("type", "list")
                .add("ack", Json.createObjectBuilder()
                    .add("resp", "ok"))
                .add("results", answerTab)
                .build();
        return answer;
    }

    /*
    public JsonObject listHandlerViaClass(String c){

    }

    public JsonObject listHandlerViaName(String n){

    }
    */

}
