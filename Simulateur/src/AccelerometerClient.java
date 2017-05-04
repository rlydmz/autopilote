import fr.rlydmz.bus.Accelerometer;
import fr.rlydmz.bus.ClientHandler;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class AccelerometerClient {

    private static JsonObject fromStringToJson(String str) {
        JsonReader jsonReader = Json.createReader(new StringReader(str));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }

    public void start() throws Exception {

        //Création de la socket client (port conforme aux spécifications)
        Socket client = new Socket("192.168.0.11", 7182);

        //Création des flux d'entrées/sorties
        ObjectOutputStream oos= new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());

        //Création d'un objet de type 'Accelerometter'
        Accelerometer acc = new Accelerometer(5,5,5);
        acc.setName("ACC1");
        acc.setClasse("Accelerometer");

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

        String answer;

        int x=0, y=0, z=0;

        while(true){

            acc.setX(x);
            acc.setY(y+=2);
            acc.setZ(z);

            if(y>500)
                y=0;

            oos.writeObject(acc.toSendJsonObject().toString());

            answer = (String)ois.readObject();
            if(ch.sendHandler(fromStringToJson(answer)) == true){
                System.out.println("Message bien reçu à " + System.currentTimeMillis());
            }

            Thread.sleep(15);
        }

        //oos.close();
        //ois.close();
    }

}
