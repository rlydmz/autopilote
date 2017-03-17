import javax.json.Json;
import javax.json.JsonObject;

public class Capteur{

    private String name;
    private String classe;
    private int id;

    public Capteur(){
        name = new String();
        classe = new String();
        id=-1;
    }

    public Capteur(String n, String c, int i){
        name = n;
        classe = c;
        id = i;
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

    public String getName(){
        return name;
    }

    public String getClasse(){
        return classe;
    }

    public int getId(){
        return id;
    }


    public JsonObject toRegisterJsonObject(){
        JsonObject obj = Json.createObjectBuilder()
                .add("type", "register")
                .add("sender_class", getClasse())
                .add("sender_name", getName())
                .build();
        return obj;
    }

    public JsonObject toDeregisterJsonObject(){
        JsonObject obj = Json.createObjectBuilder()
                .add("type", "deregister")
                .add("sender_id", getId())
                .build();
        return obj;
    }

    public JsonObject toListJsonObject(){
        JsonObject obj = Json.createObjectBuilder()
                .add("type", "list")
                .add("sender_class", getClasse())
                .build();
        return obj;
    }

    public JsonObject toListViaClassJsonObject(){
        JsonObject obj = Json.createObjectBuilder()
                .add("type", "list")
                .add("sender_class", getClasse())
                .build();
        return obj;
    }

    public JsonObject toListViaNameJsonObject(){
        JsonObject obj = Json.createObjectBuilder()
                .add("type", "list")
                .add("sender_id", getName())
                .build();
        return obj;
    }

}
