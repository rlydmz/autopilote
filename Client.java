import javax.json.*;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class Client{

    private static JsonObject fromStringToJson(String str) {
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

        //Création d'un objet de type 'Accelerometter'
        Accelerometer acc = new Accelerometer(5,5,5);
        acc.setName("ACC1");
        acc.setClasse("Accelerometter");

        //Création du gestionnaire de requetes
        ClientHandler ch = new ClientHandler(acc);

        //Création d'un objet Json de type 'send' (voir méthode de classe dans Capteur.java)
        JsonObject accJsonObj = acc.toRegisterJsonObject();

        //Envoie de l'objet au serveur
        String str = accJsonObj.toString();
        System.out.println(str);
        oos.writeObject(str);

        while(ch.registerHandler(fromStringToJson((String)ois.readObject())) == false){
            System.out.println("Fail to register to server, retrying in 5 seconds");
            Thread.sleep(5000);
        }

        System.out.println(acc.getId());

        while(true){
            acc.setX(ThreadLocalRandom.current().nextInt(0, 200));
            acc.setY(ThreadLocalRandom.current().nextInt(0, 200));
            acc.setX(ThreadLocalRandom.current().nextInt(0, 200));
            oos.writeObject(acc.toSendJsonObject().toString());
            Thread.sleep(800);
        }

        //oos.close();
        //ois.close();
    }

}
