package fr.rlydmz.bus;

import javax.json.Json;
import javax.json.JsonObject;

public class ClientHandler {

    private Capteur c;

    public ClientHandler(){
        c = new Capteur();
    }

    public ClientHandler(Capteur cap){
        c = cap;
    }

    public boolean registerHandler(JsonObject answerFromServer){
        if(answerFromServer.getJsonObject("ack").getString("resp").equals("ok")){
            c.setId(answerFromServer.getInt("sender_id"));
            System.out.println("Register OK");
            return true;
        }
        else{
            System.out.println("Echec Register ");
            return false;
        }
    }

    public boolean deregisterHandler(JsonObject answerFromServer){
        if(answerFromServer.getJsonObject("ack").getString("resp").equals("ok")){
            System.out.println("Deregister OK");
            return true;
        }
        else{
            System.out.println("Echec Deregister");
            return false;
        }
    }

    public JsonObject listHandler(JsonObject answerFromServer){
        if(answerFromServer.getJsonObject("ack").getString("resp").equals("ok")){
            return answerFromServer;
        }
        else{
            System.out.println("Echec Deregister");
            return Json.createObjectBuilder().build();
        }
    }

    public boolean sendHandler(JsonObject answerFromServer){
        if(answerFromServer.getJsonObject("ack").getString("resp").equals("ok")){
            return true;
        }
        return false;
    }

    public JsonObject getHandler(JsonObject answerFromServer){
        if(answerFromServer.getJsonObject("ack").getString("resp").equals("ok")){
            return answerFromServer;
        }
        else{
            System.out.println("Echec réception du message d'id : " + answerFromServer.getInt("msg_id"));
            return Json.createObjectBuilder().build();
        }
    }

    public JsonObject getLastHandler(JsonObject answerFromServer){
        if(answerFromServer.getJsonObject("ack").getString("resp").equals("ok")){
            return answerFromServer;
        }
        else{
            System.out.println("Echec réception du dernier message");
            return Json.createObjectBuilder().build();
        }
    }

}
