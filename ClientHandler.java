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

    public void registerHandler(JsonObject answerFromServer){
        if(answerFromServer.getJsonObject("ack").getString("resp").equals("ok")){
            c.setId(answerFromServer.getInt("sender_id"));
            System.out.println("Register OK");
        }
        else{
            System.out.println("Echec Register ");
        }
    }

    public void deregisterHandler(JsonObject answerFromServer){
        if(answerFromServer.getJsonObject("ack").getString("resp").equals("ok")){
            System.out.println("Deregister OK");
        }
        else{
            System.out.println("Echec Deregister");
        }
    }

    public void listHandler(JsonObject answerFromServer){

    }

    public void sendHandler(JsonObject answerFromServer){

    }



}
