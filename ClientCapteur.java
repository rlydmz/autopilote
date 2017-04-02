import javax.json.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

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
        @SuppressWarnings("resource")
		Socket client = new Socket("127.0.0.1", 7182);

        
        //Création des flux d'entrées/sorties
        OutputStream outputDatas = client.getOutputStream();
        	

        //Création d'un objet de type 'Accelerometter'
        Accelerometer acc = new Accelerometer(5,5,5);
        acc.setName("ACC1");
        acc.setClasse("Accelerometter");
        
        JsonObject accJsonObj = acc.toSendJsonObject();
        Message accMsg = new Message(0, accJsonObj);
        
        outputDatas.write(accMsg.toCompleteJsonObject().toString().getBytes(Charset.forName("UTF-8")));
        
        
        
        
        outputDatas.close();

    }

}