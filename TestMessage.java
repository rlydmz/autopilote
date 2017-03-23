import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import java.io.OutputStream;
import java.net.Socket;

public class TestMessage {

    //Affiche le contenu de l'objet JSon de type "send"
    public static void printJsonSendObject(JsonObject obj){
        System.out.println("type : " + obj.getString("type"));
        System.out.println("sender_id : " + obj.getInt("sender_id"));
        System.out.println("contents : " + obj.getJsonObject("contents"));
    }

    public static void main(String[] args) throws Exception {

        //Création d'un objet de type 'Accelerometter'
        Accelerometer acc = new Accelerometer(5,5,5);
        acc.setName("ACC1");
        acc.setClasse("Accelerometter");

        //Création d'un objet Json de type 'send' (voir méthode de classe dans Capteur.java)
        JsonObject accJsonObj = acc.toSendJsonObject();

        printJsonSendObject(accJsonObj);

        Message m = new Message();
        m.setId(5);
        m.setContent(accJsonObj.getJsonObject("contents"));

        System.out.println(m.toString());

    }

}
