import javax.json.*;
import java.io.*;
import java.net.Socket;

public class ClientCapteur{

    //Méthode permettant de vérifier si l'identification sur le serveur
    //a été fructueuse ou non
    //Si oui, la fonction renvoie 0
    //Sinon elle renvoie le code d'erreur associé
    //Pour le moment inutilisée
    //A utiliser plus tard lorsqu'on recevra une réponse de la part du serveur
    public static int checkRegistration(JsonObject obj){
        JsonObject ackObj = obj.getJsonObject("ack");
        if(ackObj.getString("resp").equals("ok"))
            return 0;
        else
            return ackObj.getInt("error_id");
    }

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

        //Création d'un objet Json de type 'send' (voir méthode de classe dans Capteur.java)
        JsonObject accJsonObj = acc.toSendJsonObject();

        //Envoie de l'objet au serveur
        jsw.writeObject(accJsonObj);

        jsw.close();
        outputDatas.close();

    }

}
