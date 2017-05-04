package fr.rlydmz.bus;

import javax.json.*;

public class Capteur{

    private final static int MESSAGE_TAB_SIZE = 100;

    private String name;
    private String classe;
    private int id;
    private int currentMsgId;
    private Message[] msgTab;

    public Capteur(){
        name = new String();
        classe = new String();
        id=-1;
        currentMsgId = 0;
        msgTab = new Message[MESSAGE_TAB_SIZE];
    }


    public Capteur(String n){
        name = n;
        id=-1;
        currentMsgId = 0;
        msgTab = new Message[MESSAGE_TAB_SIZE];
    }


    public Capteur(String n, String c){
        classe = c;
        name = n;
        id=-1;
        currentMsgId = 0;
        msgTab = new Message[MESSAGE_TAB_SIZE];
    }

    public Capteur(String n, String c, int i, int cmi){
        name = n;
        classe = c;
        id = i;
        currentMsgId = cmi;
        msgTab = new Message[MESSAGE_TAB_SIZE];
    }

    public void setName(String n){
        name = n;
    }

    public void setClasse(String c){
        classe = c;
    }

    public void setId(int i){
        id = i;
    }

    public void setCurrentMsgId(int ncmi){
        currentMsgId = ncmi;
    }

    public void incCurrentMsgId(){
        currentMsgId++;
    }

    public String getName(){
        return name;
    }

    public String getClasse(){
        return classe;
    }

    public int getId(){
        return id;
    }

    public int getCurrentMsgId(){
        return currentMsgId;
    }

    public void addMessage(Message m){
        m.setId(getCurrentMsgId());
        msgTab[getCurrentMsgId()%MESSAGE_TAB_SIZE] = m;
        incCurrentMsgId();
    }

    public Message getMessage(int id){
        if(id < 0) {
            System.out.println("too small id");
            return null;
        }
        else if(id > currentMsgId) {
            System.out.println("too big id");
            return null;
        }
        else{
            return msgTab[id%MESSAGE_TAB_SIZE];
        }
    }

    public Message getLastMessage(){
        return msgTab[(getCurrentMsgId()-1)%MESSAGE_TAB_SIZE];
    }

    //Converti l'objet fr.rlydmz.bus.Capteur en objet Json
    public JsonObject toJson(){
        JsonObject obj = Json.createObjectBuilder()
                .add("sender_id", getId())
                .add("sender_class", getClasse())
                .add("sender_name", getName())
                .add("last_message_id", getCurrentMsgId())
                .build();
        return obj;
    }

    //Renvoie un objet Json au format d'une requete de type 'register'
    public JsonObject toRegisterJsonObject(){
        JsonObject obj = Json.createObjectBuilder()
                .add("type", "register")
                .add("sender_class", getClasse())
                .add("sender_name", getName())
                .build();
        return obj;
    }

    //Renvoie un objet Json au format d'une requete de type 'deregister'
    public JsonObject toDeregisterJsonObject(){
        JsonObject obj = Json.createObjectBuilder()
                .add("type", "deregister")
                .add("sender_id", getId())
                .build();
        return obj;
    }

    //Renvoie un objet Json au format d'une requete de type 'list' simple
    public JsonObject toListJsonObject(){
        JsonObject obj = Json.createObjectBuilder()
                .add("type", "list")
                .add("sender_class", getClasse())
                .build();
        return obj;
    }

    //Renvoie un objet Json au format d'une requete de type 'list' avec comme attribut son nom de classe
    public JsonObject toListViaClassJsonObject(){
        JsonObject obj = Json.createObjectBuilder()
                .add("type", "list")
                .add("sender_class", getClasse())
                .build();
        return obj;
    }

    //Renvoie un objet Json au format d'une requete de type 'list' avec comme attribut son nom d'identification
    public JsonObject toListViaNameJsonObject(){
        JsonObject obj = Json.createObjectBuilder()
                .add("type", "list")
                .add("sender_id", getName())
                .build();
        return obj;
    }

}
