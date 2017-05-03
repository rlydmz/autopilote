import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class GetterClient {

    public static JsonObject fromStringToJson(String str) {
        JsonReader jsonReader = Json.createReader(new StringReader(str));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }

    public static void main(String[] args) throws Exception {

        //Création de la socket client (port conforme aux spécifications)
        Socket client = new Socket("127.0.0.1", 7182);

        //Création des flux d'entrées/sorties
        ObjectOutputStream oos= new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

        //Création du gestionnaire de requetes
        ClientHandler ch = new ClientHandler();

        //Création d'un objet Json de type 'send' (voir méthode de classe dans Capteur.java)
        JsonObject requestJsonObj = Json.createObjectBuilder()
                .add("type", "get_last")
                .add("sender_id", 1)
                .build();

        //Envoie de l'objet au serveur
        String str = requestJsonObj.toString();
        System.out.println(str);
        oos.writeObject(str);

        JsonObject answer = ch.getHandler(fromStringToJson((String)ois.readObject()));

        System.out.println(answer.toString());

        System.out.println(answer.getJsonObject("content"));

        oos.close();
        ois.close();
    }

}
