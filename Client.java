import javax.json.*;
import java.io.*;
import java.net.Socket;

public class Client{

    public static void main(String[] args) throws Exception {

        //Création de la socket client (port conforme aux spécifications)
        Socket client = new Socket("127.0.0.1", 7182);

        //Création des flux d'entrées/sorties
        OutputStream outputDatas = client.getOutputStream();
        JsonWriter jsw = Json.createWriter(outputDatas);

        //Création d'un objet de type 'Accelerometter'
        Accelerometer acc = new Accelerometer(5,5,5);
        acc.setName("ACC1");
        acc.setClasse("Accelerometter");

        //Création du gestionnaire de requetes
        ClientHandler ch = new ClientHandler(acc);

        //Création d'un objet Json de type 'send' (voir méthode de classe dans Capteur.java)
        JsonObject accJsonObj = acc.toSendJsonObject();

        //Envoie de l'objet au serveur
        jsw.writeObject(accJsonObj);

        jsw.close();
        outputDatas.close();

    }

}
