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
        JsonArrayBuilder b = Json.createArrayBuilder();
        StringBuffer str =  new StringBuffer("");
        for(Iterator<Capteur> it = capteurList.iterator(); it.hasNext();){
            b.add(it.next().toJson());
        }
        JsonArray ar = b.build();
        JsonObject answer = Json.createObjectBuilder()
                .add("type", "list")
                .add("ack", Json.createObjectBuilder()
                    .add("resp", "ok"))
                .add("results", ar)
                .build();
        return answer;
    }


    public JsonObject listHandlerViaClass(String c){
        JsonArrayBuilder b = Json.createArrayBuilder();
        StringBuffer str =  new StringBuffer("");
        Capteur capteurTmp = new Capteur();
        for(Iterator<Capteur> it = capteurList.iterator(); it.hasNext();){
            capteurTmp = it.next();
            if(capteurTmp.getClasse().equals(c)) {
                b.add(capteurTmp.toJson());
            }
        }
        JsonArray ar = b.build();
        JsonObject answer = Json.createObjectBuilder()
                .add("type", "list")
                .add("ack", Json.createObjectBuilder()
                        .add("resp", "ok"))
                .add("results", ar)
                .build();
        return answer;
    }

    public JsonObject listHandlerViaName(String n){
        JsonArrayBuilder b = Json.createArrayBuilder();
        StringBuffer str =  new StringBuffer("");
        Capteur capteurTmp = new Capteur();
        for(Iterator<Capteur> it = capteurList.iterator(); it.hasNext();){
            capteurTmp = it.next();
            if(capteurTmp.getName().equals(n)) {
                b.add(capteurTmp.toJson());
            }
        }
        JsonArray ar = b.build();
        JsonObject answer = Json.createObjectBuilder()
                .add("type", "list")
                .add("ack", Json.createObjectBuilder()
                        .add("resp", "ok"))
                .add("results", ar)
                .build();
        return answer;
    }

}
